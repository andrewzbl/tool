package cn.metathought.tool.util.base;

import java.util.Map;
import lombok.Data;

/**
 * 返回结果封装类(可以封装一些额外的map信息)
 *
 * @author zhoubinglong
 */
@Data
public class WebResultMap extends WebResult {

    Map maps;

    public static WebResultMap getSuccess(Object data, Map maps) {
        return getSuccess(data, 0, null, maps);
    }

    public static WebResultMap getSuccess(Object data, long total, Map maps) {
        return getSuccess(data, total, null, maps);
    }

    public static WebResultMap getSuccess(Object data, long total, String msg, Map maps) {
        WebResultMap result = new WebResultMap();
        result.setData(data);
        result.setTotal(total);
        result.setMsg(msg);
        result.setMaps(maps);
        return result;
    }

    public static WebResultMap getError(int code, Map maps) {
        return getError(code, null, maps);
    }

    public static WebResultMap getError(int code, String msg, Map maps) {
        WebResultMap result = new WebResultMap();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        result.setMaps(maps);
        return result;
    }

    public static WebResultMap getError(Object data, int code, String msg, Map maps) {
        WebResultMap result = new WebResultMap();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        result.setMaps(maps);
        return result;
    }

    public static WebResultMap getSuccess(String msg, Map maps) {
        return getSuccess(null, 0, msg, maps);
    }

}
