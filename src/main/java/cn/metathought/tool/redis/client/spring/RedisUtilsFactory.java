package cn.metathought.tool.redis.client.spring;

import cn.metathought.tool.redis.client.RedisClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class RedisUtilsFactory implements InitializingBean, DisposableBean, FactoryBean<RedisClient> {
	private RedisClient redisClient;
	private String host;
	private Integer port;
	private String pass;
	private Integer database=0;
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public Integer getDatabase() {
		return database;
	}

	public void setDatabase(Integer database) {
		this.database = database;
	}

	@Override
	public void destroy() throws Exception {
		redisClient.destroy();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(pass==null||pass.trim().equals("")){
			redisClient=RedisClient.getInstance(host, port);
		}else{
			redisClient=RedisClient.getInstance(host, port,pass,database);
		}
	}

	@Override
	public RedisClient getObject() throws Exception {
		return redisClient;
	}

	@Override
	public Class<?> getObjectType() {
		return RedisClient.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
