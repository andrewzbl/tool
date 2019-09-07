package cn.metathought.tool.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * 文件操作工具类
 * 
 * @author zbl
 * @date 2013.12.23
 */
@Slf4j
public class FileTool {
	// 记录原始文件路径
	private static String originPath = "";

	public static String getOriginPath() {
		return originPath;
	}

	/**
	 * 删除文件夹下所有内容(不包括当前文件夹)
	 * <p>
	 * 注:path为文件夹地址，删除的是文件夹下所有内容，不包括当前文件夹
	 * 
	 * @param path
	 *            文件夹路径
	 * @return 返回true表示删除文件成功，否则失败
	 */
	public static boolean delAllFile(String path) {
		try {
			if (!StringUtils.isEmpty(path)) {
				File uDir = new File(path);
				if (null == uDir || !uDir.exists()) {
					return false;
				}
				String[] tempList = uDir.list();
				File temp = null;
				for (int i = 0; i < tempList.length; i++) {
					temp = new File(path + File.separator + tempList[i]);
					if (temp.isFile()) {
						temp.delete();
						// 解决因JVM回收机制的延迟导致的文件无法删除问题
						// System.gc();
					}
					if (temp.isDirectory()) {
						delAllFile(path + File.separator + tempList[i]);// 先删除文件夹里面的文件
						delFolder(path + File.separator + tempList[i]);// 再删除空文件夹
					}
				}
				return true;
			}
			return false;
		} catch (Exception e) {
			log.error("删除文件夹下所有内容", e);
			return false;
		}
	}

	/**
	 * 删除文件夹下所有内容(包括当前文件夹)
	 * <p>
	 * 注:path为文件夹地址，删除的是文件夹下所有内容，包括当前文件夹
	 * 
	 * @param path
	 *            文件夹路径
	 * @return 返回true表示删除文件成功，否则失败
	 */
	public static boolean delFolder(String path) {
		try {
			// 首先删除完里面所有内容
			if (delAllFile(path)) {
				// 最后删除空文件夹
				File myFilePath = new File(path);
				myFilePath.delete();
				return true;
			}
			return false;
		} catch (Exception e) {
			log.error("删除文件夹下所有内容", e);
			return false;
		}
	}

	/**
	 * 将源文件复制到目标文件中
	 * 
	 * @param sourceFile
	 *            源文件
	 * @param targetFile
	 *            目标文件路径
	 * @throws IOException
	 */
	public static void copyFile(File sourceFile, String targetFile)
			throws Exception {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			outBuff.flush();
		} catch (Exception e) {
			log.error("拷贝文件失败", e);
			throw e;
		} finally {
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}

	/**
	 * 获取单个文件的MD5值
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileMD5(File file) {
		if (null == file || !file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			log.error("获取文件MD5值失败", e);
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}

	/**
	 * 获取文件夹中文件的MD5值
	 * 
	 * @param file
	 * @param listChild
	 *            true递归子目录中的文件
	 * @return
	 */
	public static Map<String, String> getDirMD5(File file, boolean listChild) {
		if (!file.isDirectory()) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		String md5;
		File files[] = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory() && listChild) {
				map.putAll(getDirMD5(f, listChild));
			} else {
				md5 = getFileMD5(f);
				if (md5 != null) {
					map.put(f.getPath(), md5);
				}
			}
		}
		return map;
	}

	/**
	 * 根据某层文件名递归寻找文件
	 * 
	 * @param file
	 *            待寻找根文件夹
	 * @param level
	 *            目录层级
	 * @param pathName
	 *            待寻找层文件名
	 */
	public static void findFile(File file, int level, String pathName) {
		int myLevel = 0;
		File[] fileArray = file.listFiles();
		if (fileArray != null) {
			for (int i = 0; i < fileArray.length; i++) {
				findFileByLevel(fileArray[i], myLevel, level, pathName);
			}
		}
	}

	private static synchronized void findFileByLevel(File file, int myLevel,
			int level, String pathName) {
		myLevel++;
		File[] fileArray = file.listFiles();
		if (fileArray.length > 0) {
			for (int i = 0; i < fileArray.length; i++) {
				if (myLevel == level) {
					String path = fileArray[i].getPath();
					if (path.indexOf(pathName) != -1) {
						FileTool.originPath = path;
					}
				} else {
					findFileByLevel(fileArray[i], myLevel, level, pathName);
				}
			}
		}
	}

	/**
	 * 生成最终文件相对路径
	 * <p>
	 * 格式:年/月/日/时/分/文件id
	 * 
	 * @param fileId
	 *            文件fid
	 * @return
	 */
	public static String genRelativeFilePath(String fileId) {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int date = c.get(Calendar.DATE);
		int hour = c.get(Calendar.HOUR);
		int minute = c.get(Calendar.MINUTE);
		String filePath = year + File.separator + month + File.separator + date
				+ File.separator + hour + File.separator + minute
				+ File.separator + fileId;
		return filePath;
	}

	/**
	 * 进行文件续传
	 * 
	 * @param sourceFile
	 *            上传的片段文件
	 * @param destPath
	 *            已经上传的文件
	 * @throws Exception
	 */
	public static void resumeTransFile(File sourceFile, String destPath)
			throws Exception {
		InputStream is = null;
		RandomAccessFile oSavedFile = null;
		try {
			is = new FileInputStream(sourceFile);
			byte buffer[] = new byte[1024];
			oSavedFile = new RandomAccessFile(destPath, "rw");
			long startPosL = oSavedFile.getFilePointer();
			oSavedFile.seek(startPosL);

			int len = 0;
			while (len != -1) {
				oSavedFile.write(buffer, 0, len);
				len = is.read(buffer, 0, 1024);
			}
		} catch (Exception e) {
			log.error("文件续传出错", e);
			throw e;
		} finally {
			if (is != null)
				is.close();
			if (oSavedFile != null)
				oSavedFile.close();
		}
	}
}
