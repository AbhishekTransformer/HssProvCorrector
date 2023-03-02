package com.tcs.oci.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/*File Name: TimeInputHandler.class
Author : TCS
Company : TCS
Description : Basic class template from which all scenario execution implementation classes inherit from.       
*/

public class TimeInputHandler {
    private static final Logger LOGGER = LogManager.getLogger("processLogger");

    public static String getCurrentTimeStamp() {
        Date dNow = new Date();

        SimpleDateFormat ft = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss");

        Random rand = new Random();
        int value = rand.nextInt(1000000000);
        return "minSession." + ft.format(dNow) + "BW" + Integer.toString(value);
    }

    public static void main(String args[]) {
        LOGGER.info(getCurrentTimeStamp());
        Date dNow = new Date();

        SimpleDateFormat ft = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss");

    LOGGER.info("minSession." + ft.format(dNow));
	}
}
