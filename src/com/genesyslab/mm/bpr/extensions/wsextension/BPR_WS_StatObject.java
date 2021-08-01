/* Decompiler 1ms, total 95ms, lines 18 */
package com.genesyslab.mm.bpr.extensions.wsextension;

import Genesys.ComLib.TKV.TKVList;
import com.genesys.statserver.statistics.StatObject;

public class BPR_WS_StatObject extends StatObject {
   private int tenantId;

   public BPR_WS_StatObject(int var1, int var2, String var3, String var4, TKVList var5) {
      super(var2, var3, var4, var5);
      this.tenantId = var1;
   }

   public int getTenantId() {
      return this.tenantId;
   }
}
