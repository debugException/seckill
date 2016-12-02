package org.seckill.dto;

/**
 * 
 * @author shenzhiqiang
 * @ClassName: SecKillResult 
 * @Description: 封装json结果
 * @date 2016年11月3日 下午4:30:08 
 * @param <T>
 */
public class SecKillResult<T> {

	private boolean success;
	
	private T data;
	
	private String error;

	public SecKillResult(boolean success, T data) {
		super();
		this.success = success;
		this.data = data;
	}

	public SecKillResult(boolean success, String error) {
		super();
		this.success = success;
		this.error = error;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
}
