package com.tcs.test;

import com.tcs.oci.execution.ExecuteOCI;
import com.tcs.oci.execution.ExecuteUserAuthenticationModifyRequest;
import com.tcs.parser.utils.ConfigUtils;

import java.io.IOException;
import java.util.HashMap;

public class UserAuthOCITester {


    public static void main(String args[]) {
        ExecuteOCI userFixer = null;
        try {
            String clusterId = "khk9dst11.ip.tdk.dk";
            String userId = "tdctcstest@vk666668.hvoip.dk";
            userFixer = new ExecuteOCI(clusterId);
            HashMap<String, String> paramList = userFixer.init();

            ExecuteUserAuthenticationModifyRequest userAuthModify = new ExecuteUserAuthenticationModifyRequest();
            userAuthModify.setAuth(paramList, userFixer, userId);

        } catch (Exception e) {
            System.out.println("Exception :" + ConfigUtils.getStackTraceString(e));
        } finally {
            try {
                System.out.println("Disconnecting");
                userFixer.disconnect();
            } catch (IOException e) {
                System.out.println("-----Error in OCI init------" + ConfigUtils.getStackTraceString(e));
            }
        }
    }

}
