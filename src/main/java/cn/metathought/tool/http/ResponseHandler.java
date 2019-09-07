package cn.metathought.tool.http;

import org.apache.http.HttpResponse;

/**
 * Http响应处理接口
 * 
 * @author zbl
 * @2015.04.01
 */
public interface ResponseHandler {

	/**
	 * * 响应处理
	 * 
	 * @param response
	 *            响应response
	 * @return 返回true处理成功,否则处理失败
	 */
	boolean invoke(HttpResponse response);
}
