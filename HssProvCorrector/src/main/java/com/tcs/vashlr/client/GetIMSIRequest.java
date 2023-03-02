package com.tcs.vashlr.client;

import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.DevHelper;
import com.tcs.parser.utils.Environment;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class GetIMSIRequest extends BaseRequest {
	private static Logger LOGGER = LogManager.getLogger(GetIMSIRequest.class);

	public GetIMSIRequest(String msisdn) {
		super(msisdn);
	}

	@Override
	public VASHLRProfileResponse executeRequest() {
		String vasHLRUri;
		OkHttpClient client = getClient();

		if (Environment.PROD.str().equalsIgnoreCase(ConfigUtils.environment))
			vasHLRUri = this.getProdVASHLRUri();
		else {
			vasHLRUri = this.getTestVASHLRUri();
			client = DevHelper.getUnsafeOkHttpClient();
		}

		Request req = new Request.Builder().url(String.format("%s?method=lookupByMsisdn&msisdn=%s&userName=%s",
				vasHLRUri, this.getMsisdn(), ConfigUtils.VASHLRUser)).method("GET", null).build();
		LOGGER.info("GetIMSIRequest:req "+req);
		

		VASHLRProfileResponse profile = null;
		try {
			LOGGER.info("GetIMSIRequest:vasHLRUri "+vasHLRUri);
			Response res = client.newCall(req).execute();
			LOGGER.info("GetIMSIRequest:res " + res+"vasHLRUri "+vasHLRUri);
			String body = res.body().string();
			profile = objectMapper.readValue(body, VASHLRProfileResponse.class);
			LOGGER.info("IMSI received for msisdn " + this.getMsisdn() + " is "
					+ profile.getBody().getLookupByMsisdnResponse().getLookupByMsisdnReturn().getImsi());
		} catch (IOException e) {
			LOGGER.info("Response from vasHLR : "+e.getMessage());
			LOGGER.error("Exception occured : " + e.getStackTrace());
		}
		return profile;
	}

}
