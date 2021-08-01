/* Decompiler 8ms, total 96ms, lines 60 */
package com.genesyslab.mm.bpr.extensions.wsextension;

import com.genesyslab.mm.bpr.extensions.logger.ILogger;
import java.util.Properties;

public class WSServiceFactory {
   protected WSServiceFactory() {
   }

   public static IWSService getWSService(Properties properties, ILogger logger) {
      if (null == properties) {
         if (null != logger) {
            logger.error("WSServiceFactory::getWSService: can't create WS connection object. List of properties is 'null'");
         }

         return null;
      } else {
         String url = properties.getProperty("ws-url");
         if (null == url) {
            if (null != logger) {
               logger.error("WSServiceFactory::getWSService: can't create WS connection object. URL isn't provided");
            }

            return null;
         } else {
            Object service = null;

            try {
               service = new WSService(properties, logger);
               if (null != logger) {
                  logger.debug("WSServiceFactory::getWSService: WS connection object created");
               }
            } catch (Exception ex) {
               if (null != logger) {
                  logger.fatal("WSServiceFactory::getWSService: can't create WS connection object", ex);
               } else {
                  ex.printStackTrace();
               }
            }

            return (IWSService)service;
         }
      }
   }
}
