package cn.metathought.tool.compress.impl;

import cn.metathought.tool.compress.CompressTool;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

/**
 * AntZip压缩工具类
 * 
 * @author zbl
 * @date 2014.08.17
 */
public class AntZip implements CompressTool {
	private int bufSize;
	private byte[] buf;

	public AntZip() {
		this(1024);
	}

	public AntZip(int bufSize) {
		this.bufSize = bufSize;
		this.buf = new byte[this.bufSize];
	}

	@Override
	public void compress(String compressFiledir, String compressFileRootdir,
			ZipOutputStream zipOut) throws Exception {
		FileInputStream fis;
		try {
			File cFile = new File(compressFiledir);
			File[] files = cFile.listFiles();
			int readedBytes = 0;
			for (File file : files) {
				if (file.isDirectory()) {
					compress(file.getPath(), compressFileRootdir, zipOut);
				} else {
					fis = new FileInputStream(file);
					if (file.getName().endsWith(".zip"))
						continue;
					String filePath = file.getAbsolutePath().replace("\\", "/");
					zipOut.putNextEntry(new ZipEntry(filePath.substring(
							compressFileRootdir.length(), filePath.length())));

					while ((readedBytes = fis.read(buf)) > 0) {
						zipOut.write(buf, 0, readedBytes);
					}
					zipOut.setEncoding("GBK");
					zipOut.closeEntry();
					fis.close();
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void decompress(String unZipfileName, String outputDirectory)
			throws Exception {
		File file; // 输出文件
		FileOutputStream fileOut = null; // 压缩文件输出流
		InputStream inputStream = null; // 文件输入流
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(unZipfileName, "GBK");
			int readedBytes = 0;
			for (Enumeration entries = zipFile.getEntries(); entries
					.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				file = new File(outputDirectory + File.separator
						+ entry.getName());
				if (entry.isDirectory()) {
					file.mkdirs();
				} else {
					File parent = file.getParentFile();
					if (parent != null) {
						if (!parent.exists()) {
							parent.mkdirs();
						}
					}
					inputStream = zipFile.getInputStream(entry);
					fileOut = new FileOutputStream(file);
					while ((readedBytes = inputStream.read(this.buf)) > 0) {
						fileOut.write(this.buf, 0, readedBytes);
					}
					fileOut.close();
					inputStream.close();
				}
			}
			zipFile.close();
		} catch (Exception e) {
			throw e;
		} finally {
			if (fileOut != null) {
				fileOut.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
			if (zipFile != null) {
				zipFile.close();
			}
		}
	}

	/**
	 * 设置缓冲区大小
	 * 
	 * @param bufSize
	 *            缓冲区大小
	 */
	@Override
	public void setBufSize(int bufSize) {
		this.bufSize = bufSize;
	}
}
