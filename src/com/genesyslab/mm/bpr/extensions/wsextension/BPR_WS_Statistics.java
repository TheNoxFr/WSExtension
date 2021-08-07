/* Decompiler 22ms, total 115ms, lines 293 */
package com.genesyslab.mm.bpr.extensions.wsextension;

import Genesys.ComLib.TKV.TKVList;
import com.genesys.statserver.UnboundThreadException;
import com.genesys.statserver.statistics.ReportingSettings;
import com.genesys.statserver.statistics.StatType;
import com.genesys.statserver.statistics.Statistics;
import com.genesyslab.mm.bpr.extensions.wsextension.BPR_WS_TranslationData.ObjectRegistryEnum;
import com.genesyslab.mm.bpr.extensions.logger.ILogger;
import java.util.Hashtable;

public class BPR_WS_Statistics extends Statistics {
   private BPR_WS_Extension parent;
   private BPR_WS_TranslationData translationData;
   private BPR_WS_StatObject statObject;
   private ILogger logger;
   private IWSService wsService;
   private String jsonBody;

   BPR_WS_Statistics(BPR_WS_Extension var1, BPR_WS_TranslationData var2, IWSService var3, BPR_WS_StatObject var4, StatType var5, ReportingSettings var6, Hashtable<String,String> var7) throws UnboundThreadException, IllegalArgumentException {
      super(var4, var5, var6, var7);
      if (null == var1) {
         throw new IllegalArgumentException("BPR_WS_Statistics::BPR_WS_Statistics: parent can't be 'null'");
      } else if (null == var3) {
         throw new IllegalArgumentException("BPR_WS_Statistics::BPR_WS_Statistics: dbService can't be 'null'");
      } else {
         this.parent = var1;
         this.logger = var1;
         this.statObject = var4;
         this.wsService = var3;
         this.translationData = var2;
         this.updateTranslationData(var7);
         this.initBody(var7);
      }
   }

   private void updateTranslationData(Hashtable<String,String> var1) {
      try {
         String var3;
         for(int var4 = 0; var4 <= ObjectRegistryEnum.getMaxIndex(); ++var4) {
            ObjectRegistryEnum var2 = ObjectRegistryEnum.getByIndex(var4);
            var3 = (String)var1.get(var2.columnOptionName());
            this.translationData.setColumnNameByObjectType(var3, var2);
            var3 = (String)var1.get(var2.dataOptionName());
            this.translationData.setDataTypeByObjectType(var3, var2);
         }

         var3 = (String)var1.get("use-tenant");
         this.translationData.setUseTenant(var3);
      } catch (Exception var5) {
      }

      this.logger.debug("BPR_WS_Statistics::updateTranslationData:");
      this.logger.debug(this.translationData.toString());
   }

   private void initBody(Hashtable<String, String> var1) {
      try {
         String statname = (String)var1.get("ws-name");
         String dbid = this.getObjectDBIDByType(this.statObject.getProperties(), "DBID");

         this.jsonBody = "{\"Id\":\"" + dbid + "\",\"Stat\":\"" + statname + "\"}";
      } catch (Exception ex)
      {}
   }

   private String getObjectDBIDByType(TKVList var1, String var2) {
      if (null == var1) {
         this.logger.error("BPR_WS_Statistics::getObjectDBIDByType: object properties are empty, can't get DBIDs");
         return null;
      } else {
         try {
            return var1.TKVListGetStringValue(var2);
         } catch (Exception var4) {
            this.logger.error("BPR_WS_Statistics::getObjectDBIDByType: value with the key '{0}' is empty", new Object[]{var2});
            return null;
         }
      }
   }

   protected Object getValue() {
      if (null == this.wsService) {
         this.logger.error("BPStatistics::getValue: wsService isn't available");
         return null;
      } 

      Double var1 = this.wsService.call(this.jsonBody);
      if (null != var1) {
         this.logger.debug("BPR_WS_Statistics::getValue: returned value: " + var1.toString());
      } else {
         this.logger.debug("BPR_WS_Statistics::getValue: returned value is 'null'");
      }

         return var1;
   }

   protected void onClose() {
      this.logger.debug("BPR_WS_Statistics::onClose");
      this.parent.removeStatistics(this);
      this.wsService = null;
   }

   protected void reset() {
   }

   protected Object takeSample() {
      return null;
   }
   
}
