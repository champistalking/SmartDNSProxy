package com.dnasoftware.smartdnsproxy.BroadCastReceivers.WiFi;


/**
 * Smart DNS Proxy Model used by Retrofit
 * Created by Matias Radzinski on 05/07/2014, 06:55 PM.
 */
public class SmartDNSProxyAPIModel {
    String Message;
    int Status;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
