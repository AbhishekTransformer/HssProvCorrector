package com.tcs.generate.utils;


import com.mail.client.MailClient;
import com.mail.exception.InvalidMailConstructionException;
import com.mail.exception.MailSenderException;
import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.Environment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import java.util.ArrayList;
import java.util.List;

public class MailGenerator {
	private static Logger LOGGER = LogManager.getLogger(MailGenerator.class);

	public static void sendMail(boolean isAttachmentEligible, String subject, String fileDataSource) {
		LOGGER.info("Sending Mail");
		String fromEmail = ConfigUtils.fromEmail;
		List<String> toEmail = new ArrayList<String>();
		for (int i = 0; i < ConfigUtils.toEmailArr.length; i++) {
			toEmail.add(ConfigUtils.toEmailArr[i]);
		}
		// String subject= "HSS Provisoning Error List";
		String textBody = "HSS Provisoning Error List";
		String fileName = "FailedProvisioningData" + ConfigUtils.getCurrentDate() + ".xls";
		LOGGER.info("fileName: " + fileName);
//		testing: ConsideredList    previously used: FailedProvisioningData
		String environment = "";
		if (ConfigUtils.environment.equalsIgnoreCase(Environment.PROD.str()))
			environment = "in " + Environment.PROD.str() + " environment";
		else
			environment = "in " + Environment.TEST.str() + " environment";
		MailClient mailClient = new MailClient();
		LOGGER.info("fileDataSource " + fileDataSource);
		try {
			DataSource source = new FileDataSource(fileDataSource/* ConfigUtils.excelFilePath */);
			LOGGER.info("source " + source);
			if (isAttachmentEligible) {
//				textBody = "Failure found today in user HSS provisioning requests " + environment;
//				mailClient.sendEmail(fromEmail, toEmail, subject, textBody);
				textBody = "PFA user HSS provisioning error list fixes " + environment;
				mailClient.sendAttachEmail(fromEmail, toEmail, subject, textBody, source, fileName);
				
			} else {
				textBody = "No Failure found today in user HSS provisioning requests " + environment;
				mailClient.sendEmail(fromEmail, toEmail, subject, textBody);
			}
		} catch (InvalidMailConstructionException | MailSenderException e) {
			LOGGER.error(ConfigUtils.getStackTraceString(e));
		}
	}
}
