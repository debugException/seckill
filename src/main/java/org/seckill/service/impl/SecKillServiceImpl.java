
package org.seckill.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SecKillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SecKillExecution;
import org.seckill.entity.SecKill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SecKillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SecKillCloseException;
import org.seckill.exception.SecKillException;
import org.seckill.service.SecKillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

@Service
public class SecKillServiceImpl implements SecKillService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//注入Service依赖
	@Autowired
	private SecKillDao secKillDao;
	
	@Autowired
	private SuccessKilledDao successKilledDao;
	
	@Autowired
	private RedisDao redisDao;
	
	//md5颜值字符串，用户混淆MD5
	private final String slat ="sadf&(&(*&*(df9s979a(*()*()*&^^%%&";
	
	@Override
	public List<SecKill> getSecKillList() {
		return secKillDao.queryAll(0, 4);
	}

	@Override
	public SecKill getById(long seckillId) {
		return secKillDao.queryById(seckillId);
	}

	@Override
	public Exposer exportSecKillUrl(long seckillId) {
		//优化点：缓存优化
		//1：访问redis
		SecKill secKill = redisDao.getSeckill(seckillId);
		if (secKill == null) {
			//2：访问数据库
			secKill = secKillDao.queryById(seckillId);
			if (secKill == null) {
				return new Exposer(false, seckillId);
			}else {
				//3：放入redis
				redisDao.putSecKill(secKill);
			}
		}

		Date startTime = secKill.getStartTime();
		Date endTime = secKill.getEndTime();
		Date nowTime = new Date();
		if (nowTime.getTime()<startTime.getTime() || nowTime.getTime()>endTime.getTime()) {
			return new Exposer(false, seckillId, nowTime.getTime(),startTime.getTime(),endTime.getTime());
		}
		String md5 = getMD5(seckillId);
		
		return new Exposer(true, md5, seckillId);
	}
	
	private String getMD5(long seckillId){
		String base = seckillId+"/"+slat;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}

	@Override
	@Transactional
	/**
	 * 使用注解控制事务方法的优点：
	 * 	1：开发团队达成一致约定，明确标注事物方法的编程风格；
	 *  2：保证事物方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部；
	 *  3：不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制
	 */
	public SecKillExecution executeSecKill(long seckillId, long userPhone,
			String md5) throws SecKillException, SecKillCloseException,
			RepeatKillException {
		if (md5 == null || !md5.equals(getMD5(seckillId))) {
			throw new SecKillException("seckill data rewrite");
		}
		//执行秒杀逻辑：减库存+记录购买行为
		Date nowTime = new Date();
		try {
			//记录秒杀行为
			int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
			if (insertCount<=0) {
				//重复秒杀
				throw new RepeatKillException("seckill repeated");
			}else {
				//减库存，热点商品竞争
				int updateCount = secKillDao.reduceNumber(seckillId, nowTime);
				if (updateCount<=0) {
					throw new SecKillCloseException("seckill is closed");
				}else {
					//秒杀成功
					SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
					return new SecKillExecution(seckillId, SecKillStateEnum.SUCCESS,successKilled);
				}
			}
			
		} catch (SecKillCloseException e1) {
			throw e1;
		}catch (RepeatKillException e2) {
			throw e2;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new SecKillException("seckill inner error: "+e.getMessage());
		}
	}

	/**
	 * 调用存储过程执行秒杀
	 */
	@Override
	public SecKillExecution executeSecKillProcedure(long seckillId, long userPhone, String md5) {
		if (md5 == null || !md5.equals(getMD5(seckillId))) {
			return new SecKillExecution(seckillId, SecKillStateEnum.DATE_REWRITE);
		}
		Date killTime = new Date();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("seckillId", seckillId);
		map.put("phone", userPhone);
		map.put("killTime",killTime);
		map.put("result", null);
		try {
			secKillDao.killByProcedure(map);
			int result = MapUtils.getInteger(map, "result", -2);
			if (result == 1) {
				SuccessKilled  sk = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
				return new SecKillExecution(seckillId, SecKillStateEnum.SUCCESS, sk);
			}else{
				return new SecKillExecution(seckillId, SecKillStateEnum.stateof(result));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new SecKillExecution(seckillId, SecKillStateEnum.INNER_ERROR);
		}
	}

}
