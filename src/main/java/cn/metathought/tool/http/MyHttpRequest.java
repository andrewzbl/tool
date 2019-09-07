package cn.metathought.tool.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * 通过httpclient发送Http请求公共基类
 *
 * @author zbl
 * @2015.04.01
 */
@Slf4j
public class MyHttpRequest {

    /**
     * 发送http请求
     *
     * @param url 请求地址
     * @param reqHandler 请求处理函数
     * @param resHandler 响应处理函数
     * @return 返回true, 请求成功;返回false,请求失败
     */
    public static boolean sendRequest(String url, RequestHandler reqHandler,
        ResponseHandler resHandler) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
        try {
            reqHandler.invoke(httppost);
            // 提交请求
            HttpResponse response = httpClient.execute(httppost);
            // 请求结果处理回调函数
            return resHandler.invoke(response);
        } catch (Exception e) {
            log.error("httprequest error", e);
            return false;
        }
    }
}
