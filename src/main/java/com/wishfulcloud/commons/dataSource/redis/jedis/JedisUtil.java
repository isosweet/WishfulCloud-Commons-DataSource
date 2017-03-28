package com.wishfulcloud.commons.dataSource.redis.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**   
 * Jedis 工具类
 * 
 * 
 * @author wangxuezheng
 * @date 2017年3月21日 下午3:28:18
 * @version V1.0   
 *
 */
public class JedisUtil {
	
	private static volatile GenericObjectPoolConfig poolConfig = null;
	
	static{
		poolConfig = new GenericObjectPoolConfig();
		
		// 最大连接数为默认值的5倍
		poolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL * 5);
		// 最大空闲连接数为默认值的3倍
		poolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 3);
		// 最小空闲连接数为默认值的2倍
		poolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MIN_IDLE * 2);
		// 开启jmx功能
		poolConfig.setJmxEnabled(true);
		// 连接池没有连接后客户端的最大等待时间(单位 毫秒)
		poolConfig.setMaxWaitMillis(3000);
	}
	
	
	private static volatile JedisPool jedisPool = null;
	
	private JedisUtil(){}
	
	/**
	 * 从连接池中获取一个Jedis实例
	 * 
	 * @return
	 */
	public static Jedis getJedisInstance() throws Exception{
		
		try {
			if(null == jedisPool){
				synchronized (JedisUtil.class){
					if(null == jedisPool){
						jedisPool = new JedisPool(poolConfig,"192.168.1.218",6379);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return jedisPool.getResource();
	}

	/**
	 * 归还到连接池
	 * 
	 * @param jedis
	 */
	public static void close(Jedis jedis) throws Exception{
		try {
			if(null != jedis){
				jedis.close();
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
}
