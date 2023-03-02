package com.tcs.parser.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class ConfigUtils {
	private static Logger LOGGER = LogManager.getLogger(ConfigUtils.class);
	public static final Properties prop = new Properties();
	public static final String configFilePath = "../conf/Configuration.properties";
	// public static final String configFilePath =
	// "C:\\harshita\\workspace\\HssProvCorrectorConf\\Configuration.properties";
	public static String hibernateCfgXmlPath;
	public static String excelFilePath;
	public static String ociPath /*= "C:\\Users\\1710042\\Desktop\\conf\\HssProvCorrector\\bin\\oci\\"*/;// for testing
	public static Integer schedulingHour;
	public static String asUserName = "proFixer";
	public static String asTestUserName = "proFixer";
	public static String asPassword = "fixer2015";
	public static String asTestPassword = "Fixer@2015";
	public static InputStream input = null;
	public static Long fixStartTime;
	public static String HssGetRequest /*="C:\\Users\\1710042\\Desktop\\conf\\HssProvCorrector\\bin\\hss\\getProfile.xml" */; // To
	// be
	// removed
	// in
	// prod
	public static String HssLogoutRequest /*="C:\\Users\\1710042\\Desktop\\conf\\HssProvCorrector\\bin\\hss\\logout.xml"*/;
	public static String HssLoginRequest /*="C:\\Users\\1710042\\Desktop\\conf\\HssProvCorrector\\bin\\hss\\login.xml"*/;
	public static String HssDeleteRequest = "";
	public static String HssGetRequestForTelURI /*="C:\\Users\\1710042\\Desktop\\conf\\HssProvCorrector\\bin\\hss\\getProfileTelURI.xml"*/;
	public static String environment = Environment.TEST.str(); // default for testing. remove afterwards
	public static String[] toEmailArr;
	public static String fromEmail;
	public static Long revalidatingWaitingTime;
	public static String indexName;
	public static String mailSubject;
	public static Integer noOfDays;

	public static String VASHLRUser = "scale-fix";
	public static boolean fix = false;
	public static String edaProdUrl;
	public static String edaTestUrl;
	
	public static List<Integer> storeIndexList=new ArrayList<>();;

	public static void loadPropertiesFile() {

		try {
			System.out.println("Started conf loading");
			String path="";
			if(System.getProperty("os.name").contains("Windows"))
				path = "C:\\Users\\1710042\\Desktop\\conf\\HssProvCorrector\\conf\\Configuration.properties";
			else
				path=ConfigUtils.configFilePath;
			input = new FileInputStream(path);
			try {
				ConfigUtils.prop.load(input);
			} catch (IOException e) {
				System.out.println("Error");
				LOGGER.error(ConfigUtils.getStackTraceString(e));
			}
			hibernateCfgXmlPath = ConfigUtils.prop.getProperty("hibernateCfgXmlPath");
			excelFilePath = ConfigUtils.prop.getProperty("excelFilePath");
			ociPath = ConfigUtils.prop.getProperty("ociPath");
			// schedulingHour=Integer.parseInt(ConfigUtils.prop.getProperty("schedulingHour"));
			HssLogoutRequest = ConfigUtils.prop.getProperty("HssLogoutRequest");
			HssGetRequest = ConfigUtils.prop.getProperty("HssGetRequest");
			HssLoginRequest = ConfigUtils.prop.getProperty("HssLoginRequest");
			HssDeleteRequest = ConfigUtils.prop.getProperty("HssDeleteRequest");
			HssGetRequestForTelURI = ConfigUtils.prop.getProperty("HssGetRequestForTelURI");

			environment = ConfigUtils.prop.getProperty("environment");
			String toEmailStrList = ConfigUtils.prop.getProperty("toEmailList");
			toEmailArr = toEmailStrList.split(",");
			System.out.println("toEmail " + toEmailArr);
			fromEmail = ConfigUtils.prop.getProperty("fromEmail");
			indexName = ConfigUtils.prop.getProperty("indexName");
			mailSubject = ConfigUtils.prop.getProperty("mailSubject");
			revalidatingWaitingTime = Long.parseLong(ConfigUtils.prop.getProperty("revalidatingWaitingTime"));
			noOfDays = Integer.parseInt(ConfigUtils.prop.getProperty("noOfDays"));
			VASHLRUser = ConfigUtils.prop.getProperty("VASHLRUser");
			
			edaProdUrl = ConfigUtils.prop.getProperty("edaprodurl");
			edaTestUrl = ConfigUtils.prop.getProperty("edatesturl");
			
			fix = Boolean.parseBoolean(ConfigUtils.prop.getProperty("fix"));
			asTestUserName = ConfigUtils.prop.getProperty("asUserName");
			asTestPassword = ConfigUtils.prop.getProperty("asTestPassword");
			LOGGER.info("-----------------Configurations correctly loaded");
			System.out.println("Configuration loaded:" + hibernateCfgXmlPath);

		} catch (Exception e) {
			LOGGER.error(ConfigUtils.getStackTraceString(e));
		}

	}

	public static String getStackTraceString(Exception e) {
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}

	public static Long getCurrentTimestamp() {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 0);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		try {
			return dateFormat.parse(dateFormat.format(cal.getTime())).getTime();
		} catch (ParseException e) {
			LOGGER.error(ConfigUtils.getStackTraceString(e));
		}
		return null;
	}

	public static String getCurrentDate() {
		final Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		return dateFormat.format(cal.getTime());
	}

	public static String getCurrentDateWithTimeStamp() {
		final Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss");
		return dateFormat.format(cal.getTime());
	}

	public static Long getYesterdayTimeStamp() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		try {
			return dateFormat.parse(dateFormat.format(yesterday())).getTime();
		} catch (ParseException e) {
			LOGGER.error(ConfigUtils.getStackTraceString(e));
		}
		return null;
	}

	private static Date yesterday() {
		final Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.HOUR, 24 * (-noOfDays)); // cal.add(Calendar.DATE, -1);
		cal.add(Calendar.DATE, -noOfDays);
		LOGGER.info("Time: "+cal.getTime());
		return cal.getTime();
	}

	public static List<Integer> getStoreIndexList() {
		return storeIndexList;
	}

	public static void setStoreIndexList(List<Integer> storeIndexList) {
		ConfigUtils.storeIndexList = storeIndexList;
	}
public static void clearList()
{
	LOGGER.info("Before clearing list: "+ storeIndexList);
	storeIndexList.clear();
	LOGGER.info("After clearing list: "+ storeIndexList);
}

}



