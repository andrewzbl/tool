package cn.metathought.tool.util.encryption;

import cn.metathought.tool.util.encryption.dto.KeyModel;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

/**
 * 非对称加密算法RSA工具类
 *
 * @author zbl
 * @date 2016.05.05
 */
@Slf4j
public class RSAUtil {
  public final static int RSA_ENBYPUBLIC = 1;// 公钥加密
  public final static int RSA_ENBYPRIVATE = 2;// 私钥加密
  public final static int RSA_DEBYPUBLIC = 3; // 公钥解密
  public final static int RSA_DEBYPRIVATE = 4;// 私钥解密
  public final static String PUBLIC_KEY = "ak";// 公钥
  public final static String PRIVATE_KEY = "sk"; // 私钥

  /**
   * 初始化密钥,生成密匙对，并保存到map中 PUBLIC_KEY：公钥 PRIVATE_KEY：私钥
   *
   * @return Map 密钥Map
   */
  public static Map<String, Object> initKey() {
    // 初始化并且生成密匙对
    KeyPair keyPair = initKeyPair();
    // 公钥
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    // 私钥
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    // 封装密钥
    Map<String, Object> keyMap = new HashMap<String, Object>(2);
    keyMap.put(PUBLIC_KEY, publicKey);
    keyMap.put(PRIVATE_KEY, privateKey);
    return keyMap;
  }

  /**
   * 初始化，并生成密匙对
   */
  public static KeyPair initKeyPair() {
    // 实例化密钥对生成器
    KeyPairGenerator keyPairGen = null;
    KeyPair keyPair = null;
    try {
      keyPairGen = KeyPairGenerator.getInstance("RSA");
      // 初始化密钥对生成器
      keyPairGen.initialize(512);
      // 生成密钥对
      keyPair = keyPairGen.generateKeyPair();
    } catch (NoSuchAlgorithmException e) {
      log.error("初始化密匙器失败", e);
    }
    return keyPair;
  }

  /**
   * 获取KeyFactory
   */
  public static KeyFactory getKeyFactory() {
    KeyFactory keyFactory = null;
    try {
      keyFactory = KeyFactory.getInstance("RSA");
    } catch (NoSuchAlgorithmException e) {
      log.error("获取KeyFactory失败", e);
    }
    return keyFactory;
  }

  public static PKCS8EncodedKeySpec buildEncodingKey(byte[] key) {
    return new PKCS8EncodedKeySpec(key);
  }

  public static X509EncodedKeySpec getX509EncodedKeySpec(byte[] key) {
    return new X509EncodedKeySpec(key);
  }

  /**
   * 生成私钥
   */
  public static PrivateKey createPrivateKey(KeyFactory keyFactory,
      PKCS8EncodedKeySpec pkcs8KeySpec) {
    try {
      return keyFactory.generatePrivate(pkcs8KeySpec);
    } catch (InvalidKeySpecException e) {
      log.error("生成私钥失败", e);
      return null;
    }
  }

  /**
   * 生成公钥
   */
  public static PublicKey getPublicKey(KeyFactory keyFactory,
      X509EncodedKeySpec x509KeySpec) {
    try {
      return keyFactory.generatePublic(x509KeySpec);
    } catch (InvalidKeySpecException e) {
      log.error("生成公钥失败", e);
      return null;
    }
  }

  /**
   * 执行RSA私钥加密/解密
   */
  public static byte[] doPrivateKeyFinal(KeyFactory keyFactory,
      PrivateKey privateKey, int mode, byte[] data) {
    try {
      Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
      cipher.init(mode, privateKey);
      return cipher.doFinal(data);
    } catch (Exception e) {
      log.error("执行RSA私钥加/解密出错", e);
      return null;
    }
  }

  /**
   * 执行RSA公钥加/解密
   */
  public static byte[] doPublicKeyFinal(KeyFactory keyFactory,
      PublicKey publicKey, int mode, byte[] data) {
    try {
      Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
      cipher.init(mode, publicKey);
      return cipher.doFinal(data);
    } catch (Exception e) {
      log.error("执行RSA公钥加/解密出错", e);
      return null;
    }
  }

  public static Boolean ByteEquals(byte[] x, byte[] y) {
    if (x.length == y.length) {
      for (int i = 0; i < x.length; i++) {
        if (x[i] != y[i]) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  /**
   * 取得公钥
   *
   * @param keyMap 密钥Map
   * @return 字符串格式公钥
   */
  public static String getPublicKey(Map<String, Object> keyMap) {
    Key key = (Key) keyMap.get(PUBLIC_KEY);
    return Base64.encodeBase64String(key.getEncoded());
  }

  /**
   * 取得私钥
   *
   * @param keyMap 密钥Map
   * @return 字符串格式私钥
   */
  public static String getPrivateKey(Map<String, Object> keyMap) {
    Key key = (Key) keyMap.get(PRIVATE_KEY);
    return Base64.encodeBase64String(key.getEncoded());
  }

  /**
   * 对外接口，提供RSA的 ak 和 sk 分别以字符串的形式返回
   *
   * @return KeyModel
   */
  public static KeyModel getRsaKeyPair() {
    Map<String, Object> keyMap = RSAUtil.initKey();
    KeyModel model = new KeyModel();
    model.setAk(RSAUtil.getPublicKey(keyMap));
    model.setSk(RSAUtil.getPrivateKey(keyMap));
    return model;
  }

  /**
   * 执行RSA数字签名
   *
   * @param data 待签名内容
   * @param privateKey 系统生成的字符串格式私钥
   * @return 签名后内容
   */
  public static String getRSADigitalSign(String data, String privateKey) {
    try {
      PKCS8EncodedKeySpec pkcs8Spec = new PKCS8EncodedKeySpec(
          Base64.decodeBase64(privateKey));
      KeyFactory keyFatory = getKeyFactory();
      PrivateKey priKey = keyFatory.generatePrivate(pkcs8Spec);
      Signature signature = Signature.getInstance("MD5withRSA");
      signature.initSign(priKey);
      signature.update(data.getBytes());
      return Base64.encodeBase64String(signature.sign());
    } catch (Exception e) {
      log.error("RSA数字签名失败", e);
      return null;
    }
  }

  /**
   * RSA数字签名验证
   *
   * @param data 未签名原内容
   * @param signedData 已签名内容
   * @param publicKey 系统生成的字符串公钥
   */
  public static boolean verifyRSADigital(String data, String signedData,
      String publicKey) {
    try {
      X509EncodedKeySpec x509Spec = new X509EncodedKeySpec(
          Base64.decodeBase64(publicKey));
      KeyFactory keyFactory = getKeyFactory();
      PublicKey pubKey = keyFactory.generatePublic(x509Spec);
      Signature signature = Signature.getInstance("MD5withRSA");
      signature.initVerify(pubKey);
      signature.update(data.getBytes());
      return signature.verify(Base64.decodeBase64(signedData));
    } catch (Exception e) {
      log.error("验证RSA数字签名失败", e);
      return false;
    }
  }
}
