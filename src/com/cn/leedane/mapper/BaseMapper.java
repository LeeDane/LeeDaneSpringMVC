package com.cn.leedane.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.cn.leedane.mybatis.SqlProvider;

/**
 * 基本mapper
 * @author LeeDane
 * 2016年7月13日 上午9:12:58
 * Version 1.0
 */
public interface BaseMapper<T> {
	
	@InsertProvider(type = SqlProvider.class, method = "insert")
    @Options(useGeneratedKeys=true)
	public int save(T bean);
	
	@InsertProvider(type = SqlProvider.class, method = "insert")
    @Options(useGeneratedKeys=true)
    public int insert(T bean);

    @DeleteProvider(type = SqlProvider.class, method = "delete")
    public int delete(T bean);

    @UpdateProvider(type = SqlProvider.class, method = "update")
    public int update(T bean);

    /*@SelectProvider(type = SqlProvider.class, method = "findFirst")
    public T findFirst(T bean);*/
    
    @SelectProvider(type = SqlProvider.class, method = "findById")
    public T findById(Class<T> clazz, int id);
    
    @SelectProvider(type = SqlProvider.class, method = "executeSQL")
    public List<Map<String, Object>> executeSQL(String sql, Object ...params);
}
