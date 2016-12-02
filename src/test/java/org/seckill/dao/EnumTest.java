package org.seckill.dao;


/**
 * 
 * @author shenzhiqiang
 * @ClassName: EnumTest 
 * @Description: 枚举用法详解
 * @date 2016年11月2日 下午6:43:43
 */
public class EnumTest {

	/**
	 * 普通枚举
	 */
	public enum ColorEnum{
		red,green,yellow,blue;
	}
	
	/**
	 * 枚举像普通的类一样可以添加属性和方法，可以为它添加静态和非静态的属性或方法
	 */
	public enum SeasonEnum{
		//注：枚举写在最前面，否则编译出错
		spring,summer,autumn,winter;
		
		private final static String POSITION_STRING = "test";
		
		public static SeasonEnum getSeason(){
			if ("test".equals(POSITION_STRING)) {
				return spring;
			}else {
				return winter;
			}
		}
	}
	
	/**
	 * 性别
	 * 
	 * 实现带有构造器的枚举
	 */
	public enum Gender{
		//通过括号赋值，而且必须带有一个参构造器和一个属性跟方法，否则编译出错
		//赋值必须都赋值或者都不赋值，不能一部分赋值一部分不赋值；如果不赋值则不能写构造器，赋值编译也出错
		MAN("MAN"),WOMEN("WOMEN");
		
		private final String value;

		//构造器默认也只能是private，从而保证构造函数只能在内部使用
		Gender(String value){
			this.value = value;
		}

		public String getValue() {
			return value;
		} 
	}
	
	/**
	 * 订单状态
	 * 
	 * 实现带有抽象方法的枚举
	 */
	public enum OrderState{
		/** 已取消 */
		CANCEL{public String getName(){return "已取消";}},
		/** 待审核 */
		WAITCONFIRM{public String getName(){return "待审核";}},
		/** 等待付款 */
		WAITPAYMENT{public String getName(){return "等待付款";}},
		/** 正在配货 */
		ADMEASUREPRODUCT{public String getName(){return "正在配货";}},
		/** 等待发货 */
		WAITDELIVER{public String getName(){return "等代发货";}},
		/** 已发货 */
		DELIVERED{public String getName(){return "已发货";}},
		/** 已收货 */
		RECEIVED{public String getName(){return "已收货";}};
		
		public abstract String getName();
	}
	
	public static void main(String[] args) {
		//枚举是一种类型，用于定义变量，以限制变量的赋值，赋值时通过“枚举名.值”取得枚举中的值
		ColorEnum colorEnum = ColorEnum.blue;
		switch (colorEnum) {
		case red:
			System.out.println("color is red");
			break;
		case green:
			System.out.println("color is green");
			break;
		case yellow:
			System.out.println("color is yellow");
			break;
		case blue:
			System.out.println("color is blue");
			break;
		}
		
		//遍历枚举
		System.out.println("遍历colorEnum枚举中的值");
		for(ColorEnum color:ColorEnum.values()){
			System.out.println(color);
		}
		
		//遍历枚举的个数
		System.out.println("ColorEnum枚举中的值有"+ColorEnum.values().length+"个");
		
		//获取枚举的索引位置，默认从0开始
		System.out.println(ColorEnum.red.ordinal());
		System.out.println(ColorEnum.green.ordinal());
		System.out.println(ColorEnum.yellow.ordinal());
		System.out.println(ColorEnum.blue.ordinal());
		
		//枚举默认实现了java.lang.Comparable接口
		System.out.println(ColorEnum.red.compareTo(ColorEnum.blue));
		
		System.out.println("===============");
		System.out.println("季节为："+SeasonEnum.getSeason());
		
		System.out.println("===============");
		for(Gender gender: Gender.values()){
			System.out.println(gender.value);
		}
		
		System.out.println("===============");
		for (OrderState order: OrderState.values()) {
			System.out.println(order.getName());
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
