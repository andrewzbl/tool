package cn.metathought.tool.util.base;

import lombok.Data;

/**
 * 返回结果封装类
 *
 * @author zhoubinglong
 */
@Data
public class WebResult {

    public static int SUCCESS = 0;
    public static int FAIL = 1;
    /**
     * 未绑定用户
     */
    public static int UNLOGIN = 401;
    /**
     * 未关注公众号
     */
    public static int UNSUBSCRIBE = 402;
    /**
     * 微信未登录
     */
    public static int UNAUTH = 403;

    public static int EXCEPTION = 500;

    private int code = 0;
    private String msg = "";
    private Object data;
    private long total;
    private boolean success = true;
    private boolean notifyCompleted;

    public static WebResult getSuccess(String msg) {
        return getSuccess(null, 0, msg);
    }

    public static WebResult getSuccess(Object data) {
        return getSuccess(data, 0, null);
    }

    public static WebResult getSuccess(Object data, long total) {
        return getSuccess(data, total, null);
    }

    public static WebResult getSuccess(Object data, long total, String msg) {
        WebResult result = new WebResult();
        result.setData(data);
        result.setTotal(total);
        result.setMsg(msg);
        return result;
    }

    public static WebResult getError(int code) {
        return getError(code, null);
    }

    public static WebResult getError(int code, String msg) {
        WebResult result = new WebResult();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static WebResult getError(Object data, int code, String msg) {
        WebResult result = new WebResult();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}
