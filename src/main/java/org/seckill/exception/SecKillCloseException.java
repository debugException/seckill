package org.seckill.exception;

/**
 * 
 * @author shenzhiqiang
 * @ClassName: SecKillCloseException 
 * @Description: 秒杀关闭异常
 * @date 2016年11月2日 下午3:05:14
 */
public class SecKillCloseException extends SecKillException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SecKillCloseException(String message) {
		super(message);
	}

	public SecKillCloseException(String message, Throwable cause) {
		super(message, cause);
	}
}
