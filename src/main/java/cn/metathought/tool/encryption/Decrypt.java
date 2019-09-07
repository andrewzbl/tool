package cn.metathought.tool.encryption;

/**
 * 解密接口
 * <p>
 * 包含:
 * <p>
 * 对称加密:DES,3DES,AES,PBE,非对称加密:RSA
 * 
 * @author zbl
 * @date 2016.05.04
 *
 */
public interface Decrypt {
	/**
	 * 对字节数组des解密
	 *
	 * @param file
	 *            需要加密的字节数组
	 * @param key
	 *            系统生成的64位密匙
	 * @return 解密后的字节数组
	 */
	public byte[] desDecryptByteArray(byte[] file, String key);

	/**
	 * 对字节数组3des解密
	 *
	 * @param file
	 *            需要加密的字节数组
	 * @param key
	 *            系统生成的168位密匙
	 * @return 解密后的字节数组
	 */
	public byte[] desedeDecryptByteArray(byte[] file, String key);

	/**
	 * 对字节数组aes解密
	 *
	 * @param file
	 *            需要加密的字节数组
	 * @param key
	 *            系统生成的64位密匙
	 * @return 解密后的字节数组
	 */
	public byte[] aesDecryptByteArray(byte[] file, String key);

	/**
	 * 对字节数组pbe解密
	 *
	 * @param file
	 *            需要加密的字节数组
	 * @param salt
	 *            系统生成的盐
	 * @param password
	 *            自定义口令值
	 * @return 解密后的字节数组
	 */
	public byte[] pbeDecryptByteArray(byte[] file, String salt, String password);

	/**
	 * 对文件进行des解密
	 *
	 * @param sourceFile
	 *            被加密的文件路径
	 *            <p>
	 *            例子:C:/abc.txt
	 * @param destFile
	 *            生成解密后的文件路径
	 *            <p>
	 *            例子:C:/加密后abc.txt
	 * @param skey
	 *            系统生成的256位密钥
	 */
	public void desDecryptFile(String sourceFile, String destFile, String skey);

	/**
	 * 对文件进行3des解密
	 *
	 * @param sourceFile
	 *            被加密的文件路径
	 *            <p>
	 *            例子:C:/abc.txt
	 * @param destFile
	 *            生成解密后的文件路径
	 *            <p>
	 *            例子:C:/加密后abc.txt
	 * @param skey
	 *            系统生成的168位密钥
	 */
	public void desedeDecryptFile(String sourceFile, String destFile,
      String skey);

	/**
	 * 对文件进行aes解密
	 *
	 * @param sourceFile
	 *            被加密的文件路径
	 *            <p>
	 *            例子:C:/abc.txt
	 * @param destFile
	 *            生成解密后的文件路径
	 *            <p>
	 *            例子:C:/解密后abc.txt
	 * @param skey
	 *            系统生成的256位密钥
	 */
	public void aesDecryptFile(String sourceFile, String destFile, String skey);

	/**
	 * 对文件进行pbe解密
	 *
	 * @param sourceFile
	 *            被加密的文件路径
	 *            <p>
	 *            例子:C:/abc.txt
	 * @param destFile
	 *            生成解密后的文件路径
	 *            <p>
	 *            例子:C:/解密后abc.txt
	 * @param salt
	 *            系统生成的盐
	 * @param password
	 *            自定义口令
	 */
	public void pbeDecryptFile(String sourceFile, String destFile, String salt,
      String password);

	/**
	 * rsa 公钥解密
	 *
	 * @param data
	 *            需要解密的byte[]
	 * @param key
	 *            公钥
	 * @return 解密后结果
	 */
	public byte[] rsaDecryptByPublicKey(byte[] data, String key);

	/**
	 * rsa 私钥解密
	 *
	 * @param data
	 *            需要解密的byte[]
	 * @param key
	 *            私钥
	 * @return 解密后结果
	 */
	public byte[] rsaDecryptByPrivateKey(byte[] data, String key);
}
