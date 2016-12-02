package org.seckill.dao;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//高职junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

	//注入Dao实现类依赖
	@Resource
	private SuccessKilledDao successKilledDao;
	
	@Test
	public void insertSuccessKilledTest(){
		int count = successKilledDao.insertSuccessKilled(1000l, 13500000000l);
		System.out.println("updateCount = "+count);
	}
	
	@Test
	public void queryByIdWithSeckillTest(){
		SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1000l, 13500000000l);
		System.out.println(successKilled);
		System.out.println(successKilled.getSecKill());
	}
	
}
