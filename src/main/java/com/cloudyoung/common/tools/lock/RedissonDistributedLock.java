package com.cloudyoung.common.tools.lock;

import org.redisson.api.RedissonClient;

import com.cloudyoung.common.tools.redis.RedissonConfig;

/**
 * Description: redisson分布式锁, 支持全局与局部锁
 * @version 1.0 2016年8月6日 下午8:02:06 by 代鹏（daipeng.456@gmail.com）创建
 */
public class RedissonDistributedLock {
	
	private RedissonConfig redissonConfig;
	private RedissonClient redisson;
	
	public RedissonDistributedLock setRedissonConfig(RedissonConfig config){
		this.redissonConfig = config;
		this.redisson = redissonConfig.getRedissonInstance();
		return this;
	}
	
	public RedissonClient getRedisson() {
		return redisson;
	}
	
}
