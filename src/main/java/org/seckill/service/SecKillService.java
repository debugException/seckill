package org.seckill.service;

import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SecKillExecution;
import org.seckill.entity.SecKill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SecKillCloseException;
import org.seckill.exception.SecKillException;

public interface SecKillService {

	/**
	 * 查询所有秒杀记录
	 * @return
	 */
	List<SecKill> getSecKillList();
	
	/**
	 * 查询单个秒杀记录
	 * @param seckillId
	 * @return
	 */
	SecKill getById(long seckillId);
	
	/**
	 * 秒杀开启时输出秒杀接口地址
	 * 否则输出系统时间和秒杀时间
	 * @param seckillId
	 * @return
	 */
	Exposer exportSecKillUrl(long seckillId);
	
	/**
	 * 执行秒杀操作
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 */
	SecKillExecution executeSecKill(long seckillId, long userPhone, String md5)
			throws SecKillException,SecKillCloseException,RepeatKillException;
	
	/**
	 * 执行秒杀操作by 存储过程
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 */
	SecKillExecution executeSecKillProcedure(long seckillId, long userPhone, String md5)
			throws SecKillException,SecKillCloseException,RepeatKillException;
}
