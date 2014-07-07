package com.dnasoftware.smartdnsproxy.BroadCastReceivers.WiFi;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Smart DNS Proxy Interface used by Retrofit
 * Created by Matias Radzinski on 05/07/2014, 06:54 PM.
 */
public interface SmartDNSProxy {
    @GET("/{AccountId}")
    SmartDNSProxyAPIModel Update(@Path("AccountId") String AccountId);
}
