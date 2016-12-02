package org.seckill.web;

import java.util.Date;
import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SecKillExecution;
import org.seckill.dto.SecKillResult;
import org.seckill.entity.SecKill;
import org.seckill.enums.SecKillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SecKillCloseException;
import org.seckill.exception.SecKillException;
import org.seckill.service.SecKillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/seckill")
public class SecKillController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SecKillService secKillService;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(Model model) {
		List<SecKill> list = secKillService.getSecKillList();
		model.addAttribute("list", list);
		return "list";
	}

	@RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
		if (seckillId == null) {
			return "redirect:/seckill/list";
		}
		SecKill secKill = secKillService.getById(seckillId);
		if (secKill == null) {
			return "forward:/seckill/list";
		}
		model.addAttribute("secKill", secKill);
		return "detail";
	}
	
	@RequestMapping(value="/{seckillId}/exposer", method = RequestMethod.POST,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public SecKillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId){
		SecKillResult<Exposer> result;
		try {
			Exposer exposer = secKillService.exportSecKillUrl(seckillId);
			result = new SecKillResult<Exposer>(true, exposer);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result = new SecKillResult<Exposer>(false, e.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value="/{seckillId}/{md5}/execution", method = RequestMethod.POST,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public SecKillResult<SecKillExecution> execute(@PathVariable("seckillId") Long seckillId,
			@PathVariable("md5") String md5, @CookieValue(value="killPhone", required = false) Long phone){
		if (phone == null) {
			return new SecKillResult<SecKillExecution>(false, "未注册");
		}
		try {
			SecKillExecution execution = secKillService.executeSecKillProcedure(seckillId, phone, md5);
			return new SecKillResult<SecKillExecution>(true, execution);
		} catch (RepeatKillException e) {
			SecKillExecution execution = new SecKillExecution(seckillId, SecKillStateEnum.REPEAT_KILL);
			return new SecKillResult<SecKillExecution>(true, execution);
		}catch (SecKillCloseException e) {
			SecKillExecution execution = new SecKillExecution(seckillId, SecKillStateEnum.END);
			return new SecKillResult<SecKillExecution>(true, execution);
		}catch (SecKillException e) {
			SecKillExecution execution = new SecKillExecution(seckillId, SecKillStateEnum.INNER_ERROR);
			return new SecKillResult<SecKillExecution>(true, execution);
		}
	}
	
	@RequestMapping(value="/time/now", method= RequestMethod.GET)
	@ResponseBody
	public SecKillResult<Long> time(){
		Date now = new Date();
		return new SecKillResult<Long>(true, now.getTime());
	}
}
