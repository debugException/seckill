package org.seckill.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SecKill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 配置spring和junit整合，junit启动时加载springIOC容器
 * spring-test,junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SecKillDaoTest {

	//注入Dao实现类依赖
	@Resource
	private SecKillDao secKillDao;
	
	@Test
	public void testQueryById() throws Exception{
		long id = 1000;
		SecKill secKill = secKillDao.queryById(id);
		System.out.println(secKill.getName());
		System.out.println(secKill);
	}
	
	@Test
	public void testQueryAll() throws Exception{
		/**
		 * org.mybatis.spring.MyBatisSystemException: nested exception is org.apache.ibatis.binding.BindingException: 
		 * Parameter 'offset' not found. Available parameters are [1, 0, param1, param2]
		 * 
		 * java没有保存形参的记录
		 * 
		 */
		List<SecKill> list = secKillDao.queryAll(0, 100);
		for(SecKill secKill :list){
			System.out.println(secKill);
		}
	}
	
	@Test
	public void testReduceNumber() throws Exception{
		Date killTime = new Date();
		int count = secKillDao.reduceNumber(1000l, killTime);
		System.out.println("updateCount = "+count);
	}

}
