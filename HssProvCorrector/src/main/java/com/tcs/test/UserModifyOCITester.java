package com.tcs.test;

import com.tcs.as.model.AccessDeviceEndpoint;
import com.tcs.oci.execution.ExecuteOCI;
import com.tcs.oci.execution.ExecuteUserModifyRequest;
import com.tcs.parser.utils.ConfigUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserModifyOCITester {


	public static void main(String args[]) {
		ExecuteOCI userFixer = null;
		try {
			String clusterId = "khk9dst11.ip.tdk.dk";
			String userId = "tdctcstest@vk666668.hvoip.dk";
			userFixer = new ExecuteOCI(clusterId);
			HashMap<String, String> paramList = userFixer.init();
			/*String phoneNumber="19412001";
			AccessDeviceEndpoint accessDeviceEndpoint= new AccessDeviceEndpoint();
			AccessDevice accessDevice = new AccessDevice();
			accessDevice.setDeviceLevel("System");
			accessDevice.setDeviceName("Mobile");
			accessDeviceEndpoint.setAccessDevice(accessDevice);
			String linePort ="+4520309305@vk666668.hvoip.dk";
			accessDeviceEndpoint.setLinePort(linePort);*/

			String phoneNumber = null;
			AccessDeviceEndpoint accessDeviceEndpoint= new AccessDeviceEndpoint();
			String linePort =null;
			accessDeviceEndpoint.setLinePort(linePort);
			
			List<String> SIPAliasList = new ArrayList<String>();
			SIPAliasList.add("4520309305@vk666668.hvoip.dk");
			//SIPAliasList.add("4512345678@vk666668.hvoip.dk");
			System.out.println("Size "+SIPAliasList.size());
			
			ExecuteUserModifyRequest userModify= new ExecuteUserModifyRequest();
			List<String> tempSipAliasList = new ArrayList<String> ();
			if(null !=SIPAliasList && SIPAliasList.size()!=0){
				for(String sipAlias :SIPAliasList ){
				tempSipAliasList.add(sipAlias);
				}
			}
			userModify.deleteUserModify(paramList, userFixer, userId,phoneNumber,accessDeviceEndpoint,tempSipAliasList);	
			List<String> tempSipAliasList2 = new ArrayList<String> ();
			if(null !=SIPAliasList && SIPAliasList.size()!=0){
				for(String sipAlias :SIPAliasList ){
				tempSipAliasList2.add(sipAlias);
				}
			}
			userModify.addUserModify(paramList, userFixer, userId,phoneNumber,accessDeviceEndpoint,tempSipAliasList2); 			
			
		}catch(Exception e ){
			System.out.println("Exception :"+ConfigUtils.getStackTraceString(e));
		}finally {
			try {System.out.println("Disconnecting");
				userFixer.disconnect();
			} catch (IOException e) { 
				System.out.println("-----Error in OCI init------"+ConfigUtils.getStackTraceString(e));		
			}
		}
	}

}
