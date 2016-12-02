package org.seckill.redis;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/redis/spring-redis.xml")
public class RedisTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private JedisPool jedisPool;
	
	@Test
	public void basicOpTest(){
		Jedis jedis = jedisPool.getResource();
		jedis.set("person.001.name", "frank");
		jedis.set("person.001.city", "beijing");
		
		String name = jedis.get("person.001.name");
		String city = jedis.get("person.001.city");
		
		assertEquals("frank",name);
		assertEquals("beijing", city);
		
		jedis.del("person.001.name");
		Boolean result = jedis.exists("person.001.name");
		assertEquals(false, result);
		
		result = jedis.exists("person.001.city");
		assertEquals(true, result);
		
		jedis.close();
	}
}
