package com.cloudyoung.common.tools.zk;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.CreateMode;

import com.cloudyoung.common.utils.LogUtil;

/**
 * Description: 序列id生成器
 * All Rights Reserved.
 * @version 1.0  2016年7月18日 下午12:59:24  by 代鹏（daipeng.456@gmail.com）创建
 */
public class SequenceGenerator {
	
	private static final Logger logger = LogManager.getLogger(SequenceGenerator.class);
	
	private String zkServerList;
	private int zkBaseSleepTimeMs = 1000;
	private int maxRetries = 3;
	private String zkSerialIdPrefixPath;
	
	public SequenceGenerator(String zkServerList, int zkBaseSleepTimeMs, int maxRetries, String zkSerialIdPrefixPath) {
		super();
		this.zkServerList = zkServerList;
		this.zkBaseSleepTimeMs = zkBaseSleepTimeMs;
		this.maxRetries = maxRetries;
		this.zkSerialIdPrefixPath = zkSerialIdPrefixPath;
	}

	/**
	 * Description: 
	 * @Version1.0 2016年7月2日 下午3:40:56 by 代鹏（daipeng.456@gmail.com）创建
	 * @param serialPrefix 回话id前缀 @required
	 * @param dirSplit 目录分隔 @optional
	 * @return
	 */
	public String getSequence(String serialPrefix, String dirSplit) {
		if(StringUtils.isBlank(serialPrefix)){
			LogUtil.info(logger,"params serialPrefix is empty!");
			return null;
		}
		if(StringUtils.isBlank(zkSerialIdPrefixPath)){
			throw new IllegalArgumentException("zkSerialIdPrefixPath value is required, please init...");
		}
		String serialId = null;
		CuratorFramework curatorFramework = null;
		try {
			long startTime = System.currentTimeMillis();
			curatorFramework = getCuratorInstance();
			StringBuffer prefixPath = new StringBuffer(zkSerialIdPrefixPath);
			if(StringUtils.isNotBlank(dirSplit)){
				prefixPath.append(dirSplit).append("/");
			}
			
			String zkSerialIdPath = new StringBuffer(prefixPath).append(serialPrefix).toString();
			// 绝对路径为:/zkSerialIdPrefixPath/dirSplit/serialPrefix + 单调递增数字
			String serialNodeCreated = curatorFramework.create().
										creatingParentsIfNeeded().
										withMode(CreateMode.EPHEMERAL_SEQUENTIAL).
										forPath(zkSerialIdPath);
					
			serialId = serialNodeCreated.replace(prefixPath.toString(), "");//把前缀去掉

			if (StringUtils.isBlank(serialId)) {
				throw new Exception("Geneator serial id number is empty!" );
			}
			
			// zk上节点太多会影响性能，因此无用节点尽量删除
			// 同步删除节点
			curatorFramework.delete().forPath(serialNodeCreated);
			// 异步删除节点
//			curatorFramework.delete().inBackground(new BackgroundCallback() {
//				public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
//					String eventPath = event.getPath();
//					CuratorEventType eventType = event.getType();
//					int resultCode = event.getResultCode();
//					LogUtil.info(logger,"Delete serialId, eventType:{0}, eventPath:{1}, resultCode:{2}",eventType, eventPath, resultCode);
//				}
//
//			}).forPath(serialNodeCreated);
			
			long endTime = System.currentTimeMillis();
			LogUtil.info(logger,"serialNodeCreated:{0}, serialId:{1}, timecost:{2}",serialNodeCreated, serialId, endTime - startTime);
		} catch (Exception ex) {
			LogUtil.error(logger,ex,"getSequence method invoke error");
		}finally {
			if(null != curatorFramework){
				curatorFramework.close();
			}
		}
		return serialId;
	}
	
	/**
	 * Description: 批量生产序列id
	 * @Version1.0 2016年7月18日 下午1:08:47 by 代鹏（daipeng.456@gmail.com）创建
	 * @param serialPrefix
	 * @param dirSplit
	 * @param n
	 * @return
	 */
	public List<String> getSequenceBatch(String serialPrefix, String dirSplit, int n) {
		List<String> serialIds = new ArrayList<String>();
		if(n > 0){
			try {
				for(int i=0; i<n; i++){
					String serialId = this.getSequence(serialPrefix, dirSplit);
					if(StringUtils.isNotBlank(serialId)){
						serialIds.add(serialId);
					}
				}
				return serialIds;
			} catch (Exception e) {
				LogUtil.error(logger,e,"getSequenceBatch invoke error");
			}
		}
		return serialIds;
	}
	
	/**
	 * Description: 创建Curator
	 * @Version1.0 2016年7月6日 上午11:21:29 by 代鹏（daipeng.456@gmail.com）创建
	 * @return
	 */
	private CuratorFramework getCuratorInstance() {
		if(StringUtils.isBlank(zkServerList)){
			throw new IllegalArgumentException("zkServerList value is required, please init...");
		}
		ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(zkBaseSleepTimeMs, maxRetries);
		CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(zkServerList, retryPolicy);
		curatorFramework.start();
		return curatorFramework;
	}
	
	public String getZkServerList() {
		return zkServerList;
	}

	public void setZkServerList(String zkServerList) {
		this.zkServerList = zkServerList;
	}

	public int getZkBaseSleepTimeMs() {
		return zkBaseSleepTimeMs;
	}

	public void setZkBaseSleepTimeMs(int zkBaseSleepTimeMs) {
		this.zkBaseSleepTimeMs = zkBaseSleepTimeMs;
	}

	public int getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	public String getZkSerialIdPrefixPath() {
		return zkSerialIdPrefixPath;
	}

	public void setZkSerialIdPrefixPath(String zkSerialIdPrefixPath) {
		this.zkSerialIdPrefixPath = zkSerialIdPrefixPath;
	}
	
}
