package com.wishfulcloud.commons.dataSource.mongo;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.google.common.collect.Lists;
import com.mongodb.client.model.Filters;
import com.wishfulcloud.commons.model.Pager;

/**   
 * @Title: 
 * @Description: 
 * @author wangxuezheng
 * @date 2017年4月12日 上午11:25:51
 * @version V1.0   
 *
 */
public class MongoDao<T extends BaseMongoEntity<?>> extends BaseMongo implements IMongoDao<T> {
	

	/**
	 * 插入一条记录
	 * @param dbName
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public boolean insertOne(String dbName,T entity) throws Exception{
		if(entity==null){
			return false;
		}
		Class<T> clazz=this.getTClass();
		insertOne(dbName, clazz, entity.toDocument());
		return true;
	}
	
	/**插入多条记录
	 * @param dbName
	 * @param list
	 * @return
	 * @throws Exception 
	 */
	public boolean insertMany(String dbName,List<T> list) throws Exception{
		if(list.isEmpty()){
			return false;
		}
		Class<T> clazz=this.getTClass();
		List<Document> docList= Lists.newArrayList();
		for (T entity : list) {
			docList.add(entity.toDocument());
		}
		insertMany(dbName, clazz, docList);
		return true;
	}
	
	/**
	 * 更新第一条匹配的记录
	 * @param dbName
	 * @param param 将匹配的条件放入map中
	 * @param newEntity
	 * @return
	 * @throws Exception 
	 */
	public boolean updateOne(String dbName,Map<String,Object> param,T newEntity) throws Exception{
		Class<T> clazz=this.getTClass();
		Document doc = new Document();
		for (Map.Entry<String, Object> entry : param.entrySet()) {
			doc.append(entry.getKey(), entry.getValue());
		}
		return updateOne(dbName, clazz, doc, newEntity.toDocument());
	}
	
	/**
	 * 更新所有匹配的记录
	 * @param dbName
	 * @param param 将匹配的条件放入map中
	 * @param newEntity
	 * @return
	 */
//	public boolean updateMany(String dbName,Map<String,Object> param,T newEntity){
//		Class<T> clazz=this.getTClass();
//		Document doc = new Document();
//		for (Map.Entry<String, Object> entry : param.entrySet()) {
//			doc.append(entry.getKey(), entry.getValue());
//		}
//		return updateMany(dbName, clazz, doc, newEntity.toDocument());
//	}
	
	/**
	 * 删除第一条匹配的记录
	 * @param dbName
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	public boolean deleteOne(String dbName,Map<String,Object> param) throws Exception{
		Class<T> clazz=this.getTClass();
		Document doc = new Document();
		for (Map.Entry<String, Object> entry : param.entrySet()) {
			doc.append(entry.getKey(), entry.getValue());
		}
		return deleteOne(dbName, clazz, doc);
	}
	
	/**
	 * 删除所有匹配的记录
	 * @param dbName
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	public boolean deleteMany(String dbName,Map<String,Object> param) throws Exception{
		Class<T> clazz=this.getTClass();
		Document doc = new Document();
		if(param!=null){
			for (Map.Entry<String, Object> entry : param.entrySet()) {
				doc.append(entry.getKey(), entry.getValue());
			}
		}
		return deleteMany(dbName, clazz, doc);
	}
	
	
	/**
	 * 根据主键查询
	 * @param dbName
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public T getById(String dbName,String id) throws Exception{
		Class<T> clazz=this.getTClass();
		return getOne(dbName, clazz, new Document("Id",id));
	}

	/** 根据多个id 那多条记录
	 * @param dbName
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	public List<T> findByIds(String dbName,List<String> ids) throws Exception{
		Class<T> clazz=this.getTClass();
		List<Bson> bsonList = new ArrayList<Bson>();
		for(String id:ids){
			bsonList.add(Filters.eq("Id",id));
		}
		return findByFilterOr(dbName, clazz, bsonList);
	}

	/**
	 * 根据map查询列表
	 * @param dbName
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	public List<T> findByMap(String dbName,Map<String,Object> param) throws Exception{
		Class<T> clazz=this.getTClass();
		Document document = new Document();
		if(param!=null){
			for (Map.Entry<String, Object> entry : param.entrySet()) {
				document.append(entry.getKey(), entry.getValue());
			}
		}
		return find(dbName, clazz, document);
	}
	
	/**
	 * 根据Filter查询
	 * @param dbName
	 * @return
	 * @throws Exception 
	 */
	public List<T> findByFilter(String dbName,List<Bson> listBson) throws Exception{
		Class<T> clazz=this.getTClass();
		return findByFilter(dbName, clazz, listBson);
	}

	/**
	 * 根据Filter查询
	 * @param dbName
	 * @return
	 * @throws Exception 
	 */
	public List<T> findByFilter(String dbName,List<Bson> listBson,Bson orderBy) throws Exception{
		Class<T> clazz=this.getTClass();
		return findByFilter(dbName, clazz, listBson,orderBy);
	}

//	@Override
//	public List<T> findByFilterOr(String dbName, List<Bson> listBson) {
//		Class<T> clazz=this.getTClass();
//		return findByFilterOr(dbName, clazz, listBson);
//	}
//
//	@Override
//	public List<T> findByFilterOr(String dbName, List<Bson> listBson, Bson orderBy) {
//		Class<T> clazz=this.getTClass();
//		return findByFilterOr(dbName, clazz, listBson,orderBy);
//	}

	/**
	 * 根据Filter查询一个
	 * @param dbName
	 * @param listBson
	 * @param orderBy
	 * @return
	 * @throws Exception 
	 */
	public List<T> findByFilterLimitOne(String dbName,List<Bson> listBson,Bson orderBy) throws Exception{
		Class<T> clazz=this.getTClass();
		return findByFilterLimitOne(dbName, clazz, listBson,orderBy);
	}

	/**
	 * 根据实体查询列表（注意，此时所有不为空实体属性都会作为条件进行搜索）
	 * @param dbName
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public List<T> findByEntity(String dbName,T entity) throws Exception{
		Class<T> clazz=this.getTClass();
		return find(dbName, clazz, entity.toDocument());
	}
	
	/**
	 * 分页查询，目前page中没有总页数
	 * @param dbName
	 * @param param
	 * @param page
	 * @return
	 */
//	public Pager<T> searchByMap(String dbName,Map<String,Object> param, Pager<T> page){
//		Class<T> clazz=this.getTClass();
//		Bson orderBy;
//		if(StringUtils.isNotBlank(page.getOrderBy())){
//			orderBy = new BasicDBObject(page.getOrderBy(), page.getOrderByRule());
//		}
//		else{
//			orderBy = new BasicDBObject("CreateTime", page.getOrderByRule());//1 or -1 倒序
//		}		
//		List<Document> docList = Lists.newArrayList();
//		if(param!=null){
//			for (Map.Entry<String, Object> entry : param.entrySet()) {
//				docList.add(new Document(entry.getKey(),entry.getValue()));
//			}
//		}
//		
//		List<T> list= findByPage(dbName, clazz, page.getPageNo(), page.getPageSize(), orderBy, docList);
//		page.setList(list);
//		return page;
//	}
	
//	public Pager<T> searchByFilter(String dbName,List<Bson> listBson,Pager<T> page){
//		Class<T> clazz=this.getTClass();
//		Bson orderBy;
//		if(StringUtils.isNotBlank(page.getOrderBy())){
//			orderBy = new BasicDBObject(page.getOrderBy(), page.getOrderByRule());
//		}
//		else{
//			orderBy = new BasicDBObject("CreateTime", page.getOrderByRule());//1 or -1 倒序
//		}
//		return searchPageByFilter(dbName, clazz, page, orderBy, listBson);
//	}
	
	
	private Class<T> getTClass()
    {
        @SuppressWarnings("unchecked")
		Class<T> tClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }

	public boolean updateMany(String dbName, Map<String, Object> param, T newEntity) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public Pager<T> searchByMap(String dbName, Map<String, Object> param, Pager<T> page) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Pager<T> searchByFilter(String dbName, List<Bson> listBson, Pager<T> page) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<T> findByFilterOr(String dbName, List<Bson> listBson) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<T> findByFilterOr(String dbName, List<Bson> listBson, Bson orderBy) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
