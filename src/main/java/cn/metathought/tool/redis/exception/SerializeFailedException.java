package cn.metathought.tool.redis.exception;

public class SerializeFailedException extends Exception {
	private static final long serialVersionUID = -1853710034218477210L;

	public SerializeFailedException() {
	}

	public SerializeFailedException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
