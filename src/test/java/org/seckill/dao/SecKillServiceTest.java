package org.seckill.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SecKillExecution;
import org.seckill.entity.SecKill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SecKillCloseException;
import org.seckill.service.SecKillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SecKillServiceTest {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//注入service
	@Autowired
	private SecKillService secKillService;
	
	@Test
	public void testGetSecKillList() throws Exception{
		List<SecKill> list = secKillService.getSecKillList();
		logger.info("list={}",list);
	}
	
	@Test
	public void testGetById() throws Exception{
		long id = 1000;
		SecKill secKill = secKillService.getById(id);
		logger.info("secKill={}",secKill);
	}
	
	@Test
	public void testExportSecKillUrl() throws Exception{
		long id = 1000;
		Exposer exposer = secKillService.exportSecKillUrl(id);
		logger.info("exposer={}",exposer.toString());
		/**
		 * Exposer [
		 * 	exposed=true,
		 *  md5=826c75177583e2a89382ebe60cf81ab3,
		 *  seckillId=1000,
		 *  now=0, start=0, end=0]

		 */
	}
	
	@Test
	public void testExecuteSecKill() throws Exception{
		long id = 1001;
		long phone = 13500000000l;
		String md5 = "826c75177583e2a89382ebe60cf81ab3";
		try {
			SecKillExecution secKillExecution = secKillService.executeSecKill(id, phone, md5);
			logger.info("secKillExecution={}", secKillExecution);
		} catch (RepeatKillException e) {
			logger.error(e.getMessage());
		} catch (SecKillCloseException e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 合并上边两个方法 测试秒杀逻辑
	 * @throws Exception
	 */
	@Test
	public void testSecKillLogic() throws Exception{
		long id = 1001;
		Exposer exposer = secKillService.exportSecKillUrl(id);
		if (exposer.isExposed()) {
			logger.info("exposer={}",exposer.toString());
			long phone = 13500000000l;
			String md5 = exposer.getMd5();
			try {
				SecKillExecution secKillExecution = secKillService.executeSecKill(id, phone, md5);
				logger.info("secKillExecution={}", secKillExecution);
			} catch (RepeatKillException e) {
				logger.error(e.getMessage());
			} catch (SecKillCloseException e) {
				logger.error(e.getMessage());
			}
		}else{
			//秒杀未开启
			logger.warn("exposer={}",exposer);
		}
	}
	
	@Test
	public void executeSecKillProcedureTest(){
		long seckillId = 1001;
		long phone = 13711111111l;
		Exposer exposer = secKillService.exportSecKillUrl(seckillId);
		if (exposer.isExposed()) {
			String md5 = exposer.getMd5();
			SecKillExecution secKillExecution = secKillService.executeSecKillProcedure(seckillId, phone, md5);
			logger.info(secKillExecution.getStateInfo());
		}
	}
}
