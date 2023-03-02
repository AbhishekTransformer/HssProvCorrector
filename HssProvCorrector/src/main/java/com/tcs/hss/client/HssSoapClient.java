package com.tcs.hss.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HssSoapClient {
    private static Logger LOGGER = LogManager.getLogger(HssSoapClient.class);

    public HssProfile getHssProfile(String associationId) {
        //optimize this by login and logout only once for all the users -- TODO
    	try
    	{
	        LoginRequest login = new LoginRequest();
	        login.execute();
    	    
        
	        String sessionId = login.getSessionId();
	        LOGGER.info(" Session ID : " + sessionId);
	        if (null != sessionId) {
	            GetHssRequest getHss = new GetHssRequest(sessionId, associationId);
	            getHss.execute();
	            HssProfile userHssProfile = getHss.getUserHssProfile();
	            LOGGER.info(" userHssProfile public ID : " + userHssProfile.getPublicIdList());
	
	            LogoutRequest logout = new LogoutRequest(sessionId);
	            logout.execute();
	            return userHssProfile;
	        }
    	}
    	catch(Exception e)
    	{
    		LOGGER.error(" Exception in getting user hss profile " + e);
    	}
        return null;
    }

    public String getUsernameFromTelURI(String telUri) {
        LoginRequest login = new LoginRequest();
        login.execute();
        String sessionId = login.getSessionId();
        LOGGER.info(" Session ID : " + sessionId);
        LOGGER.info(" telUri : " + telUri);
        if (null != sessionId) {
            try {
                GetHssRequestFromTelURI getHssRequestFromTelURI = new GetHssRequestFromTelURI(sessionId, telUri);
                getHssRequestFromTelURI.execute();
                LOGGER.info("User found for " + telUri + " : " + getHssRequestFromTelURI.getUsername());
                return getHssRequestFromTelURI.getUsername() == null ? "No User Found" : getHssRequestFromTelURI.getUsername();
            } catch (Exception e) {
                LOGGER.error(" Exception in getting teluri user hss profile " + telUri);
            } finally {
                LogoutRequest logout = new LogoutRequest(sessionId);
                logout.execute();
            }
        }
        return "No User Found";
    }

    public void deleteHssProfile(String associationId) {
        //optimize this by login and logout only once for all the users -- TODO
        LoginRequest login = new LoginRequest();
        login.execute();
        String sessionId = login.getSessionId();
        LOGGER.info(" Session ID : " + sessionId);
        if (null != sessionId) {
            try {
                DeleteHssRequest deleteRequest = new DeleteHssRequest(sessionId, associationId);
                deleteRequest.execute();
            } catch (Exception e) {
                LOGGER.error(" Exception in user hss profile deletion " + associationId);
            } finally {
                LogoutRequest logout = new LogoutRequest(sessionId);
                logout.execute();
            }
        }
    }

    public static void main(String args[]) {
        System.out.println("Startting");

        HssSoapClient hs = new HssSoapClient();
        hs.getUsernameFromTelURI("tel:+4542158438");
    }
}
