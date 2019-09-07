package cn.metathought.tool.redis.client;

import cn.metathought.tool.redis.exception.RedisOperationException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.params.SetParams;

public class RedisClient {

    private static Map<String, RedisClient> clientMap = new HashMap<String, RedisClient>();
    private String ip;
    private int port;
    private String pass;
    private int database;
    private JedisPool pool;

    private RedisClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.database = 0;
        this.pool = getJedisPool(this.ip, this.port, this.database);
    }

    private RedisClient(String ip, int port, String pass) {
        this.ip = ip;
        this.port = port;
        this.pass = pass;
        this.database = 0;
        this.pool = getJedisPool(this.ip, this.port, this.pass, this.database);
    }

    private RedisClient(String ip, int port, String pass, int database) {
        this.ip = ip;
        this.port = port;
        this.pass = pass;
        this.database = database;
        this.pool = getJedisPool(this.ip, this.port, this.pass, this.database);
    }

    private void dealException(Jedis jedis, Exception e, String key) throws RedisOperationException {
        if (jedis != null) {
            try {
                jedis.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        throw new RedisOperationException(e.getMessage() + "[" + key + "]");
    }

    private synchronized Jedis getJedis(JedisPool pool) throws RedisOperationException {
        if (pool == null) {
            throw new RedisOperationException("get JedisPool failed");
        }
        Jedis jedis = pool.getResource();
        if (jedis == null) {
            throw new RedisOperationException("get jedis failed");
        }
        return jedis;
    }

    public static synchronized RedisClient getInstance(String ip, int port) {
        String key = ip + ":" + port;
        if (!clientMap.containsKey(key)) {
            RedisClient client = new RedisClient(ip, port);
            clientMap.put(key, client);
        }
        return (RedisClient) clientMap.get(key);
    }

    public static synchronized RedisClient getInstance(String ip, int port, String pass) {
        String key = ip + ":" + port;
        if (!clientMap.containsKey(key)) {
            RedisClient client = new RedisClient(ip, port, pass);
            clientMap.put(key, client);
        }
        return (RedisClient) clientMap.get(key);
    }

    public static synchronized RedisClient getInstance(String ip, int port, String pass, int database) {
        String key = ip + ":" + port;
        if (!clientMap.containsKey(key)) {
            RedisClient client = new RedisClient(ip, port, pass, database);
            clientMap.put(key, client);
        }
        return (RedisClient) clientMap.get(key);
    }

    public JedisPool getJedisPool() {
        return this.pool;
    }

    public static JedisPool getJedisPool(String ip, int port, int database) {
        return getJedisPool(ip, port, database, 2000);
    }

    public static JedisPool getJedisPool(String ip, int port, String pass, int database) {
        return getJedisPool(ip, port, pass, database, 2000);
    }

    public static JedisPool getJedisPool(String ip, int port, int database, int connTimeOutInMilliseconds) {
        return JedisPoolFactory.getPool(ip, port, "", database, connTimeOutInMilliseconds);
    }

    public static JedisPool getJedisPool(String ip, int port, String pass, int database, int connTimeOutInMilliseconds) {
        return JedisPoolFactory.getPool(ip, port, pass, database, connTimeOutInMilliseconds);
    }

    /**
     * @param expireTime 单位秒，不过期填0
     */
    public void set(String key, String value, int expireTime) throws RedisOperationException {
        Jedis jedis = null;
        try {
            JedisPool pool = getJedisPool();
            jedis = getJedis(pool);
            if (expireTime <= 0) {
                jedis.set(key, value);
            } else {
                jedis.setex(key, expireTime, value);
            }
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
    }

    /**
     * 设置key value ，没有过期时间
     */
    public void set(String key, String value) throws RedisOperationException {
        Jedis jedis = null;
        try {
            JedisPool pool = getJedisPool();
            jedis = getJedis(pool);
            jedis.set(key, value);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
    }

    /**
     * 添加一个byte数组
     */
    public void set(String key, byte[] value) throws RedisOperationException, UnsupportedEncodingException {
        Jedis jedis = null;
        try {
            JedisPool pool = getJedisPool();
            jedis = getJedis(pool);
            jedis.set(key.getBytes("utf-8"), value);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
    }

    /**
     * 添加一个byte数组，并且设置一个过期时间，如不过期时间为0
     */
    public void set(String key, byte[] value, int expireTime) throws RedisOperationException, UnsupportedEncodingException {
        Jedis jedis = null;
        try {
            JedisPool pool = getJedisPool();
            jedis = getJedis(pool);
            jedis.setex(key.getBytes("utf-8"), expireTime, value);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
    }

    /**
     * 根据key获取一个字符串类型的值
     */
    public String get(String key) throws RedisOperationException {
        Jedis jedis = null;
        String value = null;
        try {
            JedisPool pool = getJedisPool();
            jedis = getJedis(pool);
            value = jedis.get(key);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return value;
    }

    /**
     * 获取一个数组
     */
    public byte[] getBytes(String key) throws RedisOperationException, UnsupportedEncodingException {
        Jedis jedis = null;
        byte[] value = null;
        try {
            JedisPool pool = getJedisPool();
            jedis = getJedis(pool);
            value = jedis.get(key.getBytes("utf-8"));
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return value;
    }

    /**
     * 根据key进行自增操作并返回自增后的值
     */
    public Long incrString(String key) throws RedisOperationException {
        JedisPool pool = null;
        Jedis jedis = null;
        Long incr = Long.valueOf(0L);
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            incr = jedis.incr(key);
            jedis.close();
            return incr;
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return Long.valueOf(0L);
    }

    /**
     * 根据key进行自减操作，并返回自减操作后的值
     */
    public Long decrString(String key) throws RedisOperationException {
        JedisPool pool = null;
        Jedis jedis = null;
        Long decr = Long.valueOf(0L);
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            decr = jedis.decr(key);
            jedis.close();
            return decr;
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return Long.valueOf(0L);
    }

    public void zadd(String key, Map<String, Double> scoreMembers) throws RedisOperationException {
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            jedis.zadd(key, scoreMembers);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
    }

    public void zadd(String key, Double score, String member) throws RedisOperationException {
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            jedis.zadd(key, score.doubleValue(), member);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
    }

    public Long zcard(String key) throws RedisOperationException {
        JedisPool pool = null;
        Jedis jedis = null;
        Long count = Long.valueOf(0L);
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            count = jedis.zcard(key);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return count;
    }

    public Set<String> zrange(String key, double minScore, double maxScore) throws RedisOperationException {
        Set<String> set = new HashSet<String>();
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            set = jedis.zrangeByScore(key, minScore, maxScore);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return set;
    }

    public Set<String> zrange(String key, int start, int end, boolean desc) throws RedisOperationException {
        Set<String> set = new HashSet<String>();
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            if (desc) {
                set = jedis.zrevrange(key, start, end);
            } else {
                set = jedis.zrange(key, start, end);
            }
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return set;
    }

    public Boolean zrem(String key, String... members) throws RedisOperationException {
        Boolean flag = Boolean.valueOf(false);
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            flag = Boolean.valueOf(jedis.zrem(key, members).longValue() > 0L);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return flag;
    }

    public LinkedHashMap<String, Double> zrangeWithScores(String key, int start, int end, boolean desc)
        throws RedisOperationException {
        LinkedHashMap<String, Double> map = new LinkedHashMap<String, Double>();
        Set<Tuple> set = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            if (desc) {
                set = jedis.zrevrangeWithScores(key, start, end);
            } else {
                set = jedis.zrangeWithScores(key, start, end);
            }
            if ((set != null) && (set.size() > 0)) {
                for (Tuple t : set) {
                    map.put(t.getElement(), Double.valueOf(t.getScore()));
                }
            }
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return map;
    }

    public LinkedHashMap<String, Double> zrangeByScoreWithScores(String key) throws RedisOperationException {
        JedisPool pool = null;
        Jedis jedis = null;
        LinkedHashMap<String, Double> map = new LinkedHashMap<String, Double>();
        Set<Tuple> set = new HashSet<Tuple>();
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            set = jedis.zrangeByScoreWithScores(key, 0.0D, 2.147483647E9D);
            if ((set != null) && (set.size() > 0)) {
                for (Tuple t : set) {
                    map.put(t.getElement(), Double.valueOf(t.getScore()));
                }
            }
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return map;
    }

    public LinkedHashMap<String, Double> zrangeByScoreWithScores(String key, double start, double end, boolean desc)
        throws RedisOperationException {
        JedisPool pool = null;
        Jedis jedis = null;
        LinkedHashMap<String, Double> map = new LinkedHashMap<String, Double>();
        Set<Tuple> set = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            set = jedis.zrangeByScoreWithScores(key, start, end);
            if ((set != null) && (set.size() > 0)) {
                int i;
                if (desc) {
                    List<Tuple> tmp = new ArrayList<Tuple>(set);
                    for (i = tmp.size() - 1; i >= 0; i--) {
                        map.put(((Tuple) tmp.get(i)).getElement(), Double.valueOf(((Tuple) tmp.get(i)).getScore()));
                    }
                } else {
                    for (Tuple t : set) {
                        map.put(t.getElement(), Double.valueOf(t.getScore()));
                    }
                }
            }
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return map;
    }

    public Double zscore(String key, String member) throws RedisOperationException {
        JedisPool pool = null;
        Jedis jedis = null;
        Double value = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            value = jedis.zscore(key, member);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return value;
    }
    public Long zrank (String key,String member) throws RedisOperationException {
        JedisPool pool=null;
        Jedis jedis=null;
        Long value=0L;
        try{
            pool=getJedisPool();
            jedis=getJedis(pool);
            value=jedis.zrank(key,member);
            jedis.close();
        }catch (Exception e){
            dealException(jedis,e,key);
        }
        return value;
    }
    public Long zrevrank (String key,String member) throws RedisOperationException {
        JedisPool pool=null;
        Jedis jedis=null;
        Long value=0L;
        try{
            pool=getJedisPool();
            jedis=getJedis(pool);
            value=jedis.zrevrank(key,member);
            jedis.close();
        }catch (Exception e){
            dealException(jedis,e,key);
        }
        return value;
    }
    public void rpush(String key, List<String> list) throws RedisOperationException {
        if ((list == null) || (list.size() == 0)) {
            throw new RedisOperationException("null  value ");
        }
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            String[] strings = new String[list.size()];
            list.toArray(strings);
            jedis.rpush(key, strings);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
    }

    public void lpush(String key, String item) throws RedisOperationException {
        if (item == null) {
            throw new RedisOperationException("null  value ");
        }
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            jedis.lpush(key, new String[]{item});
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
    }

    public List<String> lrange(String key, int begin, int end) throws RedisOperationException {
        JedisPool pool = null;
        Jedis jedis = null;
        List<String> result = new ArrayList<String>();
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            result = jedis.lrange(key, begin, end);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return result;
    }

    public Long llen(String key) throws RedisOperationException {
        Long count = Long.valueOf(0L);
        if (StringUtils.isBlank(key)) {
            throw new RedisOperationException("null  key ");
        }
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            count = jedis.llen(key);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return count;
    }

    public Boolean sadd(String key, String member) throws RedisOperationException {
        Boolean flag = Boolean.valueOf(false);
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            flag = Boolean.valueOf(jedis.sadd(key, new String[]{member}).longValue() == 1L);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return flag;
    }

    public Set<String> smembers(String key) throws RedisOperationException {
        Set<String> set = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            set = jedis.smembers(key);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return set;
    }

    public Boolean srem(String key, String... member) throws RedisOperationException {
        Boolean flag = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            flag = Boolean.valueOf(jedis.srem(key, member).longValue() == 1L);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return flag;
    }

    public Boolean sismember(String key, String member) throws RedisOperationException {
        JedisPool pool = null;
        Jedis jedis = null;
        Boolean flag = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            flag = jedis.sismember(key, member);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return flag;
    }

    public Long scard(String key) throws RedisOperationException {
        JedisPool pool = null;
        Jedis jedis = null;
        Long count = Long.valueOf(0L);
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            count = jedis.scard(key);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return count;
    }

    public String hget(String key, String field) throws RedisOperationException {
        JedisPool pool = null;
        Jedis jedis = null;
        String value = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            value = jedis.hget(key, field);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return value;
    }

    public Map<String, String> hgetAll(String key) throws RedisOperationException {
        JedisPool pool = null;
        Jedis jedis = null;
        Map<String, String> map = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            map = jedis.hgetAll(key);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return map;
    }

    public void hset(String key, String field, String value) throws RedisOperationException {
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            jedis.hset(key, field, value);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
    }

    public Long hincrBy(String key, String field) throws RedisOperationException {
        Jedis jedis = null;
        Long value = Long.valueOf(0L);
        try {
            JedisPool pool = getJedisPool();
            jedis = getJedis(pool);
            value = jedis.hincrBy(key, field, 1L);
            jedis.close();
            return value;
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return value;
    }

    public Long hdecrBy(String key, String field) throws RedisOperationException {
        Jedis jedis = null;
        Long value = Long.valueOf(0L);
        try {
            JedisPool pool = getJedisPool();
            jedis = getJedis(pool);
            value = jedis.hincrBy(key, field, -1L);
            jedis.close();
            return value;
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return value;
    }

    public void hdel(String key, String... field) throws RedisOperationException {
        Jedis jedis = null;
        try {
            JedisPool pool = getJedisPool();
            jedis = getJedis(pool);
            jedis.hdel(key, field);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
    }

    public Map<String, String> mget(List<String> keys) throws RedisOperationException {
        Map<String, String> result = new HashMap<String, String>();
        if ((keys == null) || (keys.size() == 0)) {
            throw new RedisOperationException("keys null");
        }
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            String[] strings = new String[keys.size()];
            keys.toArray(strings);
            List<String> values = jedis.mget(strings);
            if ((values == null) || (values.size() != keys.size())) {
                throw new RedisOperationException(
                    String.format("mget from redis error: values.size = [%d],but keys.size=[%d]",
                        new Object[]{Integer.valueOf(values.size()), Integer.valueOf(keys.size())}));
            }
            for (int i = 0; i < keys.size(); i++) {
                result.put((String) keys.get(i), (String) values.get(i));
            }
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, keys.toString());
        }
        return result;
    }

    public void mset(Map<String, String> keyValues) throws RedisOperationException {
        if ((keyValues == null) || (keyValues.size() == 0)) {
            throw new RedisOperationException("keyValues null");
        }
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            List<String> strList = new ArrayList<String>();
            for (Map.Entry<String, String> entry : keyValues.entrySet()) {
                strList.add((String) entry.getKey());
                strList.add((String) entry.getValue());
            }
            String[] kvs = new String[strList.size()];
            strList.toArray(kvs);
            jedis.mset(kvs);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, keyValues.toString());
        }
    }

    public void del(String key) throws RedisOperationException {
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            jedis.del(new String[]{key});
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
    }


    public Boolean expire(String key, Integer seconds) throws RedisOperationException {
        JedisPool pool = null;
        Jedis jedis = null;
        Boolean flag = Boolean.valueOf(false);
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            flag = Boolean.valueOf(jedis.expire(key, seconds.intValue()).longValue() == 1L);
            jedis.close();
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return flag;
    }


    public Long setnx(String key, String value) throws RedisOperationException {
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            Long nx = jedis.setnx(key, value);
            jedis.close();
            return nx;
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return Long.valueOf(0L);
    }

    public Long setnx(String key, String value, Integer expire) throws RedisOperationException {
        JedisPool pool;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            SetParams params = new SetParams();
            params.nx();
            params.ex(expire);
            String str = jedis.set(key, value, params);
            Long nx = (StringUtils.isNotEmpty(str) && str.equals("OK")) ? 1L : 0L;
            jedis.close();
            return nx;
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return Long.valueOf(0L);
    }

    public List<String> sort(String key, String sortParam, boolean desc, Object limitObject, Integer limitTo)
        throws RedisOperationException {
        JedisPool pool = null;
        Jedis jedis = null;
        List<String> list = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            SortingParams params = null;
            params = new SortingParams();
            if (desc) {
                params.desc();
            } else {
                params.asc();
            }
            if ((!sortParam.equals("")) && (sortParam != null)) {
                params.by(sortParam);
            }
            list = jedis.sort(key, params);
            if ((list != null) && (list.size() > 0)) {
                Integer index = Integer.valueOf(list.indexOf(limitObject.toString()) + 1);
                if ((index.equals(Integer.valueOf(0))) && (!limitObject.toString().equals("0"))) {
                    list = null;
                } else {
                    Integer count = Integer.valueOf(index.intValue() + limitTo.intValue() > list.size() ? list.size()
                        : index.intValue() + limitTo.intValue());
                    list = list.subList(index.intValue(), count.intValue());
                }
            }
            jedis.close();
            return list;
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return list;
    }

    public Set<String> keys(String pattern) throws RedisOperationException {
        JedisPool pool = null;
        Jedis jedis = null;
        Set<String> set = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            set = jedis.keys(pattern);
            jedis.close();
            return set;
        } catch (Exception e) {
            dealException(jedis, e, pattern);
        }
        return null;
    }

    public ScanResult<String> scan(String pattern) throws RedisOperationException {
        JedisPool pool;
        Jedis jedis = null;
        ScanResult<String> set;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            ScanParams params = new ScanParams();
            params.match(pattern);
            set = jedis.scan("0", params);
            jedis.close();
            return set;
        } catch (Exception e) {
            dealException(jedis, e, pattern);
        }
        return null;
    }

    public Long ttl(String key) throws RedisOperationException {
        Jedis jedis = null;
        try {
            JedisPool pool = getJedisPool();
            jedis = getJedis(pool);
            long ttl = jedis.ttl(key).longValue();
            jedis.close();
            return Long.valueOf(ttl);
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return null;
    }


    public boolean exists(String key) throws RedisOperationException {
        Jedis jedis = null;
        try {
            JedisPool pool = getJedisPool();
            jedis = getJedis(pool);
            boolean exists = jedis.exists(key).booleanValue();
            jedis.close();
            return exists;
        } catch (Exception e) {
            dealException(jedis, e, key);
        }
        return false;
    }

    public void destroy() {
        try {
            pool.destroy();
            clientMap.clear();
            JedisPoolFactory.destory();
        } catch (Exception ex) {

        }
        pool = null;
    }

    public Long incr(String key) throws Exception {
        Jedis jedis = null;
        try {
            JedisPool pool = getJedisPool();
            jedis = getJedis(pool);
            return jedis.incr(key.getBytes("UTF-8"));
        } finally {
            jedis.close();
        }
    }

    public String getDayIncrValue(String prefix, int num) {
        int v = 1;
        try {
            String key = format(new Date(), "yyyyMMdd");
            v = incr(prefix + key).intValue();
        } catch (Exception e) {
            throw new RuntimeException("get code error");
        } finally {
            if (v == 1) {
                try {
                    expire(prefix, 60 * 60 * 24);//如果是1 表示第一次调用,则设置过期时间24H
                } catch (Exception e) {
                    throw new RuntimeException("get code error");
                }
            }
        }
        String str = String.format("%0" + num + "d", v);
        return str;
    }

    public static String format(Date d, String f) {
        SimpleDateFormat sdf = new SimpleDateFormat(f);
        return sdf.format(d);
    }

    public String getIp() {
        return ip;
    }

    public static void main(String[] args) {
        RedisClient redisClient=RedisClient.getInstance("123.207.150.73",6459,"mYredis196",3);
        try {
            redisClient.zadd("doctor:order:sort",1d,"1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
