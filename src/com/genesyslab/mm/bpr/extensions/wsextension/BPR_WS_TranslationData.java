/* Decompiler 19ms, total 114ms, lines 217 */
package com.genesyslab.mm.bpr.extensions.wsextension;

class BPR_WS_TranslationData implements Cloneable {
   private String[] columnNames = new String[BPR_WS_TranslationData.ObjectRegistryEnum.getMaxIndex() + 1];
   private BPR_WS_TranslationData.DataTypeEnum[] dataTypes = new BPR_WS_TranslationData.DataTypeEnum[BPR_WS_TranslationData.ObjectRegistryEnum.getMaxIndex() + 1];
   private boolean useTenant = true;

   public BPR_WS_TranslationData() {
      this.initData();
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   private void initData() {
      int var1;
      for(var1 = 0; var1 < this.columnNames.length; ++var1) {
         this.columnNames[var1] = "dbid";
      }

      this.columnNames[BPR_WS_TranslationData.ObjectRegistryEnum.OBJECT_CAMPAIGNGROUP.index()] = "name";
      this.columnNames[BPR_WS_TranslationData.ObjectRegistryEnum.OBJECT_CAMPAIGNCALLINGLIST.index()] = "name";
      this.columnNames[BPR_WS_TranslationData.ObjectRegistryEnum.OBJECT_TENANT.index()] = "tenant_id";
      this.columnNames[BPR_WS_TranslationData.ObjectRegistryEnum.OBJECT_STAGING_AREA.index()] = "queue";

      for(var1 = 0; var1 < this.dataTypes.length; ++var1) {
         this.dataTypes[var1] = BPR_WS_TranslationData.DataTypeEnum.dbid;
      }

      this.dataTypes[BPR_WS_TranslationData.ObjectRegistryEnum.OBJECT_CAMPAIGNGROUP.index()] = BPR_WS_TranslationData.DataTypeEnum.name;
      this.dataTypes[BPR_WS_TranslationData.ObjectRegistryEnum.OBJECT_CAMPAIGNCALLINGLIST.index()] = BPR_WS_TranslationData.DataTypeEnum.name;
      this.dataTypes[BPR_WS_TranslationData.ObjectRegistryEnum.OBJECT_TENANT.index()] = BPR_WS_TranslationData.DataTypeEnum.dbid;
      this.dataTypes[BPR_WS_TranslationData.ObjectRegistryEnum.OBJECT_STAGING_AREA.index()] = BPR_WS_TranslationData.DataTypeEnum.name;
   }

   public void setUseTenant(boolean var1) {
      this.useTenant = var1;
   }

   public void setUseTenant(String var1) {
      if (null != var1) {
         String var2 = var1.trim();
         if (0 < var2.length()) {
            this.useTenant = Boolean.parseBoolean(var2);
         }
      }

   }

   public boolean isUseTenant() {
      return this.useTenant;
   }

   public String getColumnNameByObjectType(BPR_WS_TranslationData.ObjectRegistryEnum var1) {
      return this.columnNames[var1.index()];
   }

   public void setColumnNameByObjectType(String var1, BPR_WS_TranslationData.ObjectRegistryEnum var2) {
      if (null != var1) {
         String var3 = var1.trim();
         if (0 < var3.length()) {
            this.columnNames[var2.index()] = var3;
         }
      }

   }

   public BPR_WS_TranslationData.DataTypeEnum getDataTypeByObjectType(BPR_WS_TranslationData.ObjectRegistryEnum var1) {
      return this.dataTypes[var1.index()];
   }

   public void setDataTypeByObjectType(String var1, BPR_WS_TranslationData.ObjectRegistryEnum var2) {
      if (null != var1) {
         String var3 = var1.trim();
         if (0 < var3.length()) {
            this.dataTypes[var2.index()] = BPR_WS_TranslationData.DataTypeEnum.getFromString(var3);
         }
      }

   }

   public void setDataTypeByObjectType(BPR_WS_TranslationData.DataTypeEnum var1, BPR_WS_TranslationData.ObjectRegistryEnum var2) {
      this.dataTypes[var2.index()] = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(1024);
      var1.append("\n\tStart TranslationData parameters");

      for(int var2 = 0; var2 <= BPR_WS_TranslationData.ObjectRegistryEnum.getMaxIndex(); ++var2) {
         var1.append("\n\t\t");
         var1.append(BPR_WS_TranslationData.ObjectRegistryEnum.getByIndex(var2).toString());
         var1.append(":\t");
         var1.append(BPR_WS_TranslationData.ObjectRegistryEnum.getByIndex(var2).columnOptionName());
         var1.append(" = \"");
         var1.append(this.columnNames[var2]);
         var1.append("\";\t");
         var1.append(BPR_WS_TranslationData.ObjectRegistryEnum.getByIndex(var2).dataOptionName());
         var1.append(" = \"");
         var1.append(this.dataTypes[var2].toString());
         var1.append("\";");
      }

      var1.append("\n\t\t");
      var1.append("use-tenant");
      var1.append(" = ");
      var1.append(this.useTenant);
      var1.append("\n\tEnd TranslationData parameters");
      return var1.toString();
   }

   static enum ObjectRegistryEnum {
      OBJECT_AGENT(0, "agent", "Agent"),
      OBJECT_PLACE(1, "place", "Place"),
      OBJECT_GROUPAGENTS(2, "agent-group", "GroupAgents"),
      OBJECT_GROUPPLACES(3, "place-group", "GroupPlaces"),
      OBJECT_ROUTEPOINT(4, "routepoint", "RoutePoint"),
      OBJECT_QUEUE(5, "queue", "Queue"),
      OBJECT_GROUPQUEUES(6, "queue-group", "GroupQueues"),
      OBJECT_SWITCH(7, "switch", "Switch"),
      OBJECT_REGDN(8, "regdn", "RegDN"),
      OBJECT_CAMPAIGN(9, "campaign", "Campaign"),
      OBJECT_CAMPAIGNGROUP(10, "campaign-group", "CampaignGroup"),
      OBJECT_CALLINGLIST(11, "calling-list", "CallingList"),
      OBJECT_CAMPAIGNCALLINGLIST(12, "campaign-calling-list", "CampaignCallingList"),
      OBJECT_TENANT(13, "tenant", "Tenant"),
      OBJECT_STAGING_AREA(14, "staging-area", "StagingArea"),
      OBJECT_ROUTING_STRATEGY(15, "routing-strategy", "RoutingStrategy");

      private final int index;
      private final String toStringName;
      private final String columnOptionName;
      private final String dataOptionName;

      static BPR_WS_TranslationData.ObjectRegistryEnum getByIndex(int var0) {
         switch(var0) {
         case 0:
            return OBJECT_AGENT;
         case 1:
            return OBJECT_PLACE;
         case 2:
            return OBJECT_GROUPAGENTS;
         case 3:
            return OBJECT_GROUPPLACES;
         case 4:
            return OBJECT_ROUTEPOINT;
         case 5:
            return OBJECT_QUEUE;
         case 6:
            return OBJECT_GROUPQUEUES;
         case 7:
            return OBJECT_SWITCH;
         case 8:
            return OBJECT_REGDN;
         case 9:
            return OBJECT_CAMPAIGN;
         case 10:
            return OBJECT_CAMPAIGNGROUP;
         case 11:
            return OBJECT_CALLINGLIST;
         case 12:
            return OBJECT_CAMPAIGNCALLINGLIST;
         case 13:
            return OBJECT_TENANT;
         case 14:
            return OBJECT_STAGING_AREA;
         case 15:
            return OBJECT_ROUTING_STRATEGY;
         default:
            return OBJECT_TENANT;
         }
      }

      static int getMaxIndex() {
         return OBJECT_ROUTING_STRATEGY.index();
      }

      private ObjectRegistryEnum(int var3, String var4, String var5) {
         this.index = var3;
         this.toStringName = var5;
         this.columnOptionName = "column-name-" + var4;
         this.dataOptionName = "data-type-" + var4;
      }

      int index() {
         return this.index;
      }

      String columnOptionName() {
         return this.columnOptionName;
      }

      String dataOptionName() {
         return this.dataOptionName;
      }

      public String toString() {
         return this.toStringName;
      }
   }

   static enum DataTypeEnum {
      name,
      dbid;

      static BPR_WS_TranslationData.DataTypeEnum getFromString(String var0) {
         if (null == var0) {
            return dbid;
         } else {
            String var1 = var0.trim();
            return 0 == "name".compareToIgnoreCase(var1) ? name : dbid;
         }
      }
   }
}
