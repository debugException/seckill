//存放主要交互逻辑
var seckill = {
	//封装秒杀相关ajax的url
		URL:{
			now:function(){
				return "/seckill/seckill/time/now";
			},
			exposer:function(seckillId){
				return "/seckill/seckill/"+seckillId+"/exposer";
			},
			execution:function(seckillId,md5){
				return "/seckill/seckill/"+seckillId+"/"+md5+"/execution";
			}
	},
	//处理秒杀逻辑
	handleSecKill: function(seckillId, node){
		node.hide().html("<button class='btn btn-primary btn-lg' id='killBtn'>开始秒杀</button>");//按钮
		$.post(seckill.URL.exposer(seckillId),{},function(result){
			console.log(result);
			if(result && result['success']){
				var exposer = result['data'];
				console.log(exposer);
				if(exposer['exposed']){
					//开启秒杀
					var md5 = exposer['md5'];
					var killUrl = seckill.URL.execution(seckillId,md5);
					console.log("killUrl = "+killUrl);
					//只绑定一次点击事件
					$("#killBtn").one("click",function(){
						//绑定执行秒杀请求操作
						//1:先禁用按钮
						$(this).addClass("disabled");
						//2:发送秒杀请求
						$.post(killUrl,{},function(result){
							if(result && result['success']){
								var killResult=result['data'];
								var state = killResult['state'];
								var stateInfo = killResult['stateInfo'];
								//3:显示秒杀结果
								node.html("<span class='label label-success'>"+stateInfo+"</span>");
							}else{
								console.log("result = "+result);
							}
						});
					});
					node.show();
				}else{
					var now = exposer['now'];
					var start = exposer['start'];
					var end = exposer['end'];
					//重新计算计时逻辑
					seckill.countdown(seckillId, now, start, end);
				}
			}else{
				console.log("result: "+result);
			}
		});
	},
	//验证手机号
	validatePhone: function(phone){
		if(phone && phone.length==11 &&!isNaN(phone)){
			return true;
		}else{
			return false;
		}
	},
	countdown: function(seckillId, nowTime, startTime, endTime){
		var seckillBox = $("#seckill-box");
		if(nowTime > endTime){
			seckillBox.html("秒杀结束！");
		}else if(nowTime < startTime){
			//秒杀未开始，计时事件绑定
			var killTime = new Date(startTime+ 1000);
			seckillBox.countdown(killTime, function(event){
				var format = event.strftime("秒杀计时：%D天 %H时 %M分 %S秒");
				seckillBox.html(format);
			}).on("finish.countdown",function(){
				//获取秒杀地址，控制显示逻辑，执行秒杀
				seckill.handleSecKill(seckillId, seckillBox);
			});
		}else{//秒杀开始
			seckill.handleSecKill(seckillId, seckillBox);
		}
	},
	//详情页秒杀逻辑
	detail:{
		//详情页初始化
		init:function(params){
			//用户手机验证和登录，计时交互
			//在cookie中查找手机号
			var killPhone = $.cookie("killPhone");
			
			//验证手机号
			if(!seckill.validatePhone(killPhone)){
				//绑定手机号
				//控制输出
				var killPhoneModal = $("#killPhoneModal");
				killPhoneModal.modal({
					show:true,
					backdrop:'static',//禁止位置关闭
					keyboard:false //关闭鼠标事件
				});
				
				$("#killPhoneBtn").click(function(){
					var inputPhone = $("#killPhoneKey").val();
					if(seckill.validatePhone(inputPhone)){
						//电话写入到cookie
						$.cookie('killPhone',inputPhone,{expires:7, path:"/seckill"});
						//刷新页面
						window.location.reload();
					}else{
						$("#killPhoneMessage").hide().html('<label class="label label-danger">手机号错误！</label>').show(300);
					}
				});
			}
			
			var startTime = params['startTime'];
			var endTime = params['endTime'];
			var seckillId = params['seckillId'];
			
			$.get(seckill.URL.now(),{},function(result){
				if(result && result['success']){
					var nowTime = result['data'];
					//倒计时，时间交互
					seckill.countdown(seckillId, nowTime, startTime, endTime);
				}else{
					console.log("result: "+result);
				}
			});
		}
	}
}