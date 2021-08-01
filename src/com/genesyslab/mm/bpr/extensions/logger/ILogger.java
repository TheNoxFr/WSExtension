/* Decompiler 0ms, total 89ms, lines 36 */
package com.genesyslab.mm.bpr.extensions.logger;

public interface ILogger {
   Level getLevel();

   void debug(Object var1);

   void debug(Object var1, Throwable var2);

   void debug(String var1, Object[] var2);

   void info(Object var1);

   void info(Object var1, Throwable var2);

   void info(String var1, Object[] var2);

   void warning(Object var1);

   void warning(Object var1, Throwable var2);

   void warning(String var1, Object[] var2);

   void error(Object var1);

   void error(Object var1, Throwable var2);

   void error(String var1, Object[] var2);

   void fatal(Object var1);

   void fatal(Object var1, Throwable var2);

   void fatal(String var1, Object[] var2);
}
