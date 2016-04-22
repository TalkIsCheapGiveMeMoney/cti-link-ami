package com.tinet.ctilink.ami.formattool;

/**
 * @Title AmiStaticParameters.java
 * @Package com.tinet.ccic.ami.formattool
 * @author 罗尧   Email:j2ee.xiao@gmail.com
 * @category AMI模块通道变量名的static final设置，统一管理
 * @since 创建时间：2011-10-24 下午05:36:20
 * @serial 所有和AMI相关的final常量定义
 */
public class AmiStaticParameters {
	
	/*
	 * Ami模块所产生的通道变量的设置
	 */
	/**咨询取消时，设定参数*/
	public static final String CONSULT_CANCEL="10001";
	
	
	/*
	 * 后台登陆验证电话号码时候用到的常量
	 */
	/**check in :NUMBER_FORMAT_ERROR 号码格式不正确*/
	public static final String NUMBER_FORMAT_ERROR="1";
	
	/**check in :NUMBER_IN_USE 号码已经被使用*/
	public static final String NUMBER_IN_USE="2";
	
	/**check in :NUMBER_NOT_EXIST 分机号码不存在*/
	public static final String NUMBER_NOT_EXIST="3";
	
	public static final String SUCCESS="0";
	
	public static final String NUMBER_NOT_ROUTED = "4";
}
