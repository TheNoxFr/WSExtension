package com.genesyslab.mm.bpr.extensions.wsextension;

import Genesys.ComLib.TKV.TKVList;
import com.genesys.statserver.InvalidReferenceException;
import com.genesys.statserver.UnboundThreadException;
import com.genesys.statserver.extensibility.Extension;
import com.genesys.statserver.statistics.ReportingSettings;
import com.genesys.statserver.statistics.StatCalculator;
import com.genesys.statserver.statistics.StatObject;
import com.genesys.statserver.statistics.StatType;
import com.genesys.statserver.statistics.Statistics;
import com.genesys.statserver.statistics.UnsupportedStatTypeException;
import com.genesyslab.mm.bpr.extensions.wsextension.BPR_WS_TranslationData.ObjectRegistryEnum;
import com.genesyslab.mm.bpr.extensions.logger.ILogger;
import com.genesyslab.mm.bpr.extensions.logger.Level;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Vector;

public class BPR_WS_Extension extends Extension implements StatCalculator, ILogger {
   private Hashtable<String, BPR_WS_StatType> supportedStatTypes = new Hashtable<String, BPR_WS_StatType>();
   private Properties wsProperties = new Properties(); // Propriétés du WebService
   private Hashtable<String, String> tenantIds = new Hashtable<String, String>();
   private Vector<Statistics> currentStatistics = new Vector<Statistics>();
   private IWSService wsService;
   private Hashtable<String, BPR_WS_Extension> loggersMap = new Hashtable<String, BPR_WS_Extension>();
   private Level logLevel;
   private SimpleDateFormat formatter;
   private BPR_WS_TranslationData translationData;

   public BPR_WS_Extension() {
      this.logLevel = Level.ERROR;
      this.formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      this.translationData = new BPR_WS_TranslationData();
      this.initSupportedStatTypes();
   }

   private void initSupportedStatTypes() {
      BPR_WS_StatType var1 = new BPR_WS_StatType(this);
      this.supportedStatTypes.put(var1.getName(), var1);
   }

   private void releaseSupportedStatTypes() {
      Enumeration<String> var1 = this.supportedStatTypes.keys();

      while(var1.hasMoreElements()) {
         BPR_WS_StatType var2 = (BPR_WS_StatType)this.supportedStatTypes.remove(var1.nextElement());
         if (null != var2) {
            var2.release();
         }
      }

   }

   public String getName() {
      return "BPR_WS_Extension";
   }

   protected void init() {
      this.initLogger();
      this.initWSProperties();
      this.updateTranslationData();
      this.initWSService();
      this.printStatServerInformation();
   }

   private void initLogger() {
      try {
         this.logLevel = Level.toLevel(this.getParameter("verbose"), Level.WARN);
         this.loggersMap.put(Thread.currentThread().getName(), this);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
/*
   private void parseTenantIds() {
      try {
         String var1 = this.getParameter("tenant-ids");
         StringTokenizer var2 = new StringTokenizer(var1, "=,;");
         this.debug("BPR_WS_Extension::parseTenantIds: option 'tenant-ids': " + var1);

         while(var2.hasMoreElements()) {
            String var3 = var2.nextToken().trim();
            String var4 = var2.nextToken().trim();
            this.tenantIds.put(var3, var4);
         }
      } catch (Exception var5) {
         this.error((Object)("BPR_WS_Extension::parseTenantIds: unable to get/parse option tenant-ids:" + this.tenantIds.toString()), (Throwable)var5);
      }

   }
*/
   private void updateTranslationData() {
      try {
         String var2;
         for(int var3 = 0; var3 <= ObjectRegistryEnum.getMaxIndex(); ++var3) {
            ObjectRegistryEnum var1 = ObjectRegistryEnum.getByIndex(var3);
            var2 = this.getParameter(var1.columnOptionName());
            this.translationData.setColumnNameByObjectType(var2, var1);
            var2 = this.getParameter(var1.dataOptionName());
            this.translationData.setDataTypeByObjectType(var2, var1);
         }

         var2 = this.getParameter("use-tenant");
         this.translationData.setUseTenant(var2);
      } catch (Exception var4) {
      }

      this.debug("BPR_WS_Extension::updateTranslationData:");
      this.debug(this.translationData.toString());
   }

   // Initialise les paramètres du WebService
   private void initWSProperties() {
      this.wsProperties.setProperty("ws-url", this.getParameter("ws-url"));
      this.wsProperties.setProperty("user", this.getParameter("user"));
      this.wsProperties.setProperty("password", this.getParameter("password"));
      this.debug("BPR_WS_Extension::initWSProperties:");
      this.debug("\tws-url: " + this.wsProperties.getProperty("ws-url"));
      this.debug("\tuser: " + this.wsProperties.getProperty("user"));
   }

   private void initWSService() {
      this.wsService = WSServiceFactory.getWSService(this.wsProperties, this);
      if (null != this.wsService) {
         this.wsService.init();
      } else {
         this.error("BPR_WS_Extension::initWSService: can't initialize DB Service");
      }

   }

   private void printStatServerInformation() {
      this.debug("BPR_WS_Extension::printStatServerInformation:\tImplemented following " + this.supportedStatTypes.size() + " statistics: ");
      Enumeration<String> var1 = this.supportedStatTypes.keys();

      while(true) {
         try {
            this.debug("\t\t" + var1.nextElement().toString());
         } catch (NoSuchElementException var3) {
            return;
         }
      }
   }

   protected void shutdown() {
      this.debug("BPR_WS_Extension::shutdown");
      this.releaseSupportedStatTypes();
      if (null != this.wsService) {
         //this.dbService.shutdown();
         this.wsService = null;
      }

      this.shutdownLogger();
   }

   private void shutdownLogger() {
      try {
         this.loggersMap.remove(Thread.currentThread().getName());
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   void removeStatistics(Statistics var1) {
      this.currentStatistics.removeElement(var1);
   }

   public Enumeration<BPR_WS_StatType> getSupportedStatTypes() throws InvalidReferenceException {
      this.debug("BPR_WS_Extension::getSupportedStatTypes: " + this.supportedStatTypes.toString());
      return this.supportedStatTypes.elements();
   }

   public StatType findStatType(String var1) throws InvalidReferenceException {
      this.debug("BPR_WS_Extension::findStatType: " + var1);
      return null == var1 ? null : (BPR_WS_StatType)this.supportedStatTypes.get(var1);
   }

   public Statistics createStatistics(String var1, StatObject var2, ReportingSettings var3, Hashtable var4) throws IllegalArgumentException, UnsupportedStatTypeException, InvalidReferenceException {
      this.debug("BPR_WS_Extension::createStatistics: " + var1);
      BPR_WS_StatType var5 = (BPR_WS_StatType)this.findStatType(var1);
      if (var5 == null) {
         this.error("BPR_WS_Extension::createStatistics: unsupported stat-type: " + var1);
         throw new UnsupportedStatTypeException(var1);
      } else if (!var5.isValidObjectType(var2.getType())) {
         this.error("BPR_WS_Extension::createStatistics: unsupported type of Object: " + Integer.toString(var2.getType()));
         throw new IllegalArgumentException("BPR_WS_Extension::createStatistics: unsupported type of Object");
      } else {
         this.printStatObjectProperties(var2);
         Integer var6 = new Integer(0);

         try {
            var6 = new Integer(var2.getProperties().TKVListGetStringValue("TENANT_DBID"));
            this.debug("BPR_WS_Extension::createStatistics: tenant id from object: " + var6);
         } catch (Exception var13) {
            this.error((Object)"BPR_WS_Extension::createStatistics: tenant id is not delivered with object or problem with conversion: ", (Throwable)var13);
         }

         if (0 >= var6) {
            try {
               var6 = new Integer((String)this.tenantIds.get(var2.getTenantName()));
            } catch (Exception var12) {
               this.error("BPR_WS_Extension::createStatistics: unsupported Tenant or wrong format for option 'tenant-ids': " + var2.getTenantName());
               this.debug("BPR_WS_Extension::createStatistics: tenantIds: " + this.tenantIds.toString());
               throw new IllegalArgumentException("BPR_WS_Extension::createStatistics: unsupported Tenant or wrong format for option 'tenant-ids': " + var2.getTenantName());
            }
         }

         BPR_WS_StatObject var7 = new BPR_WS_StatObject(var6, var2.getType(), var2.getID(), var2.getTenantName(), var2.getProperties());
         this.debug("BPR_WS_Extension::createStatistics: new stat-object created:");
         this.debug("\tobject name: " + var2.getID());
         this.debug("\tobject type: " + ObjectRegistryEnum.getByIndex(var7.getType()).toString());
         this.debug("\ttenant name: " + var2.getTenantName());
         this.debug("\ttenant dbid: " + var7.getTenantId());
         BPR_WS_Statistics var8 = null;

         try {
            var8 = new BPR_WS_Statistics(this, (BPR_WS_TranslationData)this.translationData.clone(), this.wsService, var7, var5, var3, var4);
         } catch (UnboundThreadException var10) {
            this.error((Object)"BPR_WS_Extension::createStatistics: can't create statistics", (Throwable)var10);
            return null;
         } catch (Exception var11) {
            this.error((Object)"BPR_WS_Extension::createStatistics: can't create statistics", (Throwable)var11);
            return null;
         }

         this.currentStatistics.addElement(var8);
         return var8;
      }
   }

   private void printStatObjectProperties(StatObject var1) {
      if (null != var1) {
         TKVList var2 = var1.getProperties();
         if (null != var2) {
            ByteArrayOutputStream var3 = new ByteArrayOutputStream();
            var2.TKVListPrint(new PrintStream(var3), var1.getID());
            this.debug("BPR_WS_Extension::printStatObjectProperties:\n" + var3.toString());
         }
      }

   }

   public void setLevel(Level var1) {
      if (null != var1) {
         this.logLevel = var1;
      }

   }

   public Level getLevel() {
      return this.logLevel;
   }

   public void debug(Object var1) {
      if (Level.DEBUG.isGreaterOrEqual(this.logLevel)) {
         this.logMessage(var1, (Throwable)null);
      }

   }

   public void debug(Object var1, Throwable var2) {
      if (Level.DEBUG.isGreaterOrEqual(this.logLevel)) {
         this.logMessage(var1, var2);
      }

   }

   public void debug(String var1, Object[] var2) {
      if (Level.DEBUG.isGreaterOrEqual(this.logLevel)) {
         this.logParameters(var1, var2);
      }

   }

   public void info(Object var1) {
      if (Level.INFO.isGreaterOrEqual(this.logLevel)) {
         this.logMessage(var1, (Throwable)null);
      }

   }

   public void info(Object var1, Throwable var2) {
      if (Level.INFO.isGreaterOrEqual(this.logLevel)) {
         this.logMessage(var1, var2);
      }

   }

   public void info(String var1, Object[] var2) {
      if (Level.INFO.isGreaterOrEqual(this.logLevel)) {
         this.logParameters(var1, var2);
      }

   }

   public void warning(Object var1) {
      if (Level.WARN.isGreaterOrEqual(this.logLevel)) {
         this.logMessage(var1, (Throwable)null);
      }

   }

   public void warning(Object var1, Throwable var2) {
      if (Level.WARN.isGreaterOrEqual(this.logLevel)) {
         this.logMessage(var1, var2);
      }

   }

   public void warning(String var1, Object[] var2) {
      if (Level.WARN.isGreaterOrEqual(this.logLevel)) {
         this.logParameters(var1, var2);
      }

   }

   public void error(Object var1) {
      if (Level.ERROR.isGreaterOrEqual(this.logLevel)) {
         this.logMessage(var1, (Throwable)null);
      }

   }

   public void error(Object var1, Throwable var2) {
      if (Level.ERROR.isGreaterOrEqual(this.logLevel)) {
         this.logMessage(var1, var2);
      }

   }

   public void error(String var1, Object[] var2) {
      if (Level.ERROR.isGreaterOrEqual(this.logLevel)) {
         this.logParameters(var1, var2);
      }

   }

   public void fatal(Object var1) {
      if (Level.FATAL.isGreaterOrEqual(this.logLevel)) {
         this.logMessage(var1, (Throwable)null);
      }

   }

   public void fatal(Object var1, Throwable var2) {
      if (Level.FATAL.isGreaterOrEqual(this.logLevel)) {
         this.logMessage(var1, var2);
      }

   }

   public void fatal(String var1, Object[] var2) {
      if (Level.FATAL.isGreaterOrEqual(this.logLevel)) {
         this.logParameters(var1, var2);
      }

   }

   private void logParameters(String var1, Object[] var2) {
      try {
         this.logMessage(MessageFormat.format(var1, var2), (Throwable)null);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   private void logConsole(String var1, Object var2, Throwable var3) {
      if (null != var3) {
         if (null != var2) {
            this.logConsole(var1 + var2.toString() + "  " + var3.toString());
         } else {
            this.logConsole(var1 + var3.toString());
         }

         if (var3 instanceof SQLException) {
            this.logConsole(var1 + "SQL Exception Error code: " + ((SQLException)var3).getErrorCode());
            this.logConsole(var1 + "SQL Exception state: " + ((SQLException)var3).getSQLState());
         }

         StackTraceElement[] var4 = var3.getStackTrace();
         if (null != var4) {
            for(int var5 = 0; var5 < var4.length; ++var5) {
               this.logConsole(var1 + var4[var5].toString());
            }
         }
      } else if (null != var2) {
         this.logConsole(var1 + var2.toString());
      }

   }

   private void logConsole(String var1) {
      System.out.println(this.formatter.format(new Date(System.currentTimeMillis())) + "> Java:  " + var1);
   }

   private void logMessage(Object var1, Throwable var2) {
      try {
         String var3 = Thread.currentThread().getName();
         String var4 = "'" + var3 + "'  ";
         BPR_WS_Extension var5 = (BPR_WS_Extension)this.loggersMap.get(var3);
         if (null != var5) {
            if (null != var2) {
               if (null != var1) {
                  var5.log(var4 + var1.toString(), var2);
               } else {
                  var5.log(var4, var2);
               }

               if (var2 instanceof SQLException) {
                  var5.log(var4 + "SQL Exception Error code: " + ((SQLException)var2).getErrorCode());
                  var5.log(var4 + "SQL Exception state: " + ((SQLException)var2).getSQLState());
               }

               StackTraceElement[] var6 = var2.getStackTrace();
               if (null != var6) {
                  for(int var7 = 0; var7 < var6.length; ++var7) {
                     var5.log(var4 + var6[var7].toString());
                  }
               }
            } else if (null != var1) {
               var5.log(var4 + var1.toString());
            } else {
               var5.log(var4);
            }
         } else {
            this.logConsole(var4, var1, var2);
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }
}
