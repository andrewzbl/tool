package cn.metathought.tool.encryption;

/**
 * 加密接口
 * <p>
 * 包含:
 * <p>
 * 对称加密:DES,3DES,AES,PBE,非对称加密:RSA
 *
 * @author zbl
 * @date 2016.05.04
 */
public interface Encrypt {

  /**
   * 对byte[]数组进行对称加密
   *
   * @param encryptType 对称加密方式：DES加密：EncryptUtil.DES 3DES加密：EncryptUtil.DESede AES加密：EncryptUtil.AES PBE加密：EncryptUtil.PBE
   * @param origin 需要加密的byte[]
   * @param key 秘钥
   * @param salt 系统生成的盐
   * @param password 自定义口令值
   * @return 加密后的byte[]
   */
  byte[] symmetryEncryptByteArray(String encryptType, byte[] origin, String key, String salt, String password);

  /**
   * 对文件进行对称加密
   *
   * @param encryptType 对称加密方式：DES加密：EncryptUtil.DES 3DES加密：EncryptUtil.DESede AES加密：EncryptUtil.AES PBE加密：EncryptUtil.PBE
   * @param sourceFile 源文件
   * <p>
   * 例:C:\abc.txt
   * @param destFile 加密后的文件
   * <p>
   * 例:C\加密后abc.txt
   * @param salt 系统生成的盐
   * @param password 自定义口令
   */
  void symmetryEncryptFile(String encryptType, String sourceFile, String destFile, String salt, String password);

  /**
   * rsa私钥加密
   *
   * @param data 需要加密的data[]
   * @param key 密匙
   * @return 加密后的byte[]
   */
  byte[] rsaEncryptByPrivateKey(byte[] data, String key);

  /**
   * rsa公钥加密
   *
   * @param data 需要加密的data[]
   * @param key 密匙
   * @return 加密后的byte[]
   */
  byte[] rsaEncryptByPublicKey(byte[] data, String key);
}
