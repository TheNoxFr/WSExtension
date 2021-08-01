/* Decompiler 0ms, total 90ms, lines 14 */
package com.genesyslab.mm.bpr.extensions.wsextension;

public interface IWSService {
   void init();

   Double call(String jsonBody);

   //void shutdown();
}
