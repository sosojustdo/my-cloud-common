package com.cloudyoung.common.test;

public class DistributedLockTest {

//	public static void main(String[] args) {
//		final String key = "test_lock";
//		RedissonManager.init();
//		for (int i = 1; i <= 100; i++) {
//			new Thread(new Runnable() {
//				public void run() {
//					try {
//						RedissonDistributedLock.lock(key);
//						 System.out.println("当前线程已获取锁:" + Thread.currentThread().getName());
//						Thread.sleep(2000L);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}finally {
//						RedissonDistributedLock.unlock(key);
//						System.out.println("当前线程已释放锁:" + Thread.currentThread().getName());
//					}
//				}
//			}).start();
//		}
//	}

}
