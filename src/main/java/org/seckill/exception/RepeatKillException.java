package org.seckill.exception;

/**
 *
 * @author shenzhiqiang
 * @ClassName: RepeatKillException 
 * @Description: 重复秒杀异常(运行期异常)
 * @date 2016年11月2日 下午3:02:21
 */
public class RepeatKillException extends SecKillException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RepeatKillException(String message) {
		super(message);
	}

	public RepeatKillException(String message, Throwable cause) {
		super(message, cause);
	}

	
}
