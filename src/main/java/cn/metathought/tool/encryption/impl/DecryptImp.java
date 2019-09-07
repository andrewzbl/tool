package cn.metathought.tool.encryption.impl;

import cn.metathought.tool.encryption.Decrypt;
import cn.metathought.tool.util.encryption.EncryptUtil;
import cn.metathought.tool.util.encryption.RSAUtil;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;

/**
 * 解密实现类
 * <p>
 * 包含:
 * <p>
 * 对称加密:DES,3DES,AES,PBE,非对称加密:RSA
 * 
 * @author zbl
 * @date 2016.05.04
 *
 */
public class DecryptImp implements Decrypt {
	@Override
	public byte[] desDecryptByteArray(byte[] file, String key) {
		Key k = EncryptUtil.toKey(EncryptUtil.DES, key, null);// 还原密钥
		// 实例化 初始化，设置为解密模式
		Cipher cipher = EncryptUtil.initCipher(EncryptUtil.DES, k, null,
				Cipher.DECRYPT_MODE);
		return EncryptUtil.doFinal(cipher, file);// 执行操作
	}

	@Override
	public byte[] desedeDecryptByteArray(byte[] file, String key) {
		Key k = EncryptUtil.toKey(EncryptUtil.DESEDE, key, null);// 还原密钥
		// 实例化 初始化，设置为解密模式
		Cipher cipher = EncryptUtil.initCipher(EncryptUtil.DESEDE, k, null,
				Cipher.DECRYPT_MODE);
		return EncryptUtil.doFinal(cipher, file);// 执行操作
	}

	@Override
	public byte[] aesDecryptByteArray(byte[] file, String key) {
		Key k = EncryptUtil.toKey(EncryptUtil.AES, key, null);// 还原密钥
		// 实例化 初始化，设置为解密模式
		Cipher cipher = EncryptUtil.initCipher(EncryptUtil.AES, k, null,
				Cipher.DECRYPT_MODE);
		return EncryptUtil.doFinal(cipher, file);// 执行操作
	}

	@Override
	public byte[] pbeDecryptByteArray(byte[] file, String salt, String password) {
		Key k = EncryptUtil.toKey(EncryptUtil.PBE, null, password);// 还原密钥
		// 实例化 初始化，设置为解密模式
		Cipher cipher = EncryptUtil.initCipher(EncryptUtil.PBE, k, salt,
				Cipher.DECRYPT_MODE);
		return EncryptUtil.doFinal(cipher, file);// 执行操作
	}

	@Override
	public void desDecryptFile(String sourceFile, String destFile, String skey) {
		EncryptUtil.decryptFile(EncryptUtil.DES, sourceFile, destFile, skey,
				null);
	}

	@Override
	public void desedeDecryptFile(String sourceFile, String destFile,
			String skey) {
		EncryptUtil.decryptFile(EncryptUtil.DESEDE, sourceFile, destFile, skey,
				null);
	}

	@Override
	public void aesDecryptFile(String sourceFile, String destFile, String skey) {
		EncryptUtil.decryptFile(EncryptUtil.AES, sourceFile, destFile, skey,
				null);
	}

	@Override
	public void pbeDecryptFile(String sourceFile, String destFile, String salt,
			String password) {
		EncryptUtil.decryptFile(EncryptUtil.PBE, sourceFile, destFile, salt,
				password);
	}

	@Override
	public byte[] rsaDecryptByPublicKey(byte[] data, String key) {
		// 取得公钥
		X509EncodedKeySpec x509KeySpec = RSAUtil.getX509EncodedKeySpec(Base64
				.decodeBase64(key));
		KeyFactory keyFactory = RSAUtil.getKeyFactory();
		// 生成公钥
		PublicKey publicKey = RSAUtil.getPublicKey(keyFactory, x509KeySpec);
		// 对数据解密
		return RSAUtil.doPublicKeyFinal(keyFactory, publicKey,
				Cipher.DECRYPT_MODE, data);
	}

	@Override
	public byte[] rsaDecryptByPrivateKey(byte[] data, String key) {
		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(
				Base64.decodeBase64(key));
		KeyFactory keyFactory = RSAUtil.getKeyFactory();
		// 生成私钥
		PrivateKey privateKey = RSAUtil.createPrivateKey(keyFactory,
				pkcs8KeySpec);
		// 对数据解密
		return RSAUtil.doPrivateKeyFinal(keyFactory, privateKey,
				Cipher.DECRYPT_MODE, data);
	}
}
