package cn.metathought.tool.http.impl.request;

import cn.metathought.tool.http.RequestHandler;
import java.io.File;
import java.nio.charset.Charset;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

/**
 * 文件参数的Http请求处理类
 * 
 * @author zbl
 * @date 2015.04.01
 */
@Slf4j
public class FileRequest implements RequestHandler {
	// 这个变量名是固定的,接收文件的变量名
	private static final String FILEPARAM = "file";

	private String filePath;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public FileRequest(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public boolean invoke(HttpPost httppost) {
		try {
			FileBody file = new FileBody(new File(this.filePath));
			MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
			reqEntity.setBoundary(null);
			reqEntity.setCharset(Charset.forName("UTF-8"));
			reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			reqEntity.addPart(FILEPARAM, file);
			httppost.setEntity(reqEntity.build());
			return true;
		} catch (Exception e) {
			log.error("httprequest file param error", e);
			return false;
		}
	}

}
