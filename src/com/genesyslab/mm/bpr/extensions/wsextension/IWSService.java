/* Decompiler 0ms, total 90ms, lines 14 */
package com.genesyslab.mm.bpr.extensions.wsextension;

public interface IWSService {
   void init();

//   PreparedStatement prepareStatement(String var1);

//   Double executeStatement(PreparedStatement var1);

   Double call(String jsonBody);

   void shutdown();
}
