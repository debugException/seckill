package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.cache.RedisDao;
import org.seckill.entity.SecKill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {

	private long id = 1001;
	
	@Autowired
	private RedisDao redisDao;
	
	@Autowired
	private SecKillDao secKillDao;
	
	@Test
	public void testSecKill() throws Exception{
		SecKill secKill = redisDao.getSeckill(id);
		if (secKill == null) {
			secKill = secKillDao.queryById(id);
			if (secKill != null) {
				String result = redisDao.putSecKill(secKill);
				System.out.println(result);
				SecKill secKill2 = redisDao.getSeckill(id);
				System.out.println(secKill2);
			}
		}else {
			System.out.println("从redis缓存服务器中读取："+secKill);
		}
	}
}
