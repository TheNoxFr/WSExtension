/* Decompiler 8ms, total 94ms, lines 113 */
package com.genesyslab.mm.bpr.extensions.logger;

public class Level {
   private int value;
   private String stringToPrint;
   private static final int OFF_INT = Integer.MAX_VALUE;
   private static final int FATAL_INT = 50000;
   private static final int ERROR_INT = 40000;
   private static final int WARN_INT = 30000;
   private static final int INFO_INT = 20000;
   private static final int DEBUG_INT = 10000;
   private static final int ALL_INT = Integer.MIN_VALUE;
   public static final Level OFF = new Level(Integer.MAX_VALUE, "Off");
   public static final Level FATAL = new Level(50000, "Fatal");
   public static final Level ERROR = new Level(40000, "Error");
   public static final Level WARN = new Level(30000, "Warning");
   public static final Level INFO = new Level(20000, "Info");
   public static final Level DEBUG = new Level(10000, "Debug");
   public static final Level ALL = new Level(Integer.MIN_VALUE, "All");

   protected Level(int var1, String var2) {
      this.value = var1;
      this.stringToPrint = var2;
   }

   public static Level toLevel(String var0) {
      return toLevel(var0, FATAL);
   }

   public static Level toLevel(int var0) {
      return toLevel(var0, FATAL);
   }

   public static Level toLevel(int var0, Level var1) {
      switch(var0) {
      case Integer.MIN_VALUE:
         return ALL;
      case 10000:
         return DEBUG;
      case 20000:
         return INFO;
      case 30000:
         return WARN;
      case 40000:
         return ERROR;
      case 50000:
         return FATAL;
      case Integer.MAX_VALUE:
         return OFF;
      default:
         return null != var1 ? var1 : FATAL;
      }
   }

   public static Level toLevel(String var0, Level var1) {
      if (OFF.toString().equalsIgnoreCase(var0)) {
         return OFF;
      } else if (FATAL.toString().equalsIgnoreCase(var0)) {
         return FATAL;
      } else if (!ERROR.toString().equalsIgnoreCase(var0) && !"standard".equalsIgnoreCase(var0)) {
         if (!WARN.toString().equalsIgnoreCase(var0) && !"interaction".equalsIgnoreCase(var0)) {
            if (!INFO.toString().equalsIgnoreCase(var0) && !"trace".equalsIgnoreCase(var0)) {
               if (DEBUG.toString().equalsIgnoreCase(var0)) {
                  return DEBUG;
               } else if (ALL.toString().equalsIgnoreCase(var0)) {
                  return ALL;
               } else {
                  return null != var1 ? var1 : FATAL;
               }
            } else {
               return INFO;
            }
         } else {
            return WARN;
         }
      } else {
         return ERROR;
      }
   }

   public String toString() {
      return this.stringToPrint;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof Level) {
         return this.value == ((Level)var1).value;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.value;
   }

   public boolean isGreaterOrEqual(Level var1) {
      if (null == var1) {
         return true;
      } else {
         return this.value >= var1.value;
      }
   }

   public boolean isLesser(Level var1) {
      if (null == var1) {
         return false;
      } else {
         return this.value < var1.value;
      }
   }
}
