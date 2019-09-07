package cn.metathought.tool.util.file;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * 条形码操作工具类(要求jdk1.7或以上)
 * 
 * @author zbl
 * @date 2016.02.10
 *
 */
public class BarcodeTool {
	public static final String PNG = "png";

	/**
	 * 生成条形码
	 * 
	 * @param content
	 *            条码内容
	 * @param dst
	 *            生成文件目标地址
	 * @param width
	 *            条码宽度
	 * @param height
	 *            条码高度
	 * @throws IOException
	 */
	public static void QREncode(String content, File dst, int width, int height)
			throws IOException {
		encode(content, dst, BarcodeFormat.QR_CODE, width, height, PNG);
	}

	/**
	 * 生成条形码
	 * 
	 * @param content
	 *            条码内容
	 * @param dst
	 *            生成文件目标地址
	 * @param barcodeFormat
	 *            条码格式，参见BarcodeFormat
	 * @param width
	 *            条码宽度
	 * @param height
	 *            条码高度
	 * @param format
	 *            图片格式
	 * @throws IOException
	 */
	public static void encode(String content, File dst,
			BarcodeFormat barcodeFormat, int width, int height, String format)
			throws IOException {

		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix = null;
		try {
			bitMatrix = new MultiFormatWriter().encode(content, barcodeFormat,
					width, height, hints);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		String filePath = dst.getParent();
		String fileName = dst.getName();
		Path path = FileSystems.getDefault().getPath(filePath, fileName);
		MatrixToImageWriter.writeToPath(bitMatrix, format, path);
	}

	public static void encode(String content, OutputStream os,
			BarcodeFormat barcodeFormat, int width, int height, String format)
			throws IOException {

		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix = null;
		try {
			bitMatrix = new MultiFormatWriter().encode(content, barcodeFormat,
					width, height, hints);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		MatrixToImageWriter.writeToStream(bitMatrix, format, os);
	}

	/**
	 * 对二维码进行解码，返回String类型的数组，元素0为内容，元素1为格式；
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String[] decode(File file) throws Exception {
		return decode(new FileInputStream(file));
	}

	/**
	 * 对二维码进行解码，返回String类型的数组，元素0为内容，元素1为格式；
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String[] decode(String path) throws Exception {
		return decode(new File(path));
	}

	/**
	 * 对二维码进行解码，返回String类型的数组，元素0为内容，元素1为格式；
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static String[] decode(InputStream is) throws Exception {
		BufferedImage image = ImageIO.read(is);
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		Binarizer binarizer = new HybridBinarizer(source);
		BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
		Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
		Result result = new MultiFormatReader().decode(binaryBitmap, hints);// 对图像进行解码
		String[] r = new String[2];
		r[0] = result.getText();
		r[1] = result.getBarcodeFormat().toString();
		return r;
	}
}
