package com.dnasoftware.smartdnsproxy.Utils;

import retrofit.RestAdapter;
import retrofit.http.GET;

/**
 * Get's the user IP using telize.com service
 * Created by Matias Radzinski on 06/07/2014, 03:06 PM.
 */
public class IPUtils {
    final static String BASE_URL = "http://www.telize.com";

    public static String getCurrentIP(){
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL).build();

        Telize telize = adapter.create(Telize.class);
        final TelizeModel response = telize.getIP();

        return response.getIp();
    }


    protected interface Telize{
        @GET("/jsonip")
        TelizeModel getIP();
    }

    protected class TelizeModel{
        private String ip;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }
    }
}
