package com.cn.leedane.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.leedane.model.OperateLogBean;
import com.cn.leedane.service.OperateLogService;
import com.cn.leedane.utils.EnumUtil;

@Controller
@RequestMapping("/leedane/operatelog")
public class OperateLogController extends BaseController{

	protected final Log log = LogFactory.getLog(getClass());
	
	// 操作日志
	@Autowired
	protected OperateLogService<OperateLogBean> operateLogService;
	
	/**
	 * 分页获取用户登录操作日志列表
	 * @return
	 */
	@RequestMapping("/loginPaging")
	public String loginPaging(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(operateLogService.getUserLoginLimit(json, user, request));
			printWriter();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter();
		return null;
	}
}
