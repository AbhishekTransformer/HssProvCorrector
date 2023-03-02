package com.tcs.hss.client;

public class PrivateProfile {
    private String privateUserId;
    private String msisdn;

    public PrivateProfile(String privateUserId, String msisdn) {
        this.privateUserId = privateUserId;
        this.msisdn = msisdn;
    }

    public String getPrivateUserId() {
        return privateUserId;
    }

    public void setPrivateUserId(String privateUserId) {
        this.privateUserId = privateUserId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
}
