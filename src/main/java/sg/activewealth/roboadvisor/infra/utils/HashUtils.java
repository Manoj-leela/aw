package sg.activewealth.roboadvisor.infra.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

import sg.activewealth.roboadvisor.infra.exception.SystemException;

//http://www.owasp.org/index.php/Hashing_Java
public class HashUtils {
	
	private Logger logger = Logger.getLogger(HashUtils.class);
		
	private static HashUtils me;

	public static HashUtils getInstance() {
		if (me == null) me = new HashUtils();

		return me;
	}

	private final static int ITERATION_NUMBER = 1;

	public String md5(String anyString, String salt) {
		try {
			//get UTF8 bytes
			byte[] queryStringToSignInBytes = anyString.getBytes("UTF-8");
			//create MD5 hash
			MessageDigest md5Digester = MessageDigest.getInstance("MD5");
			md5Digester.update(salt.getBytes("UTF-8"));
			byte[] hashedQueryString = md5Digester.digest(queryStringToSignInBytes);

			//convert to hex
			String hashedStringInHex = new java.math.BigInteger(1,hashedQueryString).toString(16);
			while(hashedStringInHex.length() < 32) hashedStringInHex = "0" + hashedStringInHex;
			return Base64Utils.getInstance().encode(hashedStringInHex.getBytes("UTF-8"));
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	public String getHash(String anyString, String salt) {
		byte[] bSalt = new byte[32];
		byte[] proposedDigest = new byte[32];

		try {
			bSalt = Base64Utils.getInstance().decode(salt);
			proposedDigest = getHash(ITERATION_NUMBER, anyString, bSalt);

			return Base64Utils.getInstance().encode(proposedDigest);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	/**
	 * From a password, a number of iterations and a salt,
	 * returns the corresponding digest
	 * @param iterationNb int The number of iterations of the algorithm
	 * @param password String The password to encrypt
	 * @param salt byte[] The salt
	 * @return byte[] The digested password
	 * @throws NoSuchAlgorithmException If the algorithm doesn't exist
	 * @throws UnsupportedEncodingException 
	 */
	private byte[] getHash(int iterationNb, String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.reset();
		digest.update(salt);
		byte[] input = digest.digest(password.getBytes("UTF-8"));
		for (int i = 0; i < iterationNb; i++) {
			digest.reset();
			input = digest.digest(input);
		}
		return input;
	}

}
