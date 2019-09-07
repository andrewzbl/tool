package cn.metathought.tool.http.impl.request;

import cn.metathought.tool.http.RequestHandler;
import java.nio.charset.Charset;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

/**
 * 字符串类型的Http请求
 *
 * @author zbl
 * @date 2015.04.01
 */
@Slf4j
public class StringRequest implements RequestHandler {

    private String param;

    public StringRequest(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public boolean invoke(HttpPost httppost) {
        try {
            ContentType t = ContentType.create("text/plain",
                Charset.forName("UTF-8"));
            StringEntity entity = new StringEntity(this.param, t);
            httppost.setEntity(entity);
            return true;
        } catch (Exception e) {
            log.error("httprequest string param error", e);
            return false;
        }
    }
}
