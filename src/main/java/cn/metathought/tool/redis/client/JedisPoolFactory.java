package cn.metathought.tool.redis.client;

import java.util.HashMap;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolFactory
{
  private static HashMap<String, JedisPool> poolMap = new HashMap<String, JedisPool>();
  
  public static JedisPool getPool(String address, int port, String pass,int database,int connTimeOutInMilliseconds)
  {
    synchronized (JedisPoolFactory.class)
    {
      String key = address + ":" + port;
      JedisPool pool = (JedisPool)poolMap.get(key);
      if (pool == null)
      {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(500);
        config.setMaxIdle(20);
        if(!pass.trim().equals("")&&pass!=null){
        	pool=new JedisPool(config, address, port, connTimeOutInMilliseconds,pass,database);
        }else{
        	pool = new JedisPool(config, address, port, connTimeOutInMilliseconds);
        }
        poolMap.put(key, pool);
      }
      return pool;
    }
  }
  public static void destory(){
    poolMap.clear();
  }
}
