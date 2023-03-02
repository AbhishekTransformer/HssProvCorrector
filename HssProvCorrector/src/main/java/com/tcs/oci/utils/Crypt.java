package com.tcs.oci.utils;

/*File Name: ConfigUtils.class
 Author : TCS
 Company : TCS
 Description : Used to get the MD5 hash values from authentication details.

 */

import com.tcs.parser.utils.ConfigUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Crypt {
	private static Logger LOGGER = LogManager.getLogger("processLogger");

	private static String charSet = System.getProperty("file.encoding",
			"ISO-8859-1");
	private static MessageDigest sha;
	private static final int shaOutputLength = 40; // 20 bytes * 2 chars/byte

	private static MessageDigest md5;
	private static final int md5OutputLength = 32;

	static {
		try {
			sha = MessageDigest.getInstance("SHA");
		} catch (Exception e) {
			LOGGER.error("Failed to initialize SHA MessageDigest, Exception: "
					+ ConfigUtils.getStackTraceString(e));
		}

		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
		    LOGGER.error("Failed to initialize MD5 MessageDigest, Exception: "
							+ e.getMessage());
		}
	}

	public static String shaMessageDigest(String plainText) {
		synchronized (sha) {
			// If message digest object wasn't initialized, then we're not
			// using this type of secure encoding.
			if (sha == null)
				return plainText;
			byte[] digestBytes;

			try {

				digestBytes = sha.digest(plainText.getBytes(charSet));

			} catch (UnsupportedEncodingException e) {
			    LOGGER.error("SHA Digest UnsupportedEncodingException: "
						+  ConfigUtils.getStackTraceString(e));
				return null;
			}

			char[] asciiDigestBytes = new char[shaOutputLength];
			for (int i = 0; i < (shaOutputLength / 2); i++) {
				int outputOffset = i * 2;
				byte thisByte = digestBytes[i];
				char upperNibble = toAsciiHexNibble((thisByte & 0xF0) >> 4);
				char lowerNibble = toAsciiHexNibble(thisByte & 0x0F);
				asciiDigestBytes[outputOffset] = upperNibble;
				asciiDigestBytes[outputOffset + 1] = lowerNibble;
			}
			String result = new String(asciiDigestBytes);

			sha.reset();
			return result;
		}
	}

	public static String md5MessageDigest(String plainText) {
		synchronized (md5) {
			// If message digest object wasn't initialized, then we're not
			// using this type of secure encoding.
			if (md5 == null)
				return plainText;

			// MessageDigest needs a byte[];
			byte[] plainBytes = null;

			try {
				plainBytes = plainText.getBytes(charSet);
			} catch (UnsupportedEncodingException e) {
			    LOGGER.error("MD5 Digest UnsupportedEncodingException: "
						+  ConfigUtils.getStackTraceString(e));
				return null;
			}

			// This should be 16 bytes.
			byte[] digestBytes = md5.digest(plainBytes);

			// We use the digest in ASCII Hex format (ASCII-encoded nibbles).
			// Convert.
			char[] asciiDigestBytes = new char[md5OutputLength];
			for (int i = 0; i < (md5OutputLength / 2); i++) {
				int outputOffset = i * 2;
				byte thisByte = digestBytes[i];
				char upperNibble = toAsciiHexNibble((thisByte & 0xF0) >> 4);
				char lowerNibble = toAsciiHexNibble(thisByte & 0x0F);
				asciiDigestBytes[outputOffset] = upperNibble;
				asciiDigestBytes[outputOffset + 1] = lowerNibble;
			}
			return new String(asciiDigestBytes);
		}
	}

	private static char toAsciiHexNibble(int hexValue) {
		char returnValue = '!'; // default
		if ((hexValue >= 0) && (hexValue <= 9)) {
			returnValue = (char) ((int) '0' + hexValue);
		} else if ((hexValue >= 0x000A) && (hexValue <= 0x000F)) {
			returnValue = (char) ((int) 'a' + (hexValue - 0x000A));
		}
		return returnValue;
	}

	public static String getCurrentDataTime() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy.MM.dd HH:mm:ss:SSS zzz");
		Date currTime = new Date();
		return formatter.format(currTime);
	}

	/**
	 * @param args
	 */
	public static String getNonceHash(String hashPasswordArg, String nonceArg) {

		String hashPassword = Crypt.shaMessageDigest(hashPasswordArg);
		return Crypt.md5MessageDigest(nonceArg + ":"
				+ hashPassword);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	/*	if (args.length == 0) {
			//System.out.println("no arguments were given.");
		} else {
			for (String a : args) {
				//System.out.println(a);
			}
		}
		String hashPassword = Crypt.shaMessageDigest(args[0]);
		String passwordDigest = Crypt.md5MessageDigest(args[1] + ":"
				+ hashPassword);*/
		//System.out.println(passwordDigest);

	}

}
