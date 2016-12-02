package org.seckill.enums;

/**
 * 
 * @author shenzhiqiang
 * @ClassName: SecKillStateEnum 
 * @Description: 使用枚举表述常量数据字段
 * @date 2016年11月2日 下午4:23:42
 */
public enum SecKillStateEnum {
	SUCCESS(1,"秒杀成功"),
	END(0,"秒杀结束"),
	REPEAT_KILL(-1,"重复秒杀"),
	INNER_ERROR(-2,"系统异常"),
	DATE_REWRITE(-3,"数据篡改");

	private int state;
	
	private String stateInfo;

	SecKillStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}
	
	public static SecKillStateEnum stateof(int index){
		for (SecKillStateEnum state: values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}
}
