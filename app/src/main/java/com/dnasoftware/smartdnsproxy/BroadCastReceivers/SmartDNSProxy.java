package com.dnasoftware.smartdnsproxy.BroadCastReceivers;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 *
 * Created by Matias Radzinski on 05/07/2014, 06:54 PM.
 */
public interface SmartDNSProxy {
    @GET("/{AccountId}")
    SmartDNSProxyAPIResponse Update(@Path("AccountId") String AccountId);
}
