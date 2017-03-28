package com.wishfulcloud.commons.dataSource.redis;

import com.wishfulcloud.commons.dataSource.redis.cluster.RedisCluster;

import redis.clients.jedis.JedisCluster;

public class TestRedisCluster {
	
	public static void main(String[] args) {
		
		JedisCluster cluster = RedisCluster.getInstance();
		
		String ok = cluster.set("k3", "v3");
		
		System.out.println(ok);
		
	}
	
}
