package com.tcs.parser.utils;

public enum EmConstants {

    PROCESS_FAILED("failed"),
    PROCESS_SUCCESS("succeeded"),
    SUB_PROCESS_FAILED("userFailure"),
    SUB_PROCESS_IO_FAILED("ioFailure"),
    SUB_PROCESS_SUCCESS("success"),
    USER_ADD_REQUEST("UserAddRequest17sp4"),
    USER_ALTERNATE_NUMBERS_MODIFY_REQUEST("UserAlternateNumbersModifyRequest"),
    USER_MODIFY_REQUEST("UserModifyRequest17sp4"),
    USERSCA_ADD_ENDPOINT_REQUEST("UserSharedCallAppearanceAddEndpointRequest14sp2"),
    USERSCA_DELETE_ENDPOINTLIST_REQUEST("UserSharedCallAppearanceDeleteEndpointListRequest14"),
    USER_AUTHENTICATION_MODIFY_REQUEST("UserAuthenticationModifyRequest	"),
    CREATE_HSS_SUBSCRIBER("createHssSubscriber"),
    GET_IMSI("lookupByMsisdn"),
    SET_VHSS_SUBSCRIBER("setHssSubscriber"),
    CREATE_DNS("createEnumDns"),
    DELETE_DNS("deleteEnumDns"),
    SCALE("scale");

    private String str;

    EmConstants(String str) {
        this.str = str;
    }

    public String str() {
        return str;
    }
}
