package com.wishfulcloud.commons.dataSource.mongo;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.Document;

import com.wishfulcloud.commons.utils.Reflections;


/**   
 * @Title: 
 * @Description: 封装mongodb与javaBean互转的方法
 * @author wangxuezheng
 * @date 2017年4月12日 上午10:42:32
 * @version V1.0   
 *
 */
public class BaseMongoEntity <T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Document转实体
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T toEntity(Document document) {

		for (String key : document.keySet()) {
			if (key.equals("_id")) {
				continue;
			}
			Object object = document.get(key);
			if (object != null)
				Reflections.invokeSetter(this, key, object);

		}
		return (T) this;
	}
	
	/**
	 * 实体转document
	 *
	 * @return
	 */
	public Document toDocument() {

		Document document = new Document();

		// 获取类中的所有定义字段
		Field[] fields = this.getClass().getDeclaredFields();

		// 循环遍历字段，获取字段对应的属性值
		for (Field field : fields) {

			try {
				field.setAccessible(true);
				String fieldName = field.getName();
				String newFieldName= fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				Object value = field.get(this);
				if (value == null) {
					// value="";
					continue;
				}
				if (!fieldName.equals("serialVersionUID")) {
					document.put(newFieldName, value);
				}

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return document;
	}
	
	@Override
	public String toString() {		
		return ToStringBuilder.reflectionToString(this);
	}
	
}
