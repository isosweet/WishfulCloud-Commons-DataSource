package com.wishfulcloud.commons.dataSource.redis;

import com.wishfulcloud.commons.dataSource.redis.jedis.JedisUtil;

import redis.clients.jedis.Jedis;

/**   
 * @Title: 
 * @Description: 
 * @author wangxuezheng
 * @date 2017年3月21日 下午3:35:34
 * @version V1.0   
 *
 */
public class TestJedisUtil {
	
	public static void main(String[] args) {
		
		try {
			Jedis jedis = JedisUtil.getJedisInstance();
			jedis.set("k1", "v1");
			
			
			JedisUtil.close(jedis);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
}
