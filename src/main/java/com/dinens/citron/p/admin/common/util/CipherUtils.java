package com.dinens.citron.p.admin.common.util;

import java.io.UnsupportedEncodingException;

import java.security.InvalidKeyException;

import java.security.KeyPair;

import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.security.PrivateKey;

import java.security.PublicKey;

import java.security.SecureRandom;

import java.util.Base64;


import javax.crypto.BadPaddingException;

import javax.crypto.Cipher;

import javax.crypto.IllegalBlockSizeException;

import javax.crypto.NoSuchPaddingException;

/**
 * <PRE>
 * 1. ClassName: CipherUtils
 * 2. FileName : CipherUtils.java
 * 3. Package  : com.dinens.citron.common.util
 * 4. Comment  : 보안관련 함수 모음
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

public class CipherUtils {

    /**
     * 1024비트 RSA 키쌍을 생성합니다.
     */
    public static KeyPair genRSAKeyPair() throws NoSuchAlgorithmException {

        SecureRandom secureRandom = new SecureRandom();
        KeyPairGenerator gen;

        gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(1024, secureRandom);

        KeyPair keyPair = gen.genKeyPair();

        return keyPair;
    }


    /**
     * Public Key로 RSA 암호화를 수행합니다.
     * @param plainText 암호화할 평문입니다.
     * @param publicKey 공개키 입니다.
     * @return
     */
    public static String encryptRSA(String plainText, PublicKey publicKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
                      BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] bytePlain = cipher.doFinal(plainText.getBytes());
        String encrypted = Base64.getEncoder().encodeToString(bytePlain);

    	return encrypted;
    }


    /**
     * Private Key로 RAS 복호화를 수행합니다.
     *
     * @param encrypted 암호화된 이진데이터를 base64 인코딩한 문자열 입니다.
     * @param privateKey 복호화를 위한 개인키 입니다.
     * @return
     * @throws Exception
     */
    public static String decryptRSA(String encrypted, PrivateKey privateKey)
    		throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
    		         BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        Cipher cipher = Cipher.getInstance("RSA");
        
        byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes());

        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        
        byte[] bytePlain = cipher.doFinal(byteEncrypted);
        String decrypted = new String(bytePlain, "utf-8");

        return decrypted;
    }
    
    
    /**
     * 14~40자리 Security Random Salt를 생성합니다.
     */
    public static String genSecureRandomSalt(int len) throws NoSuchAlgorithmException {
    	SecureRandom random = SecureRandom.getInstance("SHA1PRNG");		
		String randomNum = new Integer(random.nextInt()).toString();
		
		MessageDigest digest = MessageDigest.getInstance("SHA-1");				
		byte[] result = digest.digest(randomNum.getBytes());
		
		String returnVal = hexEncode(result);
		
		return returnVal.substring(returnVal.length() - len);
    }
    
    
    /**
     * Byte Array를 Hex 코드로 변환합니다.
     *
     * @param Byte Array
     * @param Hex 코드로 변환된 String
     * @return
     * @throws Exception
     */
    static private String hexEncode(byte[] input) {
		StringBuffer result = new StringBuffer();
		char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		
		for(int i=0; i<input.length; i++) {
			byte temp = input[i];
			result.append(digits[(temp&0xf0) >> 4]);
			result.append(digits[temp&0x0f]);
		}
		return result.toString();
	}
}

