package com.wishfulcloud.commons.dataSource.redis.jedis;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;

/**   
 * 单机或着主从复制 ，Jedis Java 客户端
 * 
 * @author wangxuezheng
 * @date 2017年3月21日 下午3:55:26
 * @version V1.0   
 *
 */
public class BaseCache<T> {
	
	/**
	 * 保存或者更新一个实体
	 * 
	 * 如果seconds参数值不为0的话，则是过期缓存，具有缓存时长
	 * 
	 * @param jedis
	 * @param entity 实体对象
	 * @param id     实体主键
	 * @param seconds 有效时长多少秒
	 *   
	 * @return true 保存或更新成功 
	 * 		   false 保存或更新失败
	 */
	public Boolean saveOrUpdate(Jedis jedis, T entity, String id, int seconds){
		String key  = entity.getClass().getSimpleName().toLowerCase() + ":" + id;
		String ok = jedis.set(key , JSON.toJSONString(entity));
		if (seconds != 0){
			jedis.expire(key, seconds);
		}
		return "OK".equals(ok);
	}
	
	/**
	 * 根据类型和id获取一个实体，未获取到返回 null
	 * 
	 * @param jedis
	 * @param clazz 实体.class
	 * @param id    主键id
	 * 
	 * @return T 或着 null
	 */
	public T getById(Jedis jedis, Class<T> clazz, String id){
		
		String object = jedis.get(clazz.getSimpleName().toLowerCase() + ":" +id);
		return JSON.parseObject(object, clazz);
	}
	
	/**
	 * 根据id删除一个实体对象
	 * 
	 * @param jedis
	 * @param clazz 实体.class
	 * @param id 主键id
	 * @return  true 删除成功
	 * 			false 删除失败
	 * 		
	 */
	public Boolean deleteEntity(Jedis jedis, Class<T> clazz, String id){
		String key = clazz.getSimpleName().toLowerCase() + ":" +id;
		return jedis.del(key) > 0;
	}
	
}
