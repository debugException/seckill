package org.seckill.exception;

/**
 * 
 * @author shenzhiqiang
 * @ClassName: SecKillException 
 * @Description: 所有秒杀相关异常 
 * @date 2016年11月2日 下午3:06:22
 */
public class SecKillException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SecKillException(String message) {
		super(message);
	}

	public SecKillException(String message, Throwable cause) {
		super(message, cause);
	}
}
