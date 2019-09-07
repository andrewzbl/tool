package cn.metathought.tool.http;

import org.apache.http.client.methods.HttpPost;

/**
 * Http请求处理接口
 *
 * @author zbl
 * @2015.04.01
 */
public interface RequestHandler {

    /**
     * http请求参数封装回调
     *
     * @return true为传入参数无误, false为传入参数有误
     */
    boolean invoke(HttpPost httppost);
}
