package com.cn.leedane.mybatis;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.StringUtils;

import com.cn.leedane.mybatis.table.TableFormat;
import com.cn.leedane.mybatis.table.annotation.Column;
import com.cn.leedane.mybatis.table.annotation.Table;
import com.cn.leedane.mybatis.table.impl.HumpToUnderLineFormat;

public class SqlProvider {
	
	
	private TableFormat tableFormat = new HumpToUnderLineFormat();

	public String insert(Object bean) {
		Class<?> beanClass = bean.getClass();
		String tableName = getTableName(beanClass);
		Field[] fields = getFields(beanClass);
		StringBuilder insertSql = new StringBuilder();
		List<String> insertParas = new ArrayList<String>();
		List<String> insertParaNames = new ArrayList<String>();
		insertSql.append("INSERT INTO ").append(tableName).append("(");
		try {
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				//特殊处理serialVersionUID
				if(field.getName().equalsIgnoreCase("serialVersionUID")){
					continue;
				}
				
				Column column = field.getAnnotation(Column.class);
				String columnName = "";
				if (column != null) {
					if (!column.required())
						continue;
					columnName = column.value();
				}
				if (StringUtils.isEmpty(columnName)) {
					columnName = tableFormat.getColumnName(field.getName());
				}
				field.setAccessible(true);
				Object object = field.get(bean);
				if (object != null) {
					insertParaNames.add(columnName);
					insertParas.add("#{" + field.getName() + "}");
				}
			}
		} catch (Exception e) {
			new RuntimeException("get insert sql is exceptoin:" + e);
		}
		for (int i = 0; i < insertParaNames.size(); i++) {
			insertSql.append(insertParaNames.get(i));
			if (i != insertParaNames.size() - 1)
				insertSql.append(",");
		}
		insertSql.append(")").append(" VALUES(");
		for (int i = 0; i < insertParas.size(); i++) {
			insertSql.append(insertParas.get(i));
			if (i != insertParas.size() - 1)
				insertSql.append(",");
		}
		insertSql.append(")");
		return insertSql.toString();
	}

	public String update(Object bean) {
		Class<?> beanClass = bean.getClass();
		String tableName = getTableName(beanClass);
		Field[] fields = getFields(beanClass);
		StringBuilder updateSql = new StringBuilder();
		updateSql.append(" update ").append(tableName).append(" set ");
		try {
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				Column column = field.getAnnotation(Column.class);
				String columnName = "";
				if (column != null) {
					if (!column.required())
						continue;
					columnName = column.value();
				}
				if (StringUtils.isEmpty(columnName)) {
					columnName = tableFormat.getColumnName(field.getName());
				}
				field.setAccessible(true);
				Object beanValue = field.get(bean);
				if (beanValue != null) {
					updateSql.append(columnName).append("=#{").append(field.getName()).append("}");
					if (i != fields.length - 1) {
						updateSql.append(",");
					}
				}
			}
		} catch (Exception e) {
			new RuntimeException("get update sql is exceptoin:" + e);
		}
		updateSql.append(" where ").append(tableFormat.getId()+" =#{id}");
		return updateSql.toString();
	}

	public String delete(Object bean) {
		Class<?> beanClass = bean.getClass();
		String tableName = getTableName(beanClass);
		Field[] fields = getFields(beanClass);
		StringBuilder deleteSql = new StringBuilder();
		deleteSql.append(" delete from ").append(tableName).append(" where  ");
		try {
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				Column column = field.getAnnotation(Column.class);
				String columnName = "";
				if (column != null) {
					if (!column.required())
						continue;
					columnName = column.value();
				}
				if (StringUtils.isEmpty(columnName)) {
					columnName = tableFormat.getColumnName(field.getName());
				}
				field.setAccessible(true);
				Object beanValue = field.get(bean);
				if (beanValue != null) {
					deleteSql.append(columnName).append("=#{").append(field.getName()).append("}");
					if (i != fields.length - 1) {
						deleteSql.append(" and ");
					}
				}
			}
		} catch (Exception e) {
			new RuntimeException("get delete sql is exceptoin:" + e);
		}
		return deleteSql.toString();
	}

	public String findFirst(Object bean) {
		Class<?> beanClass = bean.getClass();
		String tableName = getTableName(beanClass);
		Field[] fields = getFields(beanClass);
		StringBuilder selectSql = new StringBuilder();
		List<String> selectParaNames = new ArrayList<String>();
		List<String> selectParas = new ArrayList<String>();
		selectSql.append("select ");
		try {
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				Column column = field.getAnnotation(Column.class);
				String columnName = "";
				if (column != null) {
					if (!column.required())
						continue;
					columnName = column.value();
				}
				if (StringUtils.isEmpty(columnName)) {
					columnName = tableFormat.getColumnName(field.getName());
				}
				field.setAccessible(true);
				Object object = field.get(bean);
				selectSql.append(field.getName());
				if (object != null) {
					selectParaNames.add(columnName);
					selectParas.add("#{" + field.getName() + "}");
				}
				if (i != fields.length - 1)
					selectSql.append(",");
			}
		} catch (Exception e) {
			new RuntimeException("get select sql is exceptoin:" + e);
		}
		selectSql.append(" from ").append(tableName).append(" where ");
		for (int i = 0; i < selectParaNames.size(); i++) {
			selectSql.append(selectParaNames.get(i)).append("=").append(selectParas.get(i));
			if (i != selectParaNames.size() - 1)
				selectSql.append(" and ");
		}
		return selectSql.toString();
	}

	private String getTableName(Class<?> beanClass) {
		String tableName = "";
		Table table = beanClass.getAnnotation(Table.class);
		if (table != null) {
			tableName = table.value();
		} else {
			tableName = tableFormat.getTableName(beanClass.getSimpleName());
		}
		
		if(!StringUtils.isEmpty(tableName) && tableName.endsWith("_BEAN")){
			tableName = tableName.substring(0, tableName.length() - "_BEAN".length());
		}
		return tableName;
	}
	
	/**
	 * 获取类clazz的所有Field，包括其父类的Field，如果重名，以子类Field为准。
	 * @param clazz
	 * @return Field数组
	 */
	private Field[] getFields(Class<?> beanClass) {
		ArrayList<Field> fieldList = new ArrayList<Field>();
		Field[] dFields = beanClass.getDeclaredFields();
		if (null != dFields && dFields.length > 0) {
			fieldList.addAll(Arrays.asList(dFields));
		}

		Class<?> superClass = beanClass.getSuperclass();
		if (superClass != Object.class) {
			Field[] superFields = getFields(superClass);
			if (null != superFields && superFields.length > 0) {
				for(Field field:superFields){
					if(!isContain(fieldList, field)){
						fieldList.add(field);
					}
				}
			}
		}
		Field[] result=new Field[fieldList.size()];
		fieldList.toArray(result);
		return result;
	}

	/**检测Field List中是否已经包含了目标field
	 * @param fieldList
	 * @param field 带检测field
	 * @return
	 */
	public static boolean isContain(ArrayList<Field> fieldList,Field field){
		for(Field temp:fieldList){
			if(temp.getName().equals(field.getName())){
				return true;
			}
		}
		return false;
	}
}
