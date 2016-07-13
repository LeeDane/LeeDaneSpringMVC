package com.cn.leedane.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.leedane.model.CommentBean;
import com.cn.leedane.service.CommentService;
import com.cn.leedane.utils.EnumUtil;

@Controller
@RequestMapping("/leedane/comment")
public class CommentController extends BaseController{

	protected final Log log = LogFactory.getLog(getClass());

	//评论service
	private CommentService<CommentBean> commentService;
	
	public void setCommentService(CommentService<CommentBean> commentService) {
		this.commentService = commentService;
	}
	
	/**
	 * 发表评论
	 * @return
	 */
	@RequestMapping("/add")
	public String add(HttpServletRequest request, HttpServletResponse response){
		long start = System.currentTimeMillis();
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(commentService.add(json, user, request));
			long end = System.currentTimeMillis();
			System.out.println("发表评论总计耗时：" +(end - start) +"毫秒");
			printWriter();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", false);
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter();
		return null;
	}

	/**
	 * 获取对象的主要评论列表
	 * @return
	 */
	@RequestMapping("/paging")
	public String paging(HttpServletRequest request, HttpServletResponse response){
		long start = System.currentTimeMillis();
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.put("message", commentService.getCommentsByLimit(json, user, request));
			message.put("isSuccess", true);
			long end = System.currentTimeMillis();
			System.out.println("获取评论列表总计耗时：" +(end - start) +"毫秒");
			printWriter();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", false);
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter();
		return null;
	}
	
	
	/**
	 * 获取每条评论的子评论列表
	 * @return
	 */
	@RequestMapping("/getItemsPaging")
	public String getItemsPaging(HttpServletRequest request, HttpServletResponse response){
		long start = System.currentTimeMillis();
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.put("message", commentService.getOneCommentItemsByLimit(json, user, request));
			message.put("isSuccess", true);
			long end = System.currentTimeMillis();
			System.out.println("获取每条评论的子评论列表总计耗时：" +(end - start) +"毫秒");
			printWriter();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", false);
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter();
		return null;
	}
	
	/**
	 * 获取每一条评论的评论总数
	 * @return
	 */
	@RequestMapping("/getCountByObject")
	public String getCountByObject(HttpServletRequest request, HttpServletResponse response){
		long start = System.currentTimeMillis();
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.put("message", commentService.getCountByObject(json, user, request));
			message.put("isSuccess", true);
			long end = System.currentTimeMillis();
			System.out.println("获取表的行记录评论数量总计耗时：" +(end - start) +"毫秒");
			printWriter();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", false);
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter();
		return null;
	}
	
	/**
	 * 获取用户所有的评论数量
	 * @return
	 */
	@RequestMapping("/getCommentsCountByUser")
	public String getCommentsCountByUser(HttpServletRequest request, HttpServletResponse response){
		long start = System.currentTimeMillis();
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.put("message", commentService.getCountByUser(json, user, request));
			message.put("isSuccess", true);
			long end = System.currentTimeMillis();
			System.out.println("获取用户所有的评论数量总计耗时：" +(end - start) +"毫秒");
			printWriter();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", false);
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter();
		return null;
	}
	
	/**
	 * 删除评论
	 * @return
	 */
	@RequestMapping("/delete")
	public String delete(HttpServletRequest request, HttpServletResponse response) {
		try {
			//{"cid":1, "create_user_id":1}
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(commentService.deleteComment(json, user, request));
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
	
	/**
	 * 更改评论编辑状态
	 * {'can_comment':true, 'table_name':'t_mood', 'table_id': 1},所有参数全部必须
	 * @return
	 */
	@RequestMapping("/updateCommentStatus")
	public String updateCommentStatus(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(commentService.updateCommentStatus(json, user, request));
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
