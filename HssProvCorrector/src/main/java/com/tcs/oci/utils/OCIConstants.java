package com.tcs.oci.utils;

public class OCIConstants {
	public static final String loginCommand = "<?xml version="+"\"1.0\""+" encoding="+"\"UTF-8\""+"?>" +
			
	"<BroadsoftDocument protocol="+"\"OCI\""+" xmlns="+"\"C\"" +" xmlns:xsi="+"\"http://www.w3.org/2001/XMLSchema-instance\""+">" +
	
	  "<sessionId xmlns="+"\"\"" + ">14994130501580.7227116286783203</sessionId>"+
	
	   "<command xsi:type="+"\"AuthenticationRequest\"" +" xmlns="+"\"\""+"><userId>laege2bodil</userId></command></BroadsoftDocument>";
	
	
public static final String authCommand= "<?xml version="+"\"1.0\""+" encoding="+"\"UTF-8\""+"?>" +
			
	"<BroadsoftDocument protocol="+"\"OCI\""+" xmlns="+"\"C\"" +" xmlns:xsi="+"\"http://www.w3.org/2001/XMLSchema-instance\""+">" +
	
	  "<sessionId xmlns="+"\"\"" + ">14994130501580.7227116286783203</sessionId>"+
	
	   "<command xsi:type="+"\"LoginRequest14sp4\"" +" xmlns="+"\"\""+"><userId>laege2bodil</userId><signedPassword>abcdef</signedPassword></command></BroadsoftDocument>";
	
	
	

}
