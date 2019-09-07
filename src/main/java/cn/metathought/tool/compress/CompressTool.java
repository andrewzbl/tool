package cn.metathought.tool.compress;

import org.apache.tools.zip.ZipOutputStream;

/**
 * 压缩工具接口
 * 
 * @author zbl
 * @date 2014.08.17
 */
public interface CompressTool {
	/**
	 * 压缩
	 * 
	 * @param compressFiledir
	 *            递归用压缩文件目录，初始和compressFileRootdir一样
	 * @param compressFileRootdir
	 *            需要压缩的文件夹对象，会将source下的文件打成压缩包生成到输出对象中
	 *            <p>
	 *            格式:"D:\\source\\"
	 * @param zipOut
	 *            输出流对象
	 *            <p>
	 *            格式:ZipOutputStream zipOut = new ZipOutputStream(new
	 *            FileOutputStream("D:\\desc\\sample.zip"));
	 * @throws Exception
	 */
	void compress(String compressFiledir, String compressFileRootdir,
        ZipOutputStream zipOut) throws Exception;

	/**
	 * 解压
	 * 
	 * @param decompressfileName
	 *            需要解压的zip路径
	 * @param outputDirectory
	 *            解压后文件的输出路径
	 * @throws Exception
	 */
	void decompress(String decompressfileName, String outputDirectory)
			throws Exception;

	/**
	 * 设置缓冲区大小
	 * 
	 * @param bufSize
	 *            缓冲区大小
	 */
	void setBufSize(int bufSize);
}
