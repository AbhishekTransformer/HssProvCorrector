package com.tcs.test;

import com.tcs.as.model.SCAEndpoint;
import com.tcs.oci.execution.ExecuteOCI;
import com.tcs.oci.execution.ExecuteUserSCAModifyRequest;
import com.tcs.parser.utils.ConfigUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SCAOCITester {

	public static void main(String args[]) {
		ExecuteOCI userFixer = null;
		try {
			String clusterId = "khk9dst11.ip.tdk.dk";
			String userId = "tdctcstest@vk666668.hvoip.dk";
			userFixer = new ExecuteOCI(clusterId);
			HashMap<String, String> paramList = userFixer.init();

			Map<String, String> rowMap = new HashMap<String, String>();
			rowMap.put(SCAEndpoint.DEVICE_LEVEL.str(), "System");
			rowMap.put(SCAEndpoint.DEVICE_NAME.str(), "Mobile");
			//rowMap.put(SCAEndpoint.DEVICE_TYPE.str(), subColList.item(2).getTextContent());
			rowMap.put(SCAEndpoint.LINE_PORT.str(), "20309305@vk666668.hvoip.dk");
			//rowMap.put(SCAEndpoint.SIP_CONTACT.str(), subColList.item(4).getTextContent());
			//rowMap.put(SCAEndpoint.PORT_NUMBER.str(), subColList.item(5).getTextContent());
			//rowMap.put(SCAEndpoint.DEVICE_SUPPORT_VDM.str(), subColList.item(6).getTextContent());
			rowMap.put(SCAEndpoint.IS_ACTIVE.str(), "true");
			rowMap.put(SCAEndpoint.ALLOW_ORIGINATION.str(), "true");
			rowMap.put(SCAEndpoint.ALLOW_TERMINATION.str(), "true");
			//rowMap.put(SCAEndpoint.MAC_ADDRESS.str(), subColList.item(10).getTextContent());

			ExecuteUserSCAModifyRequest scaReq = new ExecuteUserSCAModifyRequest();
			scaReq.deleteSCA(paramList, userFixer, userId, rowMap);
			scaReq.addSCA(paramList, userFixer, userId, rowMap);
			
			
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
