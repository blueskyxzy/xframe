package com.xzy.xframe.redis;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @desc RedisUtil
 */
@Slf4j
@AllArgsConstructor
public class RedisUtil {

    private RedisTemplate<String, Object> redisTemplate;

    private HashOperations<String, String, Object> opsForHash() {
        return redisTemplate.opsForHash();
    }

    //=============================common============================

    /**
     * 指定缓存失效时间,默认秒
     */
    public boolean expire(String key, long time) {
        return expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 指定缓存失效时间
     */
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        if (time > 0) {
            redisTemplate.expire(key, time, timeUnit);
            return true;
        }
        return false;
    }

    /**
     * 根据 key 获取过期时间,默认秒
     */
    public long getExpire(String key) {
        return getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 根据 key 获取过期时间
     */
    public Long getExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 匹配查询redis中的key,需要加上*
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 获取当前database
     */
    public Integer database() {
        LettuceConnectionFactory factory = (LettuceConnectionFactory) redisTemplate.getConnectionFactory();
        if (null == factory) {
            return null;
        }
        return factory.getDatabase();
    }

    /**
     * 集合数量
     */
    public Long dbSize() {
        return redisTemplate.execute(RedisServerCommands::dbSize);
    }

    /**
     * 清空DB
     */
    public void flushDb(RedisClusterNode node) {
        this.redisTemplate.opsForCluster().flushDb(node);
    }

    //============================String=============================

    /**
     * 普通缓存获取
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存获取
     */
    public String getString(String key) {
        if (key == null) {
            return null;
        }
        Object obj = redisTemplate.opsForValue().get(key);
        if (null == obj) {
            return null;
        }
        return String.valueOf(obj);
    }

    /**
     * 普通缓存放入
     */
    public boolean set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
        return true;
    }

    /**
     * 普通缓存放入并设置时间秒,小于等于0将设置无限期
     */
    public boolean set(String key, Object value, long time) {
        return set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 普通缓存放入并设置时间
     */
    public boolean set(String key, Object value, long time, TimeUnit timeUnit) {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, timeUnit);
        } else {
            return set(key, value);
        }
        return true;
    }

    /**
     * 递增
     */
    public Long incr(String key, long delta) {
        if (delta < 0) {
            log.error("递增因子必须大于0");
            return null;
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     */
    public Long decr(String key, long delta) {
        if (delta < 0) {
            log.error("递减因子必须大于0");
            return null;
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * 判断 key 是否存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除缓存
     */
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    /**
     * 模糊删除缓存
     */
    public void deleteByBlur(String... key) {
        if (key == null || key.length == 0) {
            return;
        }
        for (String k : key) {
            if (!k.endsWith("*")) {
                k += "*";
            }
            redisTemplate.delete(keys(k));
        }
    }

    //================================Map=================================

    /**
     * HashGet
     */
    public Object hGet(String key, Objects item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     */
    public Map<String, Object> hGetMap(String key) {
        return opsForHash().entries(key);
    }

    /**
     * HashSet
     */
    public boolean hSetMap(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
        return true;
    }

    /**
     * HashSet 并设置时间秒
     */
    public boolean hSetMap(String key, Map<String, Object> map, long time) {
        redisTemplate.opsForHash().putAll(key, map);
        if (time > 0) {
            expire(key, time);
        }
        return true;
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     */
    public boolean hSet(String key, String item, Object value) {
        redisTemplate.opsForHash().put(key, item, value);
        return true;
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建,时间(秒)
     */
    public boolean hSet(String key, String item, Object value, long time) {
        redisTemplate.opsForHash().put(key, item, value);
        if (time > 0) {
            expire(key, time);
        }
        return true;
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     */
    public double hIncr(String key, String item, double delta) {
        return redisTemplate.opsForHash().increment(key, item, delta);
    }

    /**
     * hash递减
     */
    public double hDecr(String key, String item, double delta) {
        return redisTemplate.opsForHash().increment(key, item, -delta);
    }

    /**
     * 判断hash表中是否有该项的值
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * 删除hash表中的值
     */
    public Long hDelete(String key, Object... item) {
        return redisTemplate.opsForHash().delete(key, item);
    }

    //============================Set=============================

    /**
     * 根据key获取Set中的所有值
     */
    public Set<Object> sGet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 获取set缓存的长度
     */
    public Long sGetSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 将数据放入set缓存
     */
    public Long sSet(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 将set数据放入缓存,时间(秒)
     */
    public Long sSet(String key, Object[] values, long time) {
        Long count = redisTemplate.opsForSet().add(key, values);
        if (time > 0) {
            expire(key, time);
        }
        return count;
    }

    /**
     * 根据value从一个set中查询,是否存在
     */
    public Boolean sHasKey(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 移除值为value的
     */
    public Long sDelete(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    //===============================List=================================

    /**
     * 获取list缓存的内容,end=-1代表所有值
     */
    public List<Object> lGet(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 通过 index 获取list中的值
     */
    public Object lGet(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 获取list缓存的长度
     */
    public Long lGetSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 将list放入缓存
     */
    public Long lSet(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 将list放入缓存,时间(秒)
     */
    public boolean lSet(String key, Object value, long time) {
        redisTemplate.opsForList().rightPush(key, value);
        if (time > 0) {
            expire(key, time);
        }
        return true;
    }

    /**
     * 将list放入缓存
     */
    public Long lSetList(String key, List<Object> value) {
        return redisTemplate.opsForList().rightPushAll(key, value);
    }

    /**
     * 将list放入缓存,时间(秒)
     */
    public boolean lSetList(String key, List<Object> value, long time) {
        redisTemplate.opsForList().rightPushAll(key, value);
        if (time > 0) {
            expire(key, time);
        }
        return true;
    }

    /**
     * 根据 index 修改 list 中的某条数据
     */
    public boolean lUpdate(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
        return true;
    }

    /**
     * 移除N个值为value
     */
    public Long lDelete(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

}
