package cn.metathought.tool.util.encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.StringUtils;

/**
 * 加密解密工具类
 * 
 * @author zbl
 * @date 2015.04.13
 *
 */
@Slf4j
public class EncryptUtil {
	public static final String AES = "AES";
	public static final String DES = "DES";
	public static final String DESEDE = "DESede";
	public static final String PBE = "PBE";

	/**
	 * 消息摘要算法-MD5加密(cc实现方式)
	 * 
	 * @param data
	 * @return
	 */
	public static String md5(String data) {
		return DigestUtils.md5Hex(data);
	}

	/**
	 * encode file to base64 Code String
	 * 
	 * @param fileName
	 *            file path
	 * @return *
	 * @throws Exception
	 */
	public static String fileToBase64(String fileName) throws Exception {
		File file = new File(fileName);
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		return Base64.encodeBase64String(buffer);
	}

	/**
	 * base64 Code decode String save to targetPath
	 * 
	 * @param base64Code
	 * @param targetPath
	 * @throws Exception
	 */
	public static void decodeBase64ToFile(String base64Code, String targetPath) throws Exception {
		byte[] buffer = Base64.decodeBase64(base64Code);
		FileOutputStream out = new FileOutputStream(targetPath);
		out.write(buffer);
		out.close();
	}

	/**
	 * base64 code save to file
	 * 
	 * @param base64Code
	 * @param targetPath
	 * @throws Exception
	 */
	public static void base64ToFile(String base64Code, String targetPath) throws Exception {
		byte[] buffer = base64Code.getBytes();
		FileOutputStream out = new FileOutputStream(targetPath);
		out.write(buffer);
		out.close();
	}

	public static String base64UrlEncode(byte[] simple) {
		String s = new String(Base64.encodeBase64(simple)); // Regular base64
		// encoder
		s = s.split("=")[0]; // Remove any trailing '='s
		s = s.replace('+', '-'); // 62nd char of encoding
		s = s.replace('/', '_'); // 63rd char of encoding
		return s;
	}

	public static byte[] base64UrlDecode(String cipher) {
		String s = cipher;
		s = s.replace('-', '+'); // 62nd char of encoding
		s = s.replace('_', '/'); // 63rd char of encoding
		switch (s.length() % 4) { // Pad with trailing '='s
		case 0:
			break; // No pad chars in this case
		case 2:
			s += "==";
			break; // Two pad chars
		case 3:
			s += "=";
			break; // One pad char
		default:
			System.err.println("Illegal base64url String!");
		}
		return Base64.decodeBase64(s); // Standard base64 decoder
	}

	/**
	 * 生成token验证码
	 * 
	 * @param key
	 *            约定key值
	 * @param dateVerify
	 *            是否需要过期时间验证
	 * @return token验证码
	 */
	public static String generateToken(Map<String, String> params, String key, boolean dateVerify) {
		String text = createLinkString(params) + key;
		String token = EncryptUtil.md5(text);
		if (dateVerify) {
			token = token + Arithmetic.getEncString(String.valueOf(System.currentTimeMillis()));
		}
		return token;
	}

	/**
	 * URL加密验证(md5)
	 * 
	 * @param token
	 *            token验证码
	 * @param params
	 *            参数值
	 * @param key
	 *            约定key值
	 * @param dateVerify
	 *            是否需要过期时间验证
	 * @param deadline
	 *            过期时间,单位为秒
	 * @return
	 */
	public static boolean verifyUrl(String token, Map<String, String> params,
			String key, boolean dateVerify, String deadline) {
		String text = createLinkString(params) + key;
		if (dateVerify) {
			return verifyDate(token, deadline) && verifyToken(text, token, dateVerify);
		}
		return verifyToken(text, token, dateVerify);
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}
		return prestr;
	}

	private static boolean verifyToken(String text, String token,
			boolean dateVerify) {
		try {
			if (dateVerify) {
				return md5(text).equals(token.substring(0, 32));
			} else {
				return md5(text).equals(token);
			}
		} catch (Exception e) {
			log.error("verify token error", e);
			return false;
		}
	}

	private static boolean verifyDate(String token, String deadline) {
		try {
			String paramDateValue = token.substring(32, 64);
			// 默认超时时间为半小时
			long secondFormDeadLine = StringUtils.isEmpty(deadline) ? 30 * 60 : Long.valueOf(deadline);
			long createTime = Long.valueOf(Arithmetic.getDesString(paramDateValue));
			if ((System.currentTimeMillis() / 1000 - createTime / 1000) > secondFormDeadLine) {
				return false;
			}
			return true;
		} catch (Exception e) {
			log.error("verify date error", e);
			return false;
		}
	}

	/**
	 * 还原密钥
	 * 
	 * @param type
	 *            加密类型
	 * @param key
	 *            密钥
	 * @return
	 */
	public static Key toKey(String type, String key, String password) {
		try {
			if (EncryptUtil.DES.equals(type)) {
				DESKeySpec dks = new DESKeySpec(Base64.decodeBase64(key));// 实例化DES密钥材料
				SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(EncryptUtil.DES);// 实例化密钥工厂
				return keyFactory.generateSecret(dks);// 生成秘密密钥
			} else if (EncryptUtil.DESEDE.equals(type)) {
				DESedeKeySpec dks = new DESedeKeySpec(Base64.decodeBase64(key));// 实例化DES密钥材料
				SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(EncryptUtil.DESEDE);// 实例化密钥工厂
				return keyFactory.generateSecret(dks);// 生成秘密密钥
			} else if (EncryptUtil.AES.equals(type)) {
				return new SecretKeySpec(Base64.decodeBase64(key), EncryptUtil.AES);// 实例化AES密钥材料
			} else if (EncryptUtil.PBE.equals(type)) {
				PBEKeySpec pbeSpec = new PBEKeySpec(password.toCharArray());
				SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWITHMD5andDES");
				return keyFactory.generateSecret(pbeSpec);
			} else {
				return null;
			}
		} catch (Exception e) {
			log.error("还原密钥失败", e);
			return null;
		}
	}

	/**
	 * 初始化加密模式
	 *
	 * @param type
	 *            加密类型
	 * @param key
	 *            密钥
	 * @param salt
	 *            字符串格式盐值
	 * @param mode
	 *            加密模式
	 * @return
	 */
	public static Cipher initCipher(String type, Key key, String salt, int mode) {
		Cipher cipher = null;
		try {
			if (EncryptUtil.DES.equals(type)) {
				cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
				cipher.init(mode, key);
			} else if (EncryptUtil.AES.equals(type)) {
				cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
				cipher.init(mode, key);
			} else if (EncryptUtil.DESEDE.equals(type)) {
				cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
				cipher.init(mode, key);
			} else if (EncryptUtil.PBE.equals(type)) {
				PBEParameterSpec param = new PBEParameterSpec(
						Base64.decodeBase64(salt), 100);
				cipher = Cipher.getInstance("PBEWITHMD5andDES");
				cipher.init(mode, key, param);
			}
			return cipher;
		} catch (Exception e) {
			log.error("初始化加密模式失败", e);
			return cipher;
		}
	}

	/**
	 * 执行操作
	 *
	 * @param cipher
	 * @param data
	 * @return
	 */
	public static byte[] doFinal(Cipher cipher, byte[] data) {
		try {
			return cipher.doFinal(data);
		} catch (Exception e) {
			log.error("执行加解密操作失败", e);
			return null;
		}
	}

	/**
	 * 加密文件
	 * 
	 * @param type
	 *            加密类型
	 * @param sourceFile
	 *            源文件路径
	 * @param destFile
	 *            目标文件路径
	 * @param skey
	 *            经过Base64.encodeBase64String的字符串格式密匙
	 * @param password 自定义口令
	 */
	public static void encryptFile(String type, String sourceFile, String destFile, String skey, String password) {
		EncryptUtil.createNotExitFile(destFile);
		Cipher cipher = initCipher(type, toKey(type, skey, password), skey, Cipher.ENCRYPT_MODE);
		try (CipherInputStream cis = new CipherInputStream(new FileInputStream(sourceFile), cipher);
			OutputStream out = new FileOutputStream(destFile)){
			byte[] buffer = new byte[1024 * 10];
			int r;
			while ((r = cis.read(buffer)) > 0) {
				out.write(buffer, 0, r);
			}
		} catch (Exception e) {
			log.error("加密文件失败", e);
		}
	}

	/**
	 * 解密文件
	 * 
	 * @param type
	 *            加密类型
	 * @param sourceFile
	 *            源文件路径
	 * @param destFile
	 *            目标文件路径
	 * @param skey
	 *            经过Base64.encodeBase64String的字符串格式密匙
	 * @param password
	 *            自定义口令
	 */
	public static void decryptFile(String type, String sourceFile, String destFile, String skey, String password) {
		EncryptUtil.createNotExitFile(destFile);
		Cipher cipher = initCipher(type, toKey(type, skey, password), skey, Cipher.DECRYPT_MODE);
		try (InputStream is = new FileInputStream(sourceFile);
			CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(destFile), cipher)){
			byte[] buffer = new byte[1024 * 10];
			int r;
			while ((r = is.read(buffer)) >= 0) {
				cos.write(buffer, 0, r);
			}
		} catch (Exception e) {
			log.error("解密文件失败", e);
		}
	}

	public static void createNotExitFile(String destFile) {
		String path0 = destFile.substring(0, destFile.lastIndexOf(File.separator) + 1);
		File outFile = new File(path0);
		if (!outFile.exists()) {
			outFile.mkdirs();
		}
		outFile = new File(destFile);
		if (!outFile.exists()) {
			try {
				outFile.createNewFile();
			} catch (IOException e) {
				log.error("加解密创建文件失败", e);
			}
		}
	}

	/**
	 * 生成DES密钥
	 * <p>
	 * 注: JDK的实现支持56bit密钥,BC支持64bit密钥,我们采用BC
	 *
	 * @return DES字符串密钥
	 * @throws Exception
	 */
	public static String initDESKey() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		KeyGenerator kg = KeyGenerator.getInstance(EncryptUtil.DES, "BC");
		kg.init(64);
		SecretKey secretKey = kg.generateKey();// 生成秘密密钥
		String key = Base64.encodeBase64String(secretKey.getEncoded());
		return key;// 获得字符串格式的密钥
	}

	/**
	 * 生成3DES密钥
	 *
	 * @return 3DES字符串密钥
	 * @throws Exception
	 */
	public static String init3DESKey() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		KeyGenerator kg = KeyGenerator.getInstance(EncryptUtil.DESEDE, "BC");
		kg.init(168);
		SecretKey secretKey = kg.generateKey();// 生成秘密密钥
		String key = Base64.encodeBase64String(secretKey.getEncoded());
		return key;// 获得字符串格式的密钥
	}

	/**
	 * 生成AES密钥
	 * 
	 * @return AES字符串制密钥
	 * @throws Exception
	 */
	public static String initAESKey() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		KeyGenerator kg = KeyGenerator.getInstance("AES", "BC");
		kg.init(128);// AES 要求密钥长度为 128位、192位或 256位
		SecretKey secretKey = kg.generateKey();// 生成秘密密钥
		String key = Base64.encodeBase64String(secretKey.getEncoded());
		return key;// 获得字符串格式的密钥
	}

	/**
	 * 生成PBE盐值
	 *
	 * @return PBE字符串盐值
	 * @throws Exception
	 */
	public static String initPBEKey() throws Exception {
		SecureRandom random = new SecureRandom();
		String salt = Base64.encodeBase64String(random.generateSeed(8));
		return salt;// 获得字符串格式的盐
	}
}
