package com.tcs.hss.client;

import com.tcs.parser.utils.ConfigUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;

public class LoginRequest extends BaseRequest {
    private static Logger LOGGER = LogManager.getLogger(LoginRequest.class);
    private String sessionId;

    public LoginRequest() {
        super(ConfigUtils.HssLoginRequest, Cai3gAction.LOGIN.str());
    }

    @Override
    public void parseResponse() {
        if (null != this.responseStream) {
            try {
                sessionId = HSSParserUtils.getLoginSessionId(this.responseStream);
            } catch (FileNotFoundException e) {
                LOGGER.error(ConfigUtils.getStackTraceString(e));
            } catch (XMLStreamException e) {
                LOGGER.error(ConfigUtils.getStackTraceString(e));
            }
        }
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
