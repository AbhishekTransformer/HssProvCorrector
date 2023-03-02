package com.tcs.test;

import com.tcs.as.model.AlternateNumberEntry;
import com.tcs.oci.execution.ExecuteOCI;
import com.tcs.oci.execution.ExecuteUserAlternateNumbersModifyRequest;
import com.tcs.parser.utils.ConfigUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlternateNumberOCITester {

	public static void main(String args[]) {
		ExecuteOCI userFixer = null;
		try {
			String clusterId = "khk9dst11.ip.tdk.dk";
			String userId = "tdctcstest@vk666668.hvoip.dk";
			userFixer = new ExecuteOCI(clusterId);
			HashMap<String, String> paramList = userFixer.init();


			ExecuteUserAlternateNumbersModifyRequest altNum = new ExecuteUserAlternateNumbersModifyRequest();

			List<AlternateNumberEntry> altNumList = new ArrayList<AlternateNumberEntry>();
			AlternateNumberEntry entry1 = new AlternateNumberEntry();
			entry1.setRingPattern("Normal");
			entry1.setPhoneNumber("20309305");
			//entry1.setDescription("Dummy");
			//entry1.setExtension("256");
			altNumList.add(entry1);
				/*AlternateNumberEntry entry2 = new AlternateNumberEntry();
				entry2.setRingPattern("Normal");
				entry2.setPhoneNumber("10401001");
				altNumList.add(entry2);*/
			String distinctiveRing = "true";
			System.out.println(altNumList.size());
			List<AlternateNumberEntry> tempList = new ArrayList<AlternateNumberEntry>();
			;
				for(AlternateNumberEntry alt:altNumList){
					tempList.add(alt);
				}
				altNum.deleteAltNum(paramList, userFixer, userId,tempList);
				System.out.println(altNumList.size());
				for(AlternateNumberEntry alt:altNumList){
					tempList.add(alt);
				}
				altNum.addAltNum(paramList, userFixer, userId, distinctiveRing, tempList); 
			
		}catch(Exception e ){
			
		}finally {
			try {System.out.println("Disconnecting");
				userFixer.disconnect();
			} catch (IOException e) { 
				System.out.println("-----Error in OCI init------"+ConfigUtils.getStackTraceString(e));		
			}
		}
	}
}
