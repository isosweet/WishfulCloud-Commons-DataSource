package com.wishfulcloud.commons.dataSource.test;

import com.alibaba.fastjson.JSON;
import com.wishfulcloud.commons.dataSource.entity.User;
import com.wishfulcloud.commons.dataSource.redis.jedis.JedisUtil;

import redis.clients.jedis.Jedis;

/**   
 * @Title: 
 * @Description: 
 * @author wangxuezheng
 * @date 2017年3月21日 下午4:16:17
 * @version V1.0   
 *
 */
public class TestUserCache {
	
	public static void main(String[] args) throws Exception {
		
		UserCache cache = new UserCache();
		
		Jedis jedis = JedisUtil.getJedisInstance();
		
		User u = new User();
		u.setId("12221213-0002");
		u.setSerialNumber("23232323");
		u.setTenantCode("0000000-1");
		u.setUserName("Tom----Tkkk");
		
		// 添加或更新
		boolean boo = cache.saveOrUpdate(jedis, u, u.getId(), 30);
		System.out.println(boo);
		
		
		// 查询
//		User user = cache.getById(jedis, User.class, u.getId());
//		System.out.println(JSON.toJSONString(user));
		
		// 删除
//		boolean booDel = cache.deleteEntity(jedis, User.class, u.getId());
//		System.out.println(booDel);
		
		
//		jedis.sadd("member:1" , "m4");
		
	}
	
}
