package cn.metathought.tool.http.impl.request;

import cn.metathought.tool.http.RequestHandler;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;

/**
 * 带参数的Http请求处理类
 *
 * @author zbl
 * @2015.04.01
 */
@Slf4j
public class ParamRequest implements RequestHandler {

    // request请求参数集合
    private List<NameValuePair> paramList;

    public ParamRequest(List<NameValuePair> paramList) {
        this.paramList = paramList;
    }

    public List<NameValuePair> getParamList() {
        return paramList;
    }

    public void setParamList(List<NameValuePair> paramList) {
        this.paramList = paramList;
    }

    @Override
    public boolean invoke(HttpPost httppost) {
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (NameValuePair param : this.paramList) {
                params.add(param);
            }
            UrlEncodedFormEntity urlEntity = new UrlEncodedFormEntity(params,
                "UTF-8");
            httppost.setEntity(urlEntity);
            return true;
        } catch (Exception e) {
            log.error("httprequest map param error", e);
            return false;
        }
    }
}
