package com.wishfulcloud.commons.dataSource.mongo;

import java.util.List;
import java.util.Map;

import org.bson.conversions.Bson;

import com.wishfulcloud.commons.model.Pager;

/**   
 * @Title: 
 * @Description: 
 * @author wangxuezheng
 * @date 2017年4月12日 上午11:24:26
 * @version V1.0   
 *
 */
public interface IMongoDao<T extends BaseMongoEntity<?>>  {
	
	public boolean insertOne(String dbName,T entity) throws Exception;
    
	public boolean insertMany(String dbName,List<T> list) throws Exception;
	
	@Deprecated
	public boolean updateOne(String dbName,Map<String,Object> param,T newEntity) throws Exception;
	
	@Deprecated
	public boolean updateMany(String dbName,Map<String,Object> param,T newEntity) throws Exception;
	
	public boolean deleteOne(String dbName,Map<String,Object> param) throws Exception;
	
	public boolean deleteMany(String dbName,Map<String,Object> param) throws Exception;
	
	public T getById(String dbName,String id) throws Exception;

	public List<T> findByIds(String dbName,List<String> ids) throws Exception;
	
	public List<T> findByMap(String dbName,Map<String,Object> param) throws Exception;
	
	public List<T> findByFilter(String dbName,List<Bson> listBson) throws Exception;

	public List<T> findByFilter(String dbName,List<Bson> listBson,Bson orderBy) throws Exception;

	public List<T> findByFilterOr(String dbName,List<Bson> listBson) throws Exception;

	public List<T> findByFilterOr(String dbName,List<Bson> listBson,Bson orderBy) throws Exception;

	public List<T> findByFilterLimitOne(String dbName,List<Bson> listBson,Bson orderBy) throws Exception;

	@Deprecated
	public List<T> findByEntity(String dbName,T entity) throws Exception;
	
	public Pager<T> searchByMap(String dbName,Map<String,Object> param,Pager<T> page) throws Exception;
	
	public Pager<T> searchByFilter(String dbName,List<Bson> listBson,Pager<T> page) throws Exception;
	
}
