package cn.metathought.tool.encryption;

import cn.metathought.tool.BaseTest;
import cn.metathought.tool.encryption.impl.DecryptImp;
import cn.metathought.tool.encryption.impl.EncryptImp;
import cn.metathought.tool.util.encryption.EncryptUtil;
import cn.metathought.tool.util.encryption.RSAUtil;
import cn.metathought.tool.util.encryption.dto.KeyModel;
import java.io.UnsupportedEncodingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

@Slf4j
public class EncryptionTest extends BaseTest {

  String sourceFilePath = "/Users/zhoubinglong/Downloads/最近两次盘库差距都在500元以上药品.xlsx";
  String secretFilePath = "/Users/zhoubinglong/Downloads/秘文.xlsx";
  String finalFilePath = "/Users/zhoubinglong/Downloads/明文.xlsx";

  @Test
  public void testDES() {
    try {
      String inputStr = "这是加密的内容";
      byte[] inputData = inputStr.getBytes("UTF-8");
      log.info("原文:" + inputStr);
      String key = EncryptUtil.initDESKey();
      log.info("密钥:" + key);
      // 加密
      Encrypt se = new EncryptImp();
      inputData = se.symmetryEncryptByteArray(EncryptUtil.DES, inputData, key, null, null);
      log.info("加密后:" + Base64.encodeBase64String(inputData));
      // 解密
      Decrypt de = new DecryptImp();
      byte[] outputData = de.desDecryptByteArray(inputData, key);
      String outputStr = new String(outputData, "UTF-8");
      log.info("解密后:" + outputStr);
    } catch (Exception e) {
      log.error("DES字符串加解密出错", e);
    }
  }

  @Test
  public void testDesEnDefile() {
    try {
      Encrypt se = new EncryptImp();
      Decrypt sd = new DecryptImp();
      String key = EncryptUtil.initDESKey();
      log.info("密钥:" + key);
      se.symmetryEncryptFile(EncryptUtil.DES, sourceFilePath, secretFilePath, key, null);
      sd.desDecryptFile(secretFilePath, finalFilePath, key);
    } catch (Exception e) {
      log.error("DES文件加解密出错", e);
    }
  }

  @Test
  public void test3DES() {
    try {
      String inputStr = "这是加密的内容";
      byte[] inputData = inputStr.getBytes("UTF-8");
      log.info("原文:" + inputStr);
      String key = EncryptUtil.init3DESKey();
      log.info("密钥:" + key);
      // 加密
      Encrypt se = new EncryptImp();
      inputData = se.symmetryEncryptByteArray(EncryptUtil.DESEDE, inputData, key, null, null);
      log.info("加密后:" + Base64.encodeBase64String(inputData));
      // 解密
      Decrypt de = new DecryptImp();
      byte[] outputData = de.desedeDecryptByteArray(inputData, key);
      String outputStr = new String(outputData, "UTF-8");
      log.info("解密后:" + outputStr);
    } catch (Exception e) {
      log.error("3DES字符串加解密出错", e);
    }
  }

  @Test
  public void test3DesEnDefile() {
    try {
      Encrypt se = new EncryptImp();
      Decrypt sd = new DecryptImp();
      String key = EncryptUtil.init3DESKey();
      log.info("密钥:" + key);
      se.symmetryEncryptFile(EncryptUtil.DESEDE, sourceFilePath, secretFilePath, key, null);
      sd.desedeDecryptFile(secretFilePath, finalFilePath, key);
    } catch (Exception e) {
      log.error("3DES文件加解密出错", e);
    }
  }

  @Test
  public void testAES() {
    try {
      String inputStr = "这是加密的内容";
      byte[] inputData = inputStr.getBytes("UTF-8");
      log.info("原文:" + inputStr);
      String key = EncryptUtil.initAESKey();
      log.info("密钥:" + key);
      // 加密
      Encrypt se = new EncryptImp();
      inputData = se.symmetryEncryptByteArray(EncryptUtil.AES, inputData, key, null, null);
      log.info("加密后:" + Base64.encodeBase64String(inputData));
      // 解密
      Decrypt de = new DecryptImp();
      byte[] outputData = de.aesDecryptByteArray(inputData, key);
      String outputStr = new String(outputData, "UTF-8");
      log.info("解密后:" + outputStr);
    } catch (Exception e) {
      log.error("AES字符串加解密出错", e);
    }
  }

  @Test
  public void testAesEnDefile() {
    try {
      Encrypt se = new EncryptImp();
      Decrypt sd = new DecryptImp();
      String key = EncryptUtil.initAESKey();
      log.info("密钥:" + key);
      se.symmetryEncryptFile(EncryptUtil.AES, sourceFilePath, secretFilePath, key, null);
      sd.aesDecryptFile(secretFilePath, finalFilePath, key);
    } catch (Exception e) {
      log.error("AES文件加解密出错", e);
    }

  }

  @Test
  public void testPBE() {
    try {
      String inputStr = "这是加密的内容";
      byte[] inputData = inputStr.getBytes("UTF-8");
      log.info("原文:" + inputStr);
      String salt = EncryptUtil.initPBEKey();
      log.info("盐:" + salt);
      String password = "123456";
      log.info("口令:" + password);
      // 加密
      Encrypt se = new EncryptImp();
      inputData = se.symmetryEncryptByteArray(EncryptUtil.PBE, inputData, null, salt, password);
      log.info("加密后:" + Base64.encodeBase64String(inputData));
      // 解密
      Decrypt de = new DecryptImp();
      byte[] outputData = de.pbeDecryptByteArray(inputData, salt,
          password);
      String outputStr = new String(outputData, "UTF-8");
      log.info("解密后:" + outputStr);
    } catch (Exception e) {
      log.error("PBE字符串加解密出错", e);
    }
  }

  @Test
  public void testPBEEnDefile() {
    try {
      Encrypt se = new EncryptImp();
      Decrypt sd = new DecryptImp();
      String salt = EncryptUtil.initPBEKey();
      log.info("盐:" + salt);
      String password = "123456";
      log.info("口令:" + password);
      se.symmetryEncryptFile(EncryptUtil.PBE, sourceFilePath, secretFilePath, salt, password);
      sd.pbeDecryptFile(secretFilePath, finalFilePath, salt, password);
    } catch (Exception e) {
      log.error("PBE文件加解密出错", e);
    }
  }

  @Test
  public void testRsaEncryptByPrivateKey() {
    KeyModel keyModel = initPrivateAndPublicKey();
    String inputStr = "私钥加密公钥解密";
    log.info("原文:" + inputStr);
    try {
      // 加密
      byte[] data = new byte[0];
      data = inputStr.getBytes("UTF-8");
      Encrypt en = new EncryptImp();
      byte[] encodedData = en.rsaEncryptByPrivateKey(data,
          keyModel.getSk());
      log.info("加密后:" + Base64.encodeBase64String(encodedData));

      // 解密
      Decrypt dec = new DecryptImp();
      byte[] decodedData = dec.rsaDecryptByPublicKey(encodedData,
          keyModel.getAk());
      String outputStr = new String(decodedData);
      log.info("解密后:" + outputStr);
    } catch (Exception e) {
      log.error("非对称算法私钥加密公钥解密失败", e);
    }
  }

  @Test
  public void testRsaEncryptByPublicKey() {
    KeyModel keyModel = initPrivateAndPublicKey();
    String inputStr = "公钥加密私钥解密";
    log.info("原文:" + inputStr);
    try {
      // 加密
      byte[] data = new byte[0];
      data = inputStr.getBytes("UTF-8");
      Encrypt en = new EncryptImp();
      byte[] encodedData = en.rsaEncryptByPublicKey(data,
          keyModel.getAk());
      log.info("加密后:\n" + Base64.encodeBase64String(encodedData));

      // 解密
      Decrypt dec = new DecryptImp();
      byte[] decodedData = dec.rsaDecryptByPrivateKey(encodedData,
          keyModel.getSk());
      String outputStr = new String(decodedData);
      log.info("解密后: " + outputStr);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testRSADigitalSign() {
    KeyModel model = initPrivateAndPublicKey();
    String data = "RSA数字签名内容";
    log.info("原内容:" + data);
    String signedData = RSAUtil.getRSADigitalSign(data, model.getSk());
    log.info("签名后内容:" + signedData);
    boolean flag = RSAUtil
        .verifyRSADigital(data, signedData, model.getAk());
    log.info("RSA数字签名验证结果:" + flag);
  }

  private KeyModel initPrivateAndPublicKey() {
    KeyModel keyModule = RSAUtil.getRsaKeyPair();// 获取公钥私钥对

    log.info("公钥:" + keyModule.getAk());
    log.info("私钥:" + keyModule.getSk());

    return keyModule;
  }
}
