package cn.metathought.tool.redis.exception;

public class RedisOperationException extends Exception {
	private static final long serialVersionUID = 8477193403161391251L;

	public RedisOperationException() {
	}

	public RedisOperationException(String msg) {
		super(msg);
	}
}
