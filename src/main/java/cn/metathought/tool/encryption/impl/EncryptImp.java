package cn.metathought.tool.encryption.impl;

import cn.metathought.tool.encryption.Encrypt;
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
 */
public class EncryptImp implements Encrypt {

  @Override
  public byte[] symmetryEncryptByteArray(String encryptType, byte[] file, String key, String salt, String password) {
    // 还原密钥
    Key k = EncryptUtil.toKey(encryptType, key, password);
    // 实例化,初始化，设置为加密模式
    Cipher cipher = EncryptUtil.initCipher(encryptType, k, salt, Cipher.ENCRYPT_MODE);
    // 执行操作
    return EncryptUtil.doFinal(cipher, file);
  }

  @Override
  public void symmetryEncryptFile(String encryptType, String sourceFile, String destFile, String salt, String password) {
    EncryptUtil.encryptFile(encryptType, sourceFile, destFile, salt, password);
  }

  @Override
  public byte[] rsaEncryptByPrivateKey(byte[] data, String key) {
    // 取得私钥
    PKCS8EncodedKeySpec pkcs8KeySpec = RSAUtil.buildEncodingKey(Base64
        .decodeBase64(key));
    KeyFactory keyFactory = RSAUtil.getKeyFactory();
    // 生成私钥
    PrivateKey privateKey = RSAUtil.createPrivateKey(keyFactory,
        pkcs8KeySpec);
    // 对数据加密
    return RSAUtil.doPrivateKeyFinal(keyFactory, privateKey,
        Cipher.ENCRYPT_MODE, data);
  }

  @Override
  public byte[] rsaEncryptByPublicKey(byte[] data, String key) {
    // 取得公钥
    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(
        Base64.decodeBase64(key));
    KeyFactory keyFactory = RSAUtil.getKeyFactory();
    PublicKey publicKey = RSAUtil.getPublicKey(keyFactory, x509KeySpec);
    // 对数据加密
    return RSAUtil.doPublicKeyFinal(keyFactory, publicKey,
        Cipher.ENCRYPT_MODE, data);
  }

}
