package com.tcs.vashlr.client;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;


public abstract class BaseRequest {

	public static final XmlMapper objectMapper = new XmlMapper();
	private static final OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(60, TimeUnit.SECONDS).build();
	//private final String testVASHLRUri = "https://tflex.tdctech.dk/vashlr-ws/Vashlr";
	// suggested by Ritesh for Test
	private final String testVASHLRUri = "https://t-mobilvas-dmz.tdctech.dk/vashlr-ws/Vashlr";
	private final String prodVASHLRUri = "https://www.tdctech.dk/vashlr-ws/Vashlr";
	private String msisdn;

	public BaseRequest(String msisdn) {
		super();
		this.msisdn = msisdn;
	}

	public abstract VASHLRProfileResponse executeRequest();

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getTestVASHLRUri() {
		return testVASHLRUri;
	}

	public String getProdVASHLRUri() {
		return prodVASHLRUri;
	}

	public static OkHttpClient getClient() {
		return client;
	}

}