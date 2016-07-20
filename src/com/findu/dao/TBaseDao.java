package com.findu.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Table;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.findu.dto.FUSearchPage;
import com.findu.model.TGenealogyMapId;
import com.findu.utils.SysParamUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@SuppressWarnings("unchecked")
@Repository
public abstract class TBaseDao<T> {
	private static Logger logger = Logger.getLogger(TBaseDao.class);
	
	private Class<T> entityClass;
	
	@Autowired
	public SessionFactory sessionFactory;
	@Autowired
	public ComboPooledDataSource dataSource;
	
	
	@SuppressWarnings("rawtypes")
	public TBaseDao() {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		entityClass = (Class) params[0];
	}
	
	public abstract String getTargetTableName(Object entity) throws Exception;
	
	public String getEntityTable(Object entity){
		try {
			Table annotation = entity.getClass().getAnnotation(Table.class);
			return annotation.name();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage());
			return null;
		}
	}
	
	public Session getSession(Object entity) {
		ThreadContext tc = ThreadContext.getThreadContext();

		if(tc.session == null){
			tc.session = sessionFactory.withOptions().interceptor(new MultiTableInterceptor()).openSession();
			tc.session.setCacheMode(CacheMode.NORMAL);			
		}
		
		setTheadContext(entity);
		return tc.session;
	}
	
	protected void setTheadContext(Object entity){
		ThreadContext tc = ThreadContext.getThreadContext();
		tc.dao = this;
		tc.entity = entity;
	}
	
	/**
	 * 根据id查询记录。
	 * @param entity，对象。
	 * @param id，对象ID。
	 * @return 返回完整对象。
	 */
	public T get(T entity, Serializable id) {
		return get(getSession(entity), entity, id);
	}
	
	public T get(Session session, T entity, Serializable id) {
		try {
			setTheadContext(entity);
			entity = (T) session.get(entityClass, id);
			session.flush();
		} catch (Exception e) {
			if(e.getCause() instanceof com.mysql.jdbc.exceptions.jdbc4.CommunicationsException){
				this.restSession();
//				session = this.getSession(entity);
//				entity = (T) session.get(entityClass, id);
			}
			logger.error(e.getMessage());
			entity = null;
		}
		
		return entity;
	}
	
	/**
	 * 根据id更新记录。
	 * @param entity，完整对象。
	 */
	public boolean update(T entity){
		return update(getSession(entity), entity);
	}
	
	public boolean update(Session session, T entity){
		try {
			setTheadContext(entity);
			session.clear();
			session.update(entity);
			session.flush();
		} catch (Exception e) {
			if(e.getCause() instanceof com.mysql.jdbc.exceptions.jdbc4.CommunicationsException){
				this.restSession();
//				session = this.getSession(entity);
//				session.update(entity);				
			}
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * 插入记录。
	 * @param entity，对象，不需要id和createTime。
	 * @return 返回完整对象。
	 */
	public T insert(T entity){
		return insert(getSession(entity), entity);
	}
	
	public T insert(Session session, T entity){
		try {
			setTheadContext(entity);
			session.save(entity);
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getCause() instanceof com.mysql.jdbc.exceptions.jdbc4.CommunicationsException){
				this.restSession();
//				session = this.getSession(entity);
//				session.save(entity);	
			}
			logger.error(e.getMessage());
			entity = null;
		}
		
		return entity;
	}
	
	/**
	 * 根据id删除记录。
	 * @param entity，对象。
	 */
	public boolean delete(T entity){
		return delete(getSession(entity), entity);
	}
	
	public boolean delete(Session session, T entity){
		try {
			setTheadContext(entity);
			session.delete(entity);
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getCause() instanceof com.mysql.jdbc.exceptions.jdbc4.CommunicationsException){
				this.restSession();
//				session = this.getSession(entity);
//				session.delete(entity);
			}
			logger.error(e.getMessage());
			return false;
		}
		
		return true;
	}
	
	/**
	 * 按条件找到记录。
	 * @param cond，条件。
	 * @param and，条件关系。
	 * @return 返回对象。
	 */
	public T find(T cond, boolean and){
		return find(getSession(cond), cond, and);
	}
	
	public T find(Session session, T cond, boolean and){
		T data = null;
		try {
			setTheadContext(cond);
			ArrayList<Field> condFields = new ArrayList<Field>();
			String hql = CreateSearchSql(cond, and, condFields);
			if(hql == null){
				logger.warn("create HQL error");
				return null;
			}
			
			Query query = CreateQueryByEntity(session, cond, hql, condFields);
			if(query == null){
				logger.warn("create Query eror");
				return null;
			}
			query.setCacheable(true);
			data = (T)query.uniqueResult();
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getCause() instanceof com.mysql.jdbc.exceptions.jdbc4.CommunicationsException){
				this.restSession();
			}
			logger.error(e.getMessage());
			data = null;
		}
		
		return data;
	}
	
	/**
	 * 按条件搜索所有记录。
	 * @param cond，条件。
	 * @param and，条件关系。
	 * @return，对象列表。
	 */
	public ArrayList<T> search(T cond, boolean and, ArrayList<String> orderFields, boolean desc, FUSearchPage page){
		return search(getSession(cond), cond, and, orderFields, desc, page);
	}
	
	public ArrayList<T> search(Session session, T cond, boolean and, ArrayList<String> orderFields, boolean desc, FUSearchPage page){
		ArrayList<T> data = null;
		try {
			setTheadContext(cond);
			ArrayList<Field> condFields = new ArrayList<Field>();
			String hql = CreateSearchSql(cond, and, condFields);
			if(hql == null){
				logger.warn("create HQL error");
				return null;
			}
			if(orderFields != null && orderFields.size() > 0){
				String orderBySql = "";
				for(String field : orderFields){
					if(orderBySql.trim().length() > 0)
						orderBySql += ",";
					orderBySql += String.format(" t.%s", field);
				}
				hql += String.format(" order by %s %s", orderBySql, desc ? " desc" : "asc");
			}			
			
			Query query = CreateQueryByEntity(session, cond, hql, condFields);
			if(query == null){
				logger.warn("create Query eror");
				return null;
			}
			query.setCacheable(true);
			if(page != null && page.getPageCount() > 0 && page.getPageNumber() >= 0 && page.getPageSize() > 0){
				int pageNum = page.getPageNumber() >= page.getPageCount() ? page.getPageCount() - 1 : page.getPageNumber();
				int pageSize = Integer.parseInt(SysParamUtils.PAGE_MAX_SIZE);
				if(pageSize > page.getPageSize())
					pageSize = page.getPageSize(); 
				query.setFirstResult(pageNum * pageSize);
				query.setMaxResults(pageSize);
			}			
			data = (ArrayList<T>) query.list();
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getCause() instanceof com.mysql.jdbc.exceptions.jdbc4.CommunicationsException){
				this.restSession();
			}
			logger.error(e.getMessage());
			data = null;
		}
		
		return data;
	}
	
	/**
	 * 按条件搜索所有记录。
	 * @param cond，条件。
	 * @param and，条件关系。
	 * @return，对象列表。
	 */
	public int searchCount(T cond, boolean and, ArrayList<String> orderFields, boolean desc){
		return searchCount(getSession(cond), cond, and, orderFields, desc);
	}
	
	public int searchCount(Session session, T cond, boolean and, ArrayList<String> orderFields, boolean desc){
		int count = -1;
		try {
			setTheadContext(cond);
			ArrayList<Field> condFields = new ArrayList<Field>();
			String hql = CreateSearchSql(cond, and, condFields);
			if(hql == null){
				logger.warn("create HQL error");
				return -1;
			}
			if(orderFields != null && orderFields.size() > 0){
				String orderBySql = "";
				for(String field : orderFields){
					if(orderBySql.trim().length() > 0)
						orderBySql += ",";
					orderBySql += String.format(" t.%s", field);
				}
				hql = String.format("select count(*) %s order by %s %s", hql, orderBySql, desc ? " desc" : "asc");
			}			
			
			Query query = CreateQueryByEntity(session, cond, hql, condFields);
			if(query == null){
				logger.warn("create Query eror");
				return -1;
			}
			count = ((Long)query.iterate().next()).intValue();
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getCause() instanceof com.mysql.jdbc.exceptions.jdbc4.CommunicationsException){
				this.restSession();
			}
			logger.error(e.getMessage());
			return -1;
		}
		
		return count;
	}
	
	/**
	 * 按条件删除所有记录。
	 * @param cond，条件。
	 * @param and，条件关系。
	 * @return 返回删除记录数。
	 */
	public int remove(T cond, boolean and){
		return remove(getSession(cond), cond, and);
	}
	
	public int remove(Session session, T cond, boolean and){
		int rows = -1;
		try {
			setTheadContext(cond);
			ArrayList<Field> condFields = new ArrayList<Field>();
			String hql = CreateSearchSql(cond, and, condFields);
			if(hql == null){
				logger.warn("create HQL error");
				return -1;
			}
			hql = "delete " + hql;
			Query query = CreateQueryByEntity(session, cond, hql, condFields);
			if(query == null){
				logger.warn("create Query eror");
				return -1;
			}
			rows = query.executeUpdate();
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getCause() instanceof com.mysql.jdbc.exceptions.jdbc4.CommunicationsException){
				this.restSession();
			}
			logger.error(e.getMessage());
			rows = -1;
		}		
		
		return rows;
	}
	
	public String CreateSearchSql(T cond, boolean and, ArrayList<Field> fieldList){
		String hql = null;
		try {
			hql = String.format("from %s t", entityClass.getSimpleName());
			Field fields[] = entityClass.getDeclaredFields();
			String where = "";
			for(Field f : fields){
				if(Modifier.isStatic(f.getModifiers()))
					continue;
				Object val = PropertyUtils.getProperty(cond, f.getName());
				if(val == null)
					continue;
				String type = f.getType().getSimpleName();
				if((type.equals("long") || type.equals("int"))
						&& Long.parseLong(val.toString()) <= 0)
					continue;
				if(f.getType() == TGenealogyMapId.class){
					TGenealogyMapId mapId = (TGenealogyMapId)PropertyUtils.getProperty(cond, f.getName());
					if(mapId.getUserId() > 0){
						if(where.length() > 0){
							if(and)
								where += " and";
							else
								where += " or";
						}
						where += String.format(" t.id.userId = %d", mapId.getUserId());
					}
					if(mapId.getGenealogyId() > 0){
						if(where.length() > 0){
							if(and)
								where += " and";
							else
								where += " or";
						}
						where += String.format(" t.id.genealogyId = %d", mapId.getGenealogyId());
					}
				}else{
					fieldList.add(f);
					if(where.length() > 0){
						if(and)
							where += " and";
						else
							where += " or";
					}
					where += String.format(" t.%s=?", f.getName());
				}				
			}
			if(where.trim().length() > 0)
				hql += " where" + where;
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getCause() instanceof com.mysql.jdbc.exceptions.jdbc4.CommunicationsException){
				this.restSession();
			}
			logger.error(e.getMessage());
			return null;
		}
		return hql;
	}
	
	public Query CreateQueryByEntity(Session session, T cond, String hql, ArrayList<Field> condFields){
		Query query = null;
		try {
			query = session.createQuery(hql);
			for (int i = 0; i < condFields.size(); i++) {
				Field f = condFields.get(i);
				switch (f.getType().getSimpleName().toLowerCase()) {
				case "long":
					query.setLong(i, (Long) PropertyUtils.getProperty(cond, f.getName()));
					break;
				case "int":
				case "integer":
					query.setInteger(i, (Integer) PropertyUtils.getProperty(cond, f.getName()));
					break;
				case "string":
					query.setString(i, (String) PropertyUtils.getProperty(cond, f.getName()));
					break;
				case "date":
					query.setTimestamp(i, (Date) PropertyUtils.getProperty(cond, f.getName()));
					break;
				default:
					logger.warn(String.format("data type %s is unsupported", f.getType().getSimpleName()));
					return null;
				}
			} 
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage());
			return null;
		}
		return query;
	}
	
	protected void restSession(){
		dataSource = new ComboPooledDataSource("dataSource");
		ThreadContext tc = ThreadContext.getThreadContext();
		tc.session.close();
		tc.session = null;
	}
}
