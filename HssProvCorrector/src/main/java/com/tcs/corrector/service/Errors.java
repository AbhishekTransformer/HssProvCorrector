package com.tcs.corrector.service;

public enum Errors {
	DNSERROR("DNS public record not found"), HSSMISSING("Public Id Values Missing from HSS"),
	ASMISSING("Extra Public Id Values in HSS"), HSSMISSINGIMSI("IMSI Values Missing from HSS"),
	HSSEXTRAIMSI("Extra IMSI Values in HSS"), HSSZEROPUBLICIDS("No public Id found on HSS"),
	ASZEROPUBLICIDS("No publicId on AS but Public Id Values found in HSS"), INCORRECTMSISDN("MSISDN could be corrupted");

	private String str;

	Errors(String str) {
		this.str = str;
	}

	public String str() {
		return str;
	}

}
