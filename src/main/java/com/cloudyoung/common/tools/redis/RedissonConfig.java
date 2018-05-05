package com.cloudyoung.common.tools.redis;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import com.cloudyoung.common.enums.RedisArchModelEnum;

/**
 * Description: redisson configure implement
 * support:single、master-slave、sentinel、cluster 
 * All Rights Reserved.
 * @version 1.0  2017年4月23日 下午12:05:02  by 代鹏（daipeng.456@gmail.com）创建
 */
public class RedissonConfig {
	
	private final Config config = new Config();
	private RedissonClient redisson;
	
	//common configure
	private String model;
	private int dataBase = 0;
	private String password;
	private int idleConnectionTimeout = 10000;//连接空闲超时，单位：毫秒
	private int connectTimeout = 10000;//连接超时，单位：毫秒
	
	//single special configure
	private String singleNodes;//single地址
	private int connectionPoolSize = 64;//连接池大小
	
	//sentinel special configure
	private String masterName;//masterName
	private Set<String> nodes;//sentinel地址集
	
	//master-slave special configure
	private String masterNodes;//主节点地址
	private Set<String> slaveNodes;//从节点地址集
	
	//cluster special configure
	private int scanInterval;//集群状态扫描间隔时间，单位是毫秒
	private Set<String> clusterNodes;//集群节点地址集
	
	//sentinel、 master-slave、 cluster common configure
	private int slaveConnectionMinimumIdleSize = 10;//从节点最小空闲连接数
	private int slaveConnectionPoolSize = 64;//从节点连接池大小
	private int masterConnectionMinimumIdleSize = 10;//主节点最小空闲连接数
	private int masterConnectionPoolSize = 64;//主节点连接池大小
	
	/**
	 * single model construct
	 * @param model
	 * @param dataBase
	 * @param password
	 * @param singleNodes
	 */
	public RedissonConfig(String model, int dataBase, String password, String singleNodes) {
		super();
		this.model = model;
		this.dataBase = dataBase;
		this.password = password;
		this.singleNodes = singleNodes;
	}
	
	/**
	 * sentinel model construct
	 * @param model
	 * @param dataBase
	 * @param masterName
	 * @param password
	 * @param nodes
	 */
	public RedissonConfig(String model, int dataBase, String masterName, String password, Set<String> nodes) {
		super();
		this.model = model;
		this.dataBase = dataBase;
		this.masterName = masterName;
		this.password = password;
		this.nodes = nodes;
	}
	
	/**
	 * sentinel model construct, include pool and timeout properties
	 * @param model
	 * @param dataBase
	 * @param masterName
	 * @param password
	 * @param nodes
	 * @param slaveConnectionMinimumIdleSize
	 * @param slaveConnectionPoolSize
	 * @param masterConnectionMinimumIdleSize
	 * @param masterConnectionPoolSize
	 * @param idleConnectionTimeout
	 * @param connectTimeout
	 */
	public RedissonConfig(String model, int dataBase, String masterName, String password, Set<String> nodes, 
			int slaveConnectionMinimumIdleSize, int slaveConnectionPoolSize, 
			int masterConnectionMinimumIdleSize, int masterConnectionPoolSize, int idleConnectionTimeout, int connectTimeout) {
		super();
		this.model = model;
		this.dataBase = dataBase;
		this.masterName = masterName;
		this.password = password;
		this.nodes = nodes;
		this.slaveConnectionMinimumIdleSize = slaveConnectionMinimumIdleSize;
		this.slaveConnectionPoolSize = slaveConnectionPoolSize;
		this.masterConnectionMinimumIdleSize = masterConnectionMinimumIdleSize;
		this.masterConnectionPoolSize = masterConnectionPoolSize;
		this.idleConnectionTimeout = idleConnectionTimeout;
		this.connectTimeout = connectTimeout;
	}
	
	/**
	 * master-slave construct
	 * @param model
	 * @param dataBase
	 * @param password
	 * @param slaveNodes
	 * @param masterNodes
	 */
	public RedissonConfig(String model, int dataBase, String password, Set<String> slaveNodes, String masterNodes) {
		super();
		this.model = model;
		this.dataBase = dataBase;
		this.password = password;
		this.masterNodes = masterNodes;
		this.slaveNodes = slaveNodes;
	}
	
	/**
	 * cluster construct
	 * @param model
	 * @param password
	 * @param scanInterval
	 * @param clusterNodes
	 */
	public RedissonConfig(String model, String password, int scanInterval, Set<String> clusterNodes) {
		super();
		this.model = model;
		this.password = password;
		this.scanInterval = scanInterval;
		this.clusterNodes = clusterNodes;
	}

	public void init(){
		if(StringUtils.isBlank(model)){
			throw new IllegalArgumentException("RedissonConfig init method params with:[model] can not be empty......!");
		}
		if(RedisArchModelEnum.SINGLE.getModel().equals(model)){
			this.initSingleRedissonClient();
		}else if(RedisArchModelEnum.MASTERSLAVE.getModel().equals(model)){
			this.initMasterSlaveRedissonClient();
		}else if(RedisArchModelEnum.SENTINEL.getModel().equals(model)){
			this.initSentinelRedissonClient();
		}else if(RedisArchModelEnum.CLUSTER.getModel().equals(model)){
			this.initClusterRedissonClient();
		}else{
			throw new IllegalArgumentException("RedissonConfig init method params with:[model] invalid......");
		}
	}
	
	/**
	 * Description: init single redisson client
	 * @Version1.0 2017年8月24日 上午10:32:39 by 代鹏（daipeng.456@gmail.com）创建
	 */
	private void initSingleRedissonClient(){
		if(StringUtils.isBlank(singleNodes)){
			throw new IllegalArgumentException("redis install mode choice single, must be set singleNodes params......!");
		}
		config.useSingleServer()
		.setPassword(StringUtils.isNotBlank(password)?password:null)
		.setAddress(singleNodes)
		.setDatabase(dataBase)
		.setIdleConnectionTimeout(idleConnectionTimeout)
		.setConnectTimeout(connectTimeout)
		.setConnectionPoolSize(connectionPoolSize);
		redisson = Redisson.create(config);
	}
	
	/**
	 * Description: init master slave redisson client
	 * @Version1.0 2017年8月24日 上午10:33:02 by 代鹏（daipeng.456@gmail.com）创建
	 */
	private void initMasterSlaveRedissonClient(){
		if(StringUtils.isBlank(masterNodes)){
			throw new IllegalArgumentException("redis install mode choice master-slave, must be set masterNodes params......!");
		}
		if(null == slaveNodes || slaveNodes.isEmpty()){
			throw new IllegalArgumentException("redis install mode choice master-slave, must be set slaveNodes params......!");
		}
		config.useMasterSlaveServers()
		.setPassword(StringUtils.isNotBlank(password)?password:null)
		.setDatabase(dataBase)
		.setMasterAddress(masterNodes)
		.addSlaveAddress(slaveNodes.toArray(new String[]{}))
		.setIdleConnectionTimeout(idleConnectionTimeout)
		.setConnectTimeout(connectTimeout)
		.setMasterConnectionPoolSize(masterConnectionPoolSize)
		.setSlaveConnectionPoolSize(slaveConnectionPoolSize)
		.setMasterConnectionMinimumIdleSize(masterConnectionMinimumIdleSize)
		.setSlaveConnectionMinimumIdleSize(slaveConnectionMinimumIdleSize);
		redisson = Redisson.create(config);
	}
	
	/**
	 * Description: init sentinel redisson client
	 * @Version1.0 2017年8月24日 上午10:33:15 by 代鹏（daipeng.456@gmail.com）创建
	 */
	private void initSentinelRedissonClient(){
		if(null == nodes || nodes.isEmpty()){
			throw new IllegalArgumentException("redis install mode choice sentinel, must be set sentinelAddress params......!");
		}
		if(StringUtils.isBlank(masterName)){
			throw new IllegalArgumentException("redis install mode choice sentinel, must be set masterName params......!");
		}
		
		config.useSentinelServers()
		.setMasterName(masterName)
		.setPassword(StringUtils.isNotBlank(password)?password:null)
		.setDatabase(dataBase)
		.setSlaveConnectionMinimumIdleSize(slaveConnectionMinimumIdleSize)
		.setSlaveConnectionPoolSize(slaveConnectionPoolSize)
		.setMasterConnectionMinimumIdleSize(masterConnectionMinimumIdleSize)
		.setMasterConnectionPoolSize(masterConnectionPoolSize)
		.setIdleConnectionTimeout(idleConnectionTimeout)
		.setConnectTimeout(connectTimeout)
		.addSentinelAddress(nodes.toArray(new String[]{}));
		redisson = Redisson.create(config);
	}
	
	/**
	 * Description: init cluster redisson client
	 * @Version1.0 2017年8月24日 上午10:33:30 by 代鹏（daipeng.456@gmail.com）创建
	 */
	private void initClusterRedissonClient(){
		if(null == clusterNodes || clusterNodes.isEmpty()){
			throw new IllegalArgumentException("redis install mode choice cluster, must be set clusterNodes params......!");
		}
		config.useClusterServers()
		.setScanInterval(scanInterval)
		.addNodeAddress(clusterNodes.toArray(new String[]{}))
		.setPassword(password)
		.setSlaveConnectionMinimumIdleSize(slaveConnectionMinimumIdleSize)
		.setSlaveConnectionPoolSize(slaveConnectionPoolSize)
		.setMasterConnectionMinimumIdleSize(masterConnectionMinimumIdleSize)
		.setMasterConnectionPoolSize(masterConnectionPoolSize)
		.setIdleConnectionTimeout(idleConnectionTimeout)
		.setConnectTimeout(connectTimeout);
		redisson = Redisson.create(config);
	}
	
	public RedissonClient getRedissonInstance(){
		return redisson;
	}
	
}
