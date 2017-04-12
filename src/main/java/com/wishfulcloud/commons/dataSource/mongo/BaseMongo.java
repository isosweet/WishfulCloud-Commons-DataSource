package com.wishfulcloud.commons.dataSource.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Table;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.wishfulcloud.commons.model.Pager;
import com.wishfulcloud.commons.utils.StringUtils;

/**   
 * @Title: 
 * @Description: 
 * @author wangxuezheng
 * @date 2017年4月12日 上午9:57:01
 * @version V1.0   
 *
 */
public class BaseMongo {
	
	// 
	private MongoClient mongoClient;
	
	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	/**
	 * 获取DB实例 , 指定DB
	 *
	 * @param dbName
	 * @return  成功返回集合实例  <br>
	 * 			失败返回null
	 */
	public MongoDatabase getDB(String dbName) {
		if (StringUtils.isBlank(dbName)){
			return null;
		}
		return mongoClient.getDatabase(dbName);
	}
	
	/**
	 * 获取collection对象 , 指定Collection
	 *
	 * @param dbName
	 * @param collectionName
	 * @return  成功返回 文档对象实例 <br>
	 * 			失败返回null
	 */
	public MongoCollection<Document> getCollection(String dbName, String collectionName) {
		
		if (StringUtils.isBlank(dbName) || StringUtils.isBlank(collectionName)) {
			return null;
		}
		MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collectionName);
		return collection;
	}
	
	/**
	 * 插入文档
	 * 
	 * @param dbName
	 * @param clazz
	 * @param document
	 * @throws Exception
	 */
	public void insertOne(String dbName, Class<?> clazz, Document document) throws Exception{
		try {
			String tableName = getTableName(clazz);
			MongoCollection<Document> coll = getCollection(dbName, tableName);
			coll.insertOne(document);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 批量插入文档
	 * 
	 * @param dbName
	 * @param clazz
	 * @param documentList
	 * @throws Exception
	 */
	public void insertMany(String dbName, Class<?> clazz, List<Document> documentList) throws Exception{
		try {
			String tableName = getTableName(clazz);
			MongoCollection<Document> coll = getCollection(dbName, tableName);
			coll.insertMany(documentList);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 根据 key里的 字段 更新 newDocument
	 * 
	 * @param dbName
	 * @param key
	 * @param newDocument
	 * @param clazz
	 * @return
	 */
	public boolean updateOne(String dbName, Class<?> clazz, Document key, Document newDocument) throws Exception {
		String tableName = getTableName(clazz);
		UpdateResult result = null;
		try {
			MongoCollection<Document> collection = getCollection(dbName, tableName);
			result = collection.updateOne(key, new Document("$set", newDocument));
		} catch (Exception e) {
			throw e;
		}
		return result.getModifiedCount() > 0 ;
	}
	
	/**
	 * 删除符合条件的第一个文档
	 * 
	 * @param dbName
	 * @param document
	 * @param clazz
	 * @return
	 */
	public boolean deleteOne(String dbName, Class<?> clazz, Document document) throws Exception {
		String tableName = getTableName(clazz);
		DeleteResult result = null;
		try {
			MongoCollection<Document> coll = getCollection(dbName, tableName);
			result = coll.deleteOne(document);
		} catch (Exception e) {
			throw e;
		}
		return result.getDeletedCount() > 0;
	}
	
	/**
	 * 删除所有符合条件的文档
	 * 
	 * @param dbName
	 * @param clazz
	 * @param document
	 * @return
	 */
	public boolean deleteMany(String dbName, Class<?> clazz, Document document) throws Exception {
		String tableName = getTableName(clazz);
		DeleteResult result = null;
		try {
			MongoCollection<Document> coll = getCollection(dbName, tableName);
			result = coll.deleteMany(document);
		} catch (Exception e) {
			throw e;
		}
		return result.getDeletedCount() > 0;
	}
	
	/**
	 * 查询一个文档	
	 * 
	 * @param dbName
	 * @param clazz
	 * @param document
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseMongoEntity<?>> T getOne(String dbName, Class<?> clazz, Document document) throws Exception {
		String tableName = getTableName(clazz);
		T entity;
		Document findFirst = null;
		try {
			MongoCollection<Document> collection = getCollection(dbName, tableName);
			findFirst = collection.find(document).first();
			entity = (T) clazz.newInstance();
		} catch (Exception e) {
			throw e;
		}		
		return (T) entity.toEntity(findFirst);
	}
	
	
	/**
	 * 检索文档
	 * 
	 * @param dbName
	 * @param clazz
	 * @param document
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseMongoEntity<?>> List<T> find(String dbName, Class<?> clazz, Document document) throws Exception{
		String tableName = getTableName(clazz);
		List<T> list = null;
		try {
			MongoCollection<Document> collection = getCollection(dbName, tableName);
			FindIterable<Document> findIterable = collection.find(document);
			list = new ArrayList<T>();
			MongoCursor<Document> mongoCursor = findIterable.iterator();  
	        while(mongoCursor.hasNext()){  
	        	Document value = mongoCursor.next();
				T entity = (T) clazz.newInstance();
				list.add((T) entity.toEntity(value));
	        } 
	        mongoCursor.close();			
		} catch (Exception e) {
			throw e;
		}
		return list;
	}

	/**
	 * 检索文档
	 * 
	 * @param dbName
	 * @param clazz
	 * @param listBson
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseMongoEntity<?>> List<T> findByFilter(String dbName, Class<?> clazz, List<Bson> listBson) throws Exception{
		String tableName = getTableName(clazz);
		List<T> list = null;
		try {
			MongoCollection<Document> collection = getCollection(dbName, tableName);
			FindIterable<Document> findIterable;
			if(listBson!=null&&listBson.size()>0){
				findIterable= collection.find(Filters.and(listBson));
			}
			else{
				findIterable= collection.find();
			}
			list = new ArrayList<T>();
			MongoCursor<Document> mongoCursor = findIterable.iterator();  
	        while(mongoCursor.hasNext()){  
	        	Document value = mongoCursor.next();				
				T entity = (T) clazz.newInstance();
				list.add((T) entity.toEntity(value));
	        } 
	        mongoCursor.close();			
		} catch (Exception e) {
			throw e;
		}
		return list;
	}
	
	/**
	 * 检索文档
	 * 
	 * @param dbName
	 * @param clazz
	 * @param listBson
	 * @param orderBy
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseMongoEntity<?>> List<T> findByFilter(String dbName, Class<?> clazz, List<Bson> listBson, Bson orderBy) throws Exception{
		String tableName = getTableName(clazz);
		List<T> list = null;
		try {
			MongoCollection<Document> collection = getCollection(dbName, tableName);
			FindIterable<Document> findIterable;
			if(listBson!=null&&listBson.size()>0){
				findIterable= collection.find(Filters.and(listBson)).sort(orderBy);
			}
			else{
				findIterable= collection.find().sort(orderBy);
			}
			list = new ArrayList<T>();
			MongoCursor<Document> mongoCursor = findIterable.iterator();
			while(mongoCursor.hasNext()){
				Document value = mongoCursor.next();
				T entity = (T) clazz.newInstance();
				list.add((T) entity.toEntity(value));
			}
			mongoCursor.close();
		} catch (Exception e) {
			throw e;
		}
		return list;
	}

	/**
	 *  or查询 区别于上面的and查询
	 * @param dbName
	 * @param clazz
	 * @param listBson
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseMongoEntity<?>> List<T> findByFilterOr(String dbName, Class<?> clazz, List<Bson> listBson) throws Exception{
		String tableName = getTableName(clazz);
		List<T> list = null;
		try {
			MongoCollection<Document> collection = getCollection(dbName, tableName);
			FindIterable<Document> findIterable;
			if(listBson!=null&&listBson.size()>0){
				findIterable= collection.find(Filters.or(listBson));
			}
			else{
				findIterable= collection.find();
			}
			list = new ArrayList<T>();
			MongoCursor<Document> mongoCursor = findIterable.iterator();
			while(mongoCursor.hasNext()){
				Document value = mongoCursor.next();
				T entity = (T) clazz.newInstance();
				list.add((T) entity.toEntity(value));
			}
			mongoCursor.close();
		} catch (Exception e) {
			throw e;
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseMongoEntity<?>> List<T> findByFilterOr(String dbName, Class<?> clazz, List<Bson> listBson,Bson orderBy) throws Exception{
		String tableName = getTableName(clazz);
		List<T> list = null;
		try {
			MongoCollection<Document> collection = getCollection(dbName, tableName);
			FindIterable<Document> findIterable;
			if(listBson!=null&&listBson.size()>0){
				findIterable= collection.find(Filters.or(listBson)).sort(orderBy);
			}
			else{
				findIterable= collection.find().sort(orderBy);
			}
			list = new ArrayList<T>();
			MongoCursor<Document> mongoCursor = findIterable.iterator();
			while(mongoCursor.hasNext()){
				Document value = mongoCursor.next();
				T entity = (T) clazz.newInstance();
				list.add((T) entity.toEntity(value));
			}
			mongoCursor.close();
		} catch (Exception e) {
			throw e;
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseMongoEntity<?>> List<T> findByFilterLimitOne(String dbName, Class<?> clazz, List<Bson> listBson, Bson orderBy) throws Exception{
		String tableName = getTableName(clazz);
		List<T> list = null;
		try {
			MongoCollection<Document> collection = getCollection(dbName, tableName);
			FindIterable<Document> findIterable;
			if(listBson!=null&&listBson.size()>0){
				findIterable= collection.find(Filters.and(listBson)).sort(orderBy).limit(1);
			} else{
				findIterable= collection.find().sort(orderBy).limit(1);
			}
			list = new ArrayList<T>();
			MongoCursor<Document> mongoCursor = findIterable.iterator();
	        while(mongoCursor.hasNext()){
	        	Document value = mongoCursor.next();
				T entity = (T) clazz.newInstance();
				list.add((T) entity.toEntity(value));
	        }
	        mongoCursor.close();
		} catch (Exception e) {
			throw e;
		}
		return list;
	}

	/**
	 * 分页基本 查询
	 *
	 * @param dbName
	 * @param pageNo
	 * @param pageSize
	 * @param clazz
	 * @param documentList
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseMongoEntity<?>> List<T> findByPage(String dbName, Class<?> clazz, int pageNo, int pageSize, 
																	Bson sortBy, List<Document> documentList) throws Exception{
		String tableName = getTableName(clazz);
		List<T> returnList = null;
		MongoCollection<Document> coll = null;
		try {
			returnList = new ArrayList<T>();
			coll = getCollection(dbName, tableName);
			// Bson orderBy = new BasicDBObject("LogCreateDate", 1);
			Map<String, Object> map = new HashMap<String, Object>();
			for (Document d : documentList) {
				for (String key : d.keySet()) {
					map.put(key, d.get(key));
				}
			}

			MongoCursor<Document> it = coll.find(new Document(map)).sort(sortBy).skip((pageNo - 1) * pageSize).limit(pageSize).iterator();

			while (it.hasNext()) {
				Document value = it.next();
				T entity = (T) clazz.newInstance();
				returnList.add((T) entity.toEntity(value));
			}
			it.close();
		} catch (Exception e) {
			throw e;
		}
		return returnList;
	}

	/**
	 * 分页基本 查询
	 *
	 * @param dbName
	 * @param pageNo
	 * @param pageSize
	 * @param clazz
	 * @param documentList
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseMongoEntity<?>> Pager<T> searchPageByFilter(String dbName, Class<?> clazz,Pager<T> page,Bson orderBy, List<Bson> listBson) {
		String tableName = getTableName(clazz);
		List<T> returnList = null;
		try {
			returnList = new ArrayList<T>();
			MongoCollection<Document> coll = getCollection(dbName, tableName);
			MongoCursor<Document> findIterable;
			if(listBson!=null&&listBson.size()>0){
				findIterable= coll.find(Filters.and(listBson)).sort(orderBy).skip((page.getPageNo() - 1) * page.getPageSize()).limit(page.getPageSize()).iterator();;
			}
			else{
				findIterable= coll.find().sort(orderBy).skip((page.getPageNo() - 1) * page.getPageSize()).limit(page.getPageSize()).iterator();;
			}
			while (findIterable.hasNext()) {
				Document value = findIterable.next();
				T entity = (T) clazz.newInstance();
				returnList.add((T) entity.toEntity(value));
			}
			findIterable.close();
			page.setDataList(returnList);
		} catch (Exception e) {
			return null;
		}
		return page;
	}
	
	/**
	 * 根据注解 获得实体代表的表名
	 * 
	 * @param clazz
	 * @return
	 */
	private  String getTableName(Class<?> clazz) {
		String tableName = null;
		if (clazz.isAnnotationPresent(Table.class)) {
			Table table = (Table) clazz.getAnnotation(Table.class);
			tableName = table.name();
		} else {
			tableName = clazz.getSimpleName();
		}
		return tableName;
	}

	/**
	 * 拼装 小于 查询的Document
	 *
	 * @param column
	 * @param value
	 * @return
	 */
	public Document lessThan(String column, Object value) {

		return new Document(column, new Document("$lt", value));
	}
	
	/**
	 * 拼装小于等于的Document
	 * 
	 * @param column
	 * @param value
	 * @return
	 */
	public Document lessThanE(String column, Object value) {

		return new Document(column, new Document("$lte", value));
	}
	
	/**
	 * 拼装 大于 查询语句的Document
	 *
	 * @param column
	 * @param value
	 * @return
	 */
	public Document greaterThan(String column, Object value) {

		return new Document(column, new Document("$gt", value));
	}	
	
	/**
	 * 拼装大于等于的Document
	 * 
	 * @param column
	 * @param value
	 * @return
	 */
	public Document greaterThanE(String column, Object value) {

		return new Document(column, new Document("$gte", value));
	}

	/**
	 * 关闭Mongodb
	 */
	public void close() {
		if (mongoClient != null) {
			mongoClient.close();
			mongoClient = null;
		}
	}

	
//	static {
//		
//		logger.debug("===============MongoDBUtil初始化================");
//		
//		ResourceBundle rb = ResourceBundle.getBundle("config");
//		String hostandport = rb.getString("mongos.host");
//
//		List<ServerAddress> sdList = new ArrayList<ServerAddress>();
//
//		for (String item : hostandport.split(",")) {
//			String host = item.split(":")[0].trim();
//			int port = Integer.valueOf(item.split(":")[1].trim());
//			sdList.add(new ServerAddress(host, port));
//		}
//		MongoClientOptions.Builder options = new MongoClientOptions.Builder();
//		options.connectionsPerHost(300);// 连接池设置为300个连接,默认为100
//		options.connectTimeout(15000);// 连接超时，推荐>3000毫秒
//		options.maxWaitTime(5000); //
//		options.socketTimeout(0);// 套接字超时时间，0无限制
//		options.threadsAllowedToBlockForConnectionMultiplier(5000);// 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
//		options.build();
//		mongoClient = new MongoClient(sdList, options.build());
//
//	}
	
	
}
