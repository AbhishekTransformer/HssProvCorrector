package com.tcs.vashlr.client;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;


class barrings {
	private String barrings;

	public String getBarrings() {
		return barrings;
	}

	public void setBarrings(String barrings) {
		this.barrings = barrings;
	}

}

class packages {
	@JacksonXmlProperty(localName = "packages")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<String> packages;

	public List<String> getPackages() {
		return packages;
	}

	public void setPackages(List<String> packages) {
		this.packages = packages;
	}

}

class lookupByMsisdnReturn {
	private String alias;
	private barrings barrings;
	private String imsi;
	private String msisdn;
	private packages packages;
	private int serviceProviderCode;
	private String state;
	private String subscriptionType;
//	private String ns1;
	private String text;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public barrings getBarrings() {
		return barrings;
	}

	public void setBarrings(barrings barrings) {
		this.barrings = barrings;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public packages getPackages() {
		return packages;
	}

	public void setPackages(packages packages) {
		this.packages = packages;
	}

	public int getServiceProviderCode() {
		return serviceProviderCode;
	}

	public void setServiceProviderCode(int serviceProviderCode) {
		this.serviceProviderCode = serviceProviderCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSubscriptionType() {
		return subscriptionType;
	}

	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType = subscriptionType;
	}


	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "lookupByMsisdnReturn [alias=" + alias + ", imsi=" + imsi + ", msisdn=" + msisdn
				+ ", serviceProviderCode=" + serviceProviderCode + ", state=" + state + ", subscriptionType="
				+ subscriptionType + "]";
	}
	
	

}

class lookupByMsisdnResponse {
	private lookupByMsisdnReturn lookupByMsisdnReturn;
	private Object xmlns;
	private String text;

	public lookupByMsisdnReturn getLookupByMsisdnReturn() {
		return lookupByMsisdnReturn;
	}

	public void setLookupByMsisdnReturn(lookupByMsisdnReturn lookupByMsisdnReturn) {
		this.lookupByMsisdnReturn = lookupByMsisdnReturn;
	}

	public Object getXmlns() {
		return xmlns;
	}

	public void setXmlns(Object xmlns) {
		this.xmlns = xmlns;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}

class Body {
	private lookupByMsisdnResponse lookupByMsisdnResponse;

	public lookupByMsisdnResponse getLookupByMsisdnResponse() {
		return lookupByMsisdnResponse;
	}

	public void setLookupByMsisdnResponse(lookupByMsisdnResponse lookupByMsisdnResponse) {
		this.lookupByMsisdnResponse = lookupByMsisdnResponse;
	}

}

@JacksonXmlRootElement(localName = "Envelope")
public class VASHLRProfileResponse {
	@JacksonXmlProperty(localName = "Body")
	private Body body;
	private String soapenv;
	private String xsd;
	private String xsi;
	private String text;

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public String getSoapenv() {
		return soapenv;
	}

	public void setSoapenv(String soapenv) {
		this.soapenv = soapenv;
	}

	public String getXsd() {
		return xsd;
	}

	public void setXsd(String xsd) {
		this.xsd = xsd;
	}

	public String getXsi() {
		return xsi;
	}

	public void setXsi(String xsi) {
		this.xsi = xsi;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
