package socket.io.common;
/**
 * @author QingWang
 * @date 2014-10-1 20:48:22
 */
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import socket.io.config.ResourceManager;

public class MD5 {
	
	private static Logger logger = Logger.getLogger(MD5.class);
	// encrypt data with time
	public static String encrypt(int nonce) {
		String input = "nonce=" + nonce + "&password="+ResourceManager.getInstance().getCipherExtendField();
		String output = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(input.getBytes("UTF-8"));
			output = Base64.encodeBase64String(md5.digest());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		return output;
	}
}
