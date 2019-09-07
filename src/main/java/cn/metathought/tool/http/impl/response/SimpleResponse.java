package cn.metathought.tool.http.impl.response;

import cn.metathought.tool.http.ResponseHandler;
import lombok.Data;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

/**
 * Http响应处理基类
 * 
 * @author zbl
 * @2015.04.01
 */
@Slf4j
@Data
public class SimpleResponse implements ResponseHandler {
	private HttpResponse response;

	@Override
	public boolean invoke(HttpResponse response) {
		try {
			this.response = response;
			int statusCode = response.getStatusLine().getStatusCode();
			// 判断返回码,若为200则认为请求成功,否则认为请求失败
			if (statusCode == 200) {
				return true;
			}
			return false;
		} catch (Exception e) {
			log.error("httpresponse error", e);
			return false;
		}
	}

}