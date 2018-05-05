package com.cloudyoung.common.utils;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.alibaba.dubbo.common.utils.NetUtils;
import com.cloudyoung.common.enums.PlatformNameEnum;
import com.cloudyoung.common.model.LogModel;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.cloudyoung.common.model.LogErrorModel;

/**
 * Description: LogUtil.java
 * All Rights Reserved.
 * @version 1.0  2014-12-24 下午3:59:15  by 王星皓（wangxh@cloud-young.com）创建
 */
public class LogUtil implements Serializable{

	private static final long serialVersionUID = 3993187523235317784L;
	private static final Logger LOGGER = LogManager.getLogger(LogUtil.class);


	/**
	 *
	 * Description: info 日志消息记录
	 *
	 * @Version1.0 2014- 6-4 下午11:32:06 by 王星皓（ wangxh@cloud-young.com）创建
	 * @param logger
	 *            日志对象
	 * @param message
	 *            记录日志的消息 例如:<code>xxxxxx{0}xxxxx{1} </code>
	 * @param args
	 *            具体消息参数 用于替代{0}{1}
	 */
	@Deprecated
	public static void info(Logger logger, String message, Object... args) {
		try {
			logger.info(LogUtil.messageFormat(message, args));
		} catch (Exception e) {
		}
	}


	@Deprecated
	public static void warn(Logger logger, String message, Object... args) {
		try {
			logger.warn(LogUtil.messageFormat(message, args));
		} catch (Exception e) {
		}
	}


	@Deprecated
	public static void debug(Logger logger, String message, Object... args) {
		try {
			logger.debug(LogUtil.messageFormat(message, args));
		} catch (Exception e) {
		}
	}

	/**
	 *
	 * Description: error 日志消息记录
	 *
	 * @Version1.0 2014- 6-4 下午11:35:32 by 王星皓（ wangxh@cloud-young.com）创建
	 * @param logger
	 *            日志对象
	 * @param exception
	 *            具体的异常信息
	 * @param message
	 *            记录日志的消息 例如:<code>xxxxxx{0}xxxxx{1} </code>
	 * @param args
	 *            具体消息参数 用于替代{0}{1}
	 */
	@Deprecated
	public static void error(Logger logger, Exception exception,
							 String message, Object... args) {
		try {
			logger.error(LogUtil.messageFormat(message, args), exception);
		} catch (Exception e) {
		}
	}

	@Deprecated
	public static void error(Logger logger, String message, Object... args) {
		try {
			logger.error(LogUtil.messageFormat(message, args));
		} catch (Exception e) {
		}
	}

	/**
	 *
	 * Description: warn 日志消息记录
	 *
	 * @Version1.0 2014- 6-4 下午11:33:16 by 王星皓（ wangxh@cloud-young.com）创建
	 * @param logger
	 *            日志对象
	 * @param exception
	 *            具体的异常信息
	 * @param message
	 *            记录日志的消息 例如:<code>xxxxxx{0}xxxxx{1} </code>
	 * @param args
	 *            具体消息参数 用于替代{0}{1}
	 */
	@Deprecated
	public static void warn(Logger logger, Exception exception, String message,
			Object... args) {
		try {
			logger.warn(LogUtil.messageFormat(message, args), exception);
		} catch (Exception e) {
		}
	}


	/**
	 * @Descpription: 普通日志调用行为记录
	 * @version 1.0  2017/9/26 20:09   by  李美根（limg@cloud-young.com）创建
	 * @param   logger  日志实体 必传
	 * @param   platformName  平台名称 必传
	 * @param   inputParam  输入参数
	 * @param   message  信息
	 * @param   result  返回值
	 * @return
	 */
	public static void info(Logger logger, PlatformNameEnum platformName, String inputParam, String message, String result)
	{
		LogModel logModel = new LogModel();
		logModel.setPlatform(platformName.getPlatformName());
		logModel.setResult(result);
		logModel.setMessage(message);
		logModel.setCreateTime(dateToString(new Date()));
		logModel.setServiceName(logger.getName());
		logModel.setInputParam(inputParam);
		logModel.setLevel("Info");
		logModel.setMethodName(getTraceByMethod());
		String json = JSON.toJSONString(logModel);
		logger.info(json);
	}


	/**
	 * @Descpription: 一般用在拦截器获取不到具体logger实体时，记录的日志
	 * @version 1.0  2017/9/30 11:43   by  李美根（limg@cloud-young.com）创建
	 * @param   serviceName  服务名称
	 * @param   methodName  方法名称
	 * @param   platformName  平台名称
	 * @param   message  消息信息
	 * @param   result  返回信息
	 * @return
	 */
	public static void info(String serviceName,String methodName,PlatformNameEnum platformName,String inputParam,String message,String result)
	{
		LogModel logModel = new LogModel();
		logModel.setPlatform(platformName.getPlatformName());
		logModel.setResult(result);
		logModel.setMessage(message);
		logModel.setCreateTime(dateToString(new Date()));
		logModel.setServiceName(serviceName);
		logModel.setInputParam(inputParam);
		logModel.setLevel("Info");
		logModel.setMethodName(methodName);
		String json = JSON.toJSONString(logModel);
		LOGGER.info(json);
	}

	/**
	 * @Descpription: 一般用在拦截器获取不到具体logger实体时，记录的日志
	 * @version 1.0  2017/10/11 11:43   by  liufs 创建
	 * @param   serviceName  服务名称
	 * @param   methodName  方法名称
	 * @param   platformName  平台名称
	 * @param inputParamObj   输入参数
	 * @param   message  消息信息
	 * @param   result  返回信息
	 * @return
	 */
	public static void info(String serviceName,String methodName,PlatformNameEnum platformName,Object inputParamObj,String message,String result)
	{
		String paramString= JSON.toJSONString(inputParamObj);
		info(serviceName,methodName,platformName,paramString,message,result);
	}

	/**
	 * @Descpription:
	 * @version 1.0  2017/9/27 12:24   by  李美根（limg@cloud-young.com）创建
	 * @param   serviceName  服务名称  必传
	 * @param   methodName  方法名称  必传
	 * @param   ex         错误信息  必传
	 * @param   platformName  平台名称  必传
	 * @param   inputParam		输入参数
	 * @param   remark		备注信息
	 * @return
	 */
	public static void error(String serviceName,String methodName,Exception ex, PlatformNameEnum platformName,String inputParam,String remark) {
		assembleError(LOGGER,inputParam,methodName,platformName.getPlatformName(),serviceName,remark,ex);
	}



	/**
	 * @Descpription:
	 * @version 1.0  2017/9/27 12:24   by  李美根（limg@cloud-young.com）创建
	 * @param   logger  日志实体  必传
	 * @param   ex         错误信息  必传
	 * @param   platformName  平台名称  必传
	 * @param   inputParam		输入参数
	 * @param   remark		备注信息
	 * @return
	 */
	public static void error(Logger logger,Exception ex, PlatformNameEnum platformName,String inputParam,String remark) {
		assembleError(logger,inputParam,getTraceByMethod(),platformName.getPlatformName(),logger.getName(),remark,ex);
	}

	/**
	 * @Descpription:
	 * @version 1.0  2017/9/27 12:24   by  李美根（limg@cloud-young.com）创建
	 * @param   serviceName  服务名称  必传
	 * @param   methodName   方法名称  必传
	 * @param   throwable      抛错类  必传
	 * @param   platformName  平台名称  必传
	 * @param   inputParam		输入参数
	 * @param   remark		备注信息
	 * @return
	 */
	public static void error(String serviceName,String methodName,Throwable throwable, PlatformNameEnum platformName,String inputParam,String remark) {
			error(serviceName,methodName,(Exception) throwable,platformName,inputParam,remark);
	}

    /**
     * @Descpription:
     * @version 1.0  2017/9/27 12:24   by  李美根（limg@cloud-young.com）创建
     * @param   serviceName  服务名称  必传
     * @param   methodName   方法名称  必传
     * @param   throwable      抛错类  必传
     * @param   platformName  平台名称  必传
     * @param   inputParamObj		输入参数
     * @param   remark		备注信息
     * @return
     */
    public static void error(String serviceName,String methodName,Throwable throwable, PlatformNameEnum platformName,Object inputParamObj,String remark) {
        String paramString= JSON.toJSONString(inputParamObj);
        error(serviceName,methodName,(Exception) throwable,platformName,paramString,remark);
    }

    /**
     * @Descpription:
     * @version 1.0  2017/9/27 12:24   by  李美根（limg@cloud-young.com）创建
     * @param   serviceName  服务名称  必传
     * @param   methodName   方法名称  必传
     * @param   exception      抛错类  必传
     * @param   platformName  平台名称  必传
     * @param   inputParamObj		输入参数
     * @param   remark		备注信息
     * @return
     */
    public static void error(String serviceName,String methodName,Exception exception, PlatformNameEnum platformName,Object inputParamObj,String remark) {
        String paramString= JSON.toJSONString(inputParamObj);
        error(serviceName,methodName,exception,platformName,paramString,remark);
    }


	/**
	 * @Descpription: 封装错误类
	 * @version 1.0  2017/10/11 10:06   by  李美根（limg@cloud-young.com）创建
	 * @param
	 * @return
	 */
	private static void assembleError(Logger logger, String inputParam,String methodName,String platform,String serviceName,String remark, Exception ex)
	{
		String data = String.format("\r\n[ERROR]\"createTime\":%s" +
									"\"inputParam\":%s"+
									"\"level\":Error"+
									"\"methodName\":%s"+
									"\"platform\":%s"+
									"\"remark\":%s"+
									"\"serviceName\":%s",
				dateToString(new Date()),inputParam,methodName,platform,remark,serviceName);
		logger.error(data,ex);
	}



	/**
	 * @Descpription: 获取堆栈中的方法名称
	 * @version 1.0  2017/9/28 11:03   by  李美根（limg@cloud-young.com）创建
	 * @param
	 * @return
	 */
	private static String getTraceByMethod(){
		try{
			StackTraceElement[] callers = Thread.currentThread().getStackTrace();
			StackTraceElement caller = callers[3];
			String sMethodName = new StringBuilder().append(
					caller.getMethodName() + "()->" + caller.getLineNumber()+": "
			).toString();
			return sMethodName;
		}catch (Exception ex){
			return  "";
		}
	}


	/**
	 * @Descpription: 转换日期格式方法
	 * @version 1.0  2017/9/27 12:11   by  李美根（limg@cloud-young.com）创建
	 * @param
	 * @return
	 */
	private static String dateToString(Date time){
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		String ctime = formatter.format(time);
		return ctime;
	}

	/**
	 * 格式化消息信息
	 *
	 * @param message
	 * @param args
	 * @return消息信息 x
	 */
	private static String messageFormat(String message, Object... args) {
	   try {
	      if (StringUtils.isEmpty(message)) {
	         return "";
	      }
	      String str = MessageFormat.format(message, args);
	      return str;
	   } catch (Exception e) {
	      LOGGER.error(
	            "Log日志工具类messageFormat(String message,Object... args)发生异常:",
	            e);
	   }
	   return "";
	}



}
