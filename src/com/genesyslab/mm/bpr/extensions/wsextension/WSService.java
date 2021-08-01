/* Decompiler 15ms, total 106ms, lines 145 */
package com.genesyslab.mm.bpr.extensions.wsextension;

import com.genesyslab.mm.bpr.extensions.logger.ILogger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import org.json.JSONObject;

public class WSService implements IWSService { //}, Runnable {
   protected ILogger logger;
   protected Properties properties;
   protected HttpURLConnection connection;
   protected String wsurl;
   private Thread connectionThread = null;
   private boolean continueReconnection = true;

   WSService(Properties properties, ILogger logger) {
      this.properties = properties;
      this.logger = logger;
   }

   public void init() {
      this.wsurl = this.properties.getProperty("ws-url");
      //this.openConnection();
   }

   public Double call(String uri) {

      Double resultat = 0.0;

      try {
         URL url = new URL(wsurl + uri);
         this.connection = (HttpURLConnection) url.openConnection();
         this.connection.setRequestMethod("GET");
         this.connection.setRequestProperty("Content-Type", "application/json; utf-8");
         this.connection.setRequestProperty("Accept", "application/json");
         this.connection.setDoOutput(false);
         this.connection.connect();

         InputStream in = new BufferedInputStream(connection.getInputStream());
         BufferedReader reader = new BufferedReader(new InputStreamReader(in));
         StringBuilder result = new StringBuilder();
         String line;
         while((line = reader.readLine()) != null) {
            result.append(line);
         }

         JSONObject retObj = new JSONObject(result.toString());

         resultat = Double.parseDouble(retObj.get("value").toString());

         this.connection.disconnect();

      } catch (Exception ex)
      {}

      return resultat;
   }
/*
   public void shutdown() {
      this.logger.debug("WSServiceAbstract::shutdown");
      synchronized(this) {
         if (null != this.connectionThread) {
            this.continueReconnection = false;

            try {
               this.connectionThread.interrupt();
               this.connectionThread.join(10000L);
            } catch (Exception var4) {
            }
         }
      }

      this.closeConnection();
      this.logger = null;
   }
*/
/*
   protected void closeConnection() {
      if (null != this.connection) {
         try {
            this.connection.disconnect();
            this.logger.debug("DBServiceAbstract::closeConnection: connection closed");
         } catch (Exception var2) {
            this.logger.error("DBServiceAbstract::closeConnection: can't close connection to DB ", var2);
         }

         this.connection = null;
      }

   }
*/
/*
   public synchronized void openConnection() {
      if (null == this.connectionThread) {
         String var1 = Thread.currentThread().getName() + " DBP";
         this.connectionThread = new Thread(this, var1);
         this.connectionThread.start();
      }
   }
*/
/*
   public void run() {
      int var1;
      try {
         var1 = Integer.parseInt(this.properties.getProperty("reconnection-timeout"));
      } catch (Exception var8) {
         var1 = 10000;
      }

      this.closeConnection();
      this.wsurl = this.properties.getProperty("ws-url");
      Properties var3 = (Properties)this.properties.clone();
      var3.remove("password");

      while(this.continueReconnection) {
         try {
            try {
               if (null != this.connection) {
                  this.logger.debug("DBServiceAbstract::openConnection: connected successfully to sql server: ");
                  this.logger.debug("\tJDBC parameters: " + var3.toString());
                  //this.connection.setReadOnly(true);
                  break;
               }
            } catch (Exception var9) {
               this.logger.debug("DBServiceAbstract::openConnection: can't set to read-only");
               break;
            }
         } catch (Exception var10) {
            this.logger.error("DBServiceAbstract::openConnection: can't connect to sql server: ");
            this.logger.error("\tJDBC parameters: " + var3.toString());
            this.logger.error("\texception:", var10);
         }

         try {
            Thread.sleep((long)var1);
         } catch (InterruptedException var7) {
         }
      }

      synchronized(this) {
         this.connectionThread = null;
      }
   }
*/
}
