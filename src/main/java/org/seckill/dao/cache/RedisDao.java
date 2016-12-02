package org.seckill.dao.cache;

import org.seckill.entity.SecKill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


public class RedisDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final JedisPool jedisPool;
	
	public RedisDao(String ip, int port){
		jedisPool = new JedisPool(ip, port);
	}
	
	private RuntimeSchema<SecKill> schema = RuntimeSchema.createFrom(SecKill.class);
	
	public SecKill getSeckill(long seckillId){
		//redis操作逻辑
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:"+seckillId;
				//redis并没有实现内部序列化操作
				//采用自定义序列化
				byte[] bytes = jedis.get(key.getBytes());
				if (bytes != null) {
					SecKill secKill = schema.newMessage();
					ProtostuffIOUtil.mergeFrom(bytes, secKill, schema);
					return secKill;
				}
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		return null;
	}
	
	public String putSecKill(SecKill secKill){
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:"+secKill.getSeckillId();
				byte[] bytes = ProtostuffIOUtil.toByteArray(secKill, schema, 
						LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
				int timeout = 60 * 60;//缓存一小时
				String result = jedis.setex(key.getBytes(), timeout, bytes);
				return result;
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
