/* Decompiler 22ms, total 115ms, lines 293 */
package com.genesyslab.mm.bpr.extensions.wsextension;

import Genesys.ComLib.TKV.TKVList;
import com.genesys.statserver.UnboundThreadException;
import com.genesys.statserver.statistics.ReportingSettings;
import com.genesys.statserver.statistics.StatType;
import com.genesys.statserver.statistics.Statistics;
import com.genesyslab.mm.bpr.extensions.wsextension.BPR_WS_TranslationData.DataTypeEnum;
import com.genesyslab.mm.bpr.extensions.wsextension.BPR_WS_TranslationData.ObjectRegistryEnum;
import com.genesyslab.mm.bpr.extensions.logger.ILogger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class BPR_WS_Statistics extends Statistics {
   private BPR_WS_Extension parent;
   private BPR_WS_TranslationData translationData;
   private BPR_WS_StatObject statObject;
   private ILogger logger;
   private IWSService wsService;
   private String sel_value = "count(*)";
   private String sel_source = "interactions";
   private String sel_condition;
   private String sel_filter;
   private String sqlExpression;
   private String uriComplete;
   private PreparedStatement preparedStatement;

   BPR_WS_Statistics(BPR_WS_Extension var1, BPR_WS_TranslationData var2, IWSService var3, BPR_WS_StatObject var4, StatType var5, ReportingSettings var6, Hashtable var7) throws UnboundThreadException, IllegalArgumentException {
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
         this.initSqlExpressionComponents(var7);
         //this.initFilter(var7);
         this.updateTranslationData(var7);
         //this.composeSqlExpression();
         this.initURI(var7);
         //this.preparedStatement = var3.prepareStatement(this.sqlExpression);
      }
   }

   private void updateTranslationData(Hashtable var1) {
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

   private void initURI(Hashtable var1) {
      try {
         String statname = (String)var1.get("ws-name");
         String dbid = this.getObjectDBIDByType(this.statObject.getProperties(), "DBID");

         this.uriComplete = "/" + statname + "/" + dbid;
      } catch (Exception ex)
      {}
   }

   private void initSqlExpressionComponents(Hashtable var1) {
      try {
         String var2 = (String)var1.get("sel-value");
         if (null != var2) {
            this.sel_value = var2;
         }

         var2 = (String)var1.get("sel-source");
         if (null != var2) {
            this.sel_source = var2;
         }

         var2 = (String)var1.get("sel-condition");
         if (null != var2) {
            this.sel_condition = var2;
         }
      } catch (Exception var3) {
      }

      this.logger.debug("BPR_WS_Statistics::initSqlExpressionComponents:");
      this.logger.debug("\tsel-value: " + this.sel_value);
      this.logger.debug("\tsel-source: " + this.sel_source);
      this.logger.debug("\tsel-condition: " + this.sel_condition);
   }
/*
   private void initFilter(Hashtable var1) {
      try {
         String var2 = (String)var1.get("Filter");
         this.logger.debug("BPR_WS_Statistics::initFilter: attempting to parse filter: " + var2);
         StringTokenizer var3 = new StringTokenizer(var2, "{}");

         while(var3.hasMoreElements()) {
            String var4 = var3.nextToken().trim();
            if (var4.startsWith("WHERE=")) {
               this.sel_filter = var4.substring("WHERE=".length()).trim();
               break;
            }
         }
      } catch (Exception var5) {
      }

      this.logger.debug("BPR_WS_Statistics::initFilter: " + this.sel_filter);
   }
   */
/*
   private void composeSqlExpression() {
      StringBuffer var1 = new StringBuffer(1024);
      ObjectRegistryEnum var2 = ObjectRegistryEnum.getByIndex(this.statObject.getType());
      var1.append("select ");
      var1.append(this.sel_value);
      var1.append(" from ");
      var1.append(this.sel_source);
      var1.append(" where ");
      if (this.translationData.isUseTenant()) {
         var1.append(this.translationData.getColumnNameByObjectType(ObjectRegistryEnum.OBJECT_TENANT));
         var1.append("='");
         if (DataTypeEnum.name == this.translationData.getDataTypeByObjectType(ObjectRegistryEnum.OBJECT_TENANT)) {
            var1.append(this.statObject.getTenantName());
         } else {
            var1.append(this.statObject.getTenantId());
         }

         if (ObjectRegistryEnum.OBJECT_TENANT != var2) {
            var1.append("' and ");
         } else {
            var1.append('\'');
         }
      }

      if (ObjectRegistryEnum.OBJECT_TENANT != var2 || ObjectRegistryEnum.OBJECT_TENANT == var2 && !this.translationData.isUseTenant()) {
         if (DataTypeEnum.name == this.translationData.getDataTypeByObjectType(var2)) {
            var1.append(this.translationData.getColumnNameByObjectType(var2));
            var1.append("='");
            var1.append(this.statObject.getID());
         } else if (ObjectRegistryEnum.OBJECT_CAMPAIGNGROUP != var2 && ObjectRegistryEnum.OBJECT_CAMPAIGNCALLINGLIST != var2) {
            var1.append(this.translationData.getColumnNameByObjectType(var2));
            var1.append("='");
            String var3 = this.getObjectDBIDByType(this.statObject.getProperties(), "DBID");
            if (null != var3) {
               var1.append(var3);
            } else {
               this.logger.error("BPR_WS_Statistics::composeSqlExpression: object id is not delivered with object or problem with conversion");
               var1.append("0");
            }
         } else {
            this.composeSqlExpressionForCampaignObjects(var1, var2);
         }

         var1.append('\'');
      }

      if (null != this.sel_condition) {
         var1.append(" and ");
         var1.append(this.sel_condition);
      }

      if (null != this.sel_filter) {
         var1.append(" and ");
         var1.append(this.sel_filter);
      }

      this.sqlExpression = var1.toString();
      this.logger.debug("BPR_WS_Statistics::composeSqlExpression: " + this.sqlExpression);
   }

   */
/*
   private void composeSqlExpressionForCampaignObjects(StringBuffer var1, ObjectRegistryEnum var2) {
      if (null != var1 && null != var2) {
         TKVList var3 = this.statObject.getProperties();
         if (null == var3) {
            this.logger.error("BPR_WS_Statistics::composeSqlExpressionForCampaignObjects: object properties are empty, can't get DBIDs");
         } else {
            var1.append(this.translationData.getColumnNameByObjectType(ObjectRegistryEnum.OBJECT_CAMPAIGN));
            var1.append("='");
            String var4 = this.getObjectDBIDByType(var3, "CAMPAIGN_DBID");
            if (null != var4) {
               var1.append(var4);
            } else {
               this.logger.error("BPR_WS_Statistics::composeSqlExpressionForCampaignObjects: campaign dbid is not delivered with object or problem with conversion");
               var1.append("0");
            }

            var1.append("' and ");
            if (ObjectRegistryEnum.OBJECT_CAMPAIGNCALLINGLIST == var2) {
               var1.append(this.translationData.getColumnNameByObjectType(ObjectRegistryEnum.OBJECT_CALLINGLIST));
               var1.append("='");
               var4 = this.getObjectDBIDByType(var3, "CALLING_LIST_DBID");
               if (null != var4) {
                  var1.append(var4);
               } else {
                  this.logger.error("BPR_WS_Statistics::composeSqlExpressionForCampaignObjects: calling list dbid is not delivered with object or problem with conversion");
                  var1.append("0");
               }
            } else {
               var4 = this.getObjectDBIDByType(var3, "GROUP_AGENTS_DBID");
               if (null != var4) {
                  var1.append(this.translationData.getColumnNameByObjectType(ObjectRegistryEnum.OBJECT_GROUPAGENTS));
                  var1.append("='");
                  var1.append(var4);
               } else {
                  var4 = this.getObjectDBIDByType(var3, "GROUP_PLACES_DBID");
                  var1.append(this.translationData.getColumnNameByObjectType(ObjectRegistryEnum.OBJECT_GROUPPLACES));
                  var1.append("='");
                  if (null != var4) {
                     var1.append(var4);
                  } else {
                     this.logger.error("BPR_WS_Statistics::composeSqlExpressionForCampaignObjects: agent and place group dbids are not delivered with object or problem with conversion");
                     var1.append("0");
                  }
               }
            }

         }
      } else {
         this.logger.error("BPR_WS_Statistics::composeSqlExpressionForCampaignObjects: buffer and/or registryObject are empty");
      }
   }
*/
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
         this.logger.error("BPStatistics::getValue: dbService isn't available");
         return null;
      } 

      this.logger.debug("BPR_WS_Statistics::getValue: SQL expression: " + this.sqlExpression);
      Double var1 = this.wsService.call(this.uriComplete);
      if (null != var1) {
         this.logger.debug("BPR_WS_Statistics::getValue: returned value: " + var1.toString());
      } else {
         this.closePreparedStatement();
         this.logger.debug("BPR_WS_Statistics::getValue: returned value is 'null'");
      }

         return var1;
   }

   protected void onClose() {
      this.logger.debug("BPR_WS_Statistics::onClose");
      this.parent.removeStatistics(this);
      this.closePreparedStatement();
      this.wsService = null;
   }

   protected void reset() {
   }

   protected Object takeSample() {
      return null;
   }

   private void closePreparedStatement() {
      if (null != this.preparedStatement) {
         try {
            this.preparedStatement.close();
         } catch (SQLException var2) {
            this.logger.debug("BPR_WS_Statistics::onClose: can't close prepared statement: ", var2);
         }

         this.preparedStatement = null;
      }

   }
}
