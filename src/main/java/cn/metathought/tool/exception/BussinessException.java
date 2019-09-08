package cn.metathought.tool.exception;

/**
 * 业务类异常，不需要发邮件的
 */
public class BussinessException extends RuntimeException{
    public BussinessException(String msg) {
        super(msg);
    }
}
