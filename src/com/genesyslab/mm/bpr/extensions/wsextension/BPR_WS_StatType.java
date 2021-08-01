/* Decompiler 5ms, total 92ms, lines 50 */
package com.genesyslab.mm.bpr.extensions.wsextension;

import com.genesys.statserver.statistics.StatCalculator;
import com.genesys.statserver.statistics.StatType;
import java.util.Hashtable;

public class BPR_WS_StatType extends StatType {
   private BPR_WS_Extension parent;
   private int[] supportedObjectTypes = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
   private String description = "BPR - WS Result.";

   public BPR_WS_StatType(BPR_WS_Extension var1) throws IllegalArgumentException {
      super("BPR WS Result");
      if (null == var1) {
         throw new IllegalArgumentException("BPR_WS_StatType::BPR_WS_StatType: parent can't be 'null'");
      } else {
         this.parent = var1;
      }
   }

   public StatCalculator getParent() {
      return this.parent;
   }

   public int[] getSupportedObjectTypes() {
      return this.supportedObjectTypes;
   }

   public void validateSpecificParams(Hashtable var1) throws IllegalArgumentException {
   }

   public String getDescription() {
      return this.description;
   }

   public boolean isValidObjectType(int var1) {
      for(int var2 = 0; var2 < this.supportedObjectTypes.length; ++var2) {
         if (this.supportedObjectTypes[var2] == var1) {
            return true;
         }
      }

      return false;
   }

   public void release() {
      this.parent = null;
   }
}
