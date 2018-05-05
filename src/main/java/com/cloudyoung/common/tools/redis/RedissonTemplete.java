package com.cloudyoung.common.tools.redis;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBatch;
import org.redisson.api.RBucket;
import org.redisson.api.RBuckets;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RQueue;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RScript;
import org.redisson.api.RScript.Mode;
import org.redisson.api.RScript.ReturnType;
import org.redisson.api.RSet;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;

/**
 * Description: Redisson 模板类 
 * All Rights Reserved.
 * @param <V>
 * @version 1.0  2017年4月23日 下午12:05:29  by 代鹏（daipeng.456@gmail.com）创建
 */
public class RedissonTemplete<V> {

    private RedissonConfig redissonConfig;

    private RedissonClient redisson;

    public RedissonTemplete<V> setRedissonConfig(RedissonConfig config) {
        this.redissonConfig = config;
        this.redisson = redissonConfig.getRedissonInstance();
        return this;
    }

    /**
     * Description: String
     * @Version1.0 2017年5月2日 下午8:45:56 by 代鹏（daipeng.456@gmail.com）创建
     * @param objectName
     * @return
     */
    private RBucket<V> getRBucket(String objectName) {
        return redisson.getBucket(objectName);
    }

    /**
     * Description: Hash
     * @Version1.0 2017年5月2日 下午8:46:06 by 代鹏（daipeng.456@gmail.com）创建
     * @param objectName
     * @return
     */
    @SuppressWarnings("hiding")
    private <K, V> RMap<K, V> getRMap(String objectName) {
        return redisson.getMap(objectName);
    }

    /**
     * Description: SortSet
     * @Version1.0 2017年5月4日 上午11:45:52 by 代鹏（daipeng.456@gmail.com）创建
     * @param objectName
     * @return
     */
    private RScoredSortedSet<V> getRScoredSortedSet(String objectName) {
        return redisson.getScoredSortedSet(objectName);
    }

    /**
     * Description: List
     * @Version1.0 2017年5月2日 下午8:46:38 by 代鹏（daipeng.456@gmail.com）创建
     * @param objectName
     * @return
     */
    private RList<V> getRList(String objectName) {
        return redisson.getList(objectName);
    }
    
    /**
     * Description:list queue
     * @Version1.0 2018年1月11日 下午8:28:37 by 代鹏（daipeng.456@gmail.com）创建
     * @param objectName
     * @return
     */
    private RQueue<V> getRQueue(String objectName){
        return redisson.getQueue(objectName);
    }

    /**
     * Description: pipelining
     * @Version1.0 2017年5月2日 下午8:47:40 by 代鹏（daipeng.456@gmail.com）创建
     * @return
     */
    private RBatch getRBatch() {
        return redisson.createBatch();
    }

    /**
     * Description: RBuckets
     * @Version1.0 2017年5月2日 下午9:55:53 by 代鹏（daipeng.456@gmail.com）创建
     * @return
     */
    private RBuckets getRBuckets() {
        return redisson.getBuckets();
    }

    /**
     * Description: RAtomicLong
     * @Version1.0 2017年5月3日 上午10:14:52 by 代鹏（daipeng.456@gmail.com）创建
     * @return
     */
    private RAtomicLong getRAtomicLong(String objectName) {
        return redisson.getAtomicLong(objectName);
    }

    /**
     * Description: RScript
     * @Version1.0 2017年5月25日 下午2:34:10 by 代鹏（daipeng.456@gmail.com）创建
     * @return
     */
    private RScript getRScript() {
        return redisson.getScript();
    }
    
    /**
     * Description: 获取RTopic
     * @Version1.0 2018年3月8日 下午5:28:37 by 代鹏（daipeng.456@gmail.com）创建
     * @param objectName
     * @return
     */
    private <M> RTopic<M> getRTopic(String objectName) {
        return redisson.getTopic(objectName);
    }
    
    /**
     * Description: 获取RSet 
     * @Version1.0 2018年3月15日 下午9:15:37 by 邹立强   (zoulq@cloud-young.com)创建
     * @param objectName
     * @return
     */
    private <M> RSet<M> getSet(String objectName){
         return redisson.getSet(objectName);
    }
    
    // -------------------------String Start---------------------------//
    public void set(String key, V value) {
        RBucket<V> bucket = this.getRBucket(key);
        bucket.set(value);
    }

    public void set(String key, V value, long timeToLive, TimeUnit timeUnit) {
        RBucket<V> bucket = this.getRBucket(key);
        bucket.set(value, timeToLive, timeUnit);
    }

    public V get(String key) {
        RBucket<V> bucket = this.getRBucket(key);
        return bucket.get();
    }

    public boolean del(String key) {
        RBucket<V> bucket = this.getRBucket(key);
        return bucket.delete();
    }

    public boolean expire(String key, long timeToLive, TimeUnit timeUnit) {
        RBucket<V> bucket = this.getRBucket(key);
        return bucket.expire(timeToLive, timeUnit);
    }

    public boolean isExists(String key) {
        RBucket<V> bucket = this.getRBucket(key);
        return bucket.isExists();
    }

    public long ttl(String key) {
        RBucket<V> bucket = this.getRBucket(key);
        return bucket.remainTimeToLive();
    }

    public boolean setnx(String key, V value) {
        RBucket<V> bucket = this.getRBucket(key);
        return bucket.trySet(value);
    }

    public boolean setnx(String key, V value, long timeToLive, TimeUnit timeUnit) {
        RBucket<V> bucket = this.getRBucket(key);
        return bucket.trySet(value, timeToLive, timeUnit);
    }

    public void mset(Map<String, V> maps) {
        if (null != maps && !maps.isEmpty()) {
            RBatch batch = this.getRBatch();
            for (Map.Entry<String, V> entry : maps.entrySet()) {
                batch.getBucket(entry.getKey()).setAsync(entry.getValue());
            }
            batch.execute();
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Long> getTtls(Collection<String> keys){
        if(CollectionUtils.isNotEmpty(keys)){
            RBatch batch = this.getRBatch();
            for (String key : keys) {
                batch.getBucket(key).remainTimeToLiveAsync();
            }
             return (List<Long>) batch.execute();
        }
        return null;
    }

    public void mset(Map<String, V> maps, long timeToLive, TimeUnit timeUnit) {
        if (null != maps && !maps.isEmpty()) {
            RBatch batch = this.getRBatch();
            for (Map.Entry<String, V> entry : maps.entrySet()) {
                batch.getBucket(entry.getKey()).setAsync(entry.getValue(), timeToLive, timeUnit);
            }
            batch.execute();
        }
    }

    public Map<String, V> mget(Collection<String> keys) {
        if (null != keys && !keys.isEmpty()) {
            RBuckets buckets = getRBuckets();
            return buckets.get(keys.toArray(new String[] {}));
        }
        return null;
    }

    public long incr(String key) {
        RAtomicLong rAtomicLong = this.getRAtomicLong(key);
        return rAtomicLong.incrementAndGet();
    }

    public long incrby(String key, long delta) {
        RAtomicLong rAtomicLong = this.getRAtomicLong(key);
        rAtomicLong.getAndAdd(delta);
        return rAtomicLong.get();
    }

    public long getIncrbyValue(String key) {
        RAtomicLong rAtomicLong = this.getRAtomicLong(key);
        return rAtomicLong.get();
    }
    // -------------------------String End---------------------------//

    // -------------------------Hash Start---------------------------//
    @SuppressWarnings("hiding")
    public <K, V> void hset(String key, K field, V value) {
        RMap<K, V> rmap = this.getRMap(key);
        rmap.put(field, value);
    }

    @SuppressWarnings("hiding")
    public <K, V> void hmset(String key, Map<? extends K, ? extends V> map) {
        RMap<K, V> rmap = this.getRMap(key);
        rmap.putAll(map);
    }

    @SuppressWarnings("hiding")
    public <K, V> V hget(String key, K field) {
        RMap<K, V> rmap = this.getRMap(key);
        return rmap.get(field);
    }

    @SuppressWarnings({ "hiding", "unchecked" })
    public <K, V> void hdel(String key, K... field) {
        RMap<K, V> rmap = this.getRMap(key);
        rmap.fastRemove(field);
    }

    @SuppressWarnings("hiding")
    public <K, V> Integer hincrBy(String key, K field, Number delta) {
        RMap<K, V> rmap = this.getRMap(key);
        return (Integer) rmap.addAndGet(field, delta);
    }

    @SuppressWarnings("hiding")
    public <K, V> Map<K, V> hmget(String key, Set<K> keys) {
        RMap<K, V> rmap = this.getRMap(key);
        return rmap.getAll(keys);
    }

    @SuppressWarnings({ "hiding" })
    public <K, V> Map<K, V> hmgetPipeline(String key, Set<K> fields) {
        if (null != fields && fields.size() > 0) {
            Map<K, V> map = new HashMap<K, V>();
            for (K item : fields) {
                RMap<K, V> rmap = this.getRMap(key);
                V v = rmap.get(item);
                map.put(item, v);
            }
        }
        return null;
    }
    
    @SuppressWarnings({ "hiding" })
    public <K, V> Integer hlen(String key){
        RMap<K, V> rmap = this.getRMap(key);
        return rmap.size();
    }

    // -------------------------Hash End---------------------------//

    // -------------------------List Start-------------------------//
    public V lindex(String key, int index) {
        RList<V> rList = this.getRList(key);
        return rList.get(index);
    }

    public boolean rpush(String key, V value) {
        RList<V> rList = this.getRList(key);
        return rList.add(value);
    }

    public int llen(String key) {
        RList<V> rList = this.getRList(key);
        return rList.size();
    }

    public void lrem(String key, int index) {
        RList<V> rList = this.getRList(key);
        rList.fastRemove(index);
    }

    public List<V> lrange(String key, int fromIndex, int toIndex) {
        RList<V> rList = this.getRList(key);
        return rList.subList(fromIndex, toIndex);
    }

    public List<V> lrangeAll(String key) {
        RList<V> rList = this.getRList(key);
        return rList.readAll();
    }
    
    public V lpop(String key) {
        RQueue<V> rQueue = this.getRQueue(key);
        return rQueue.poll();
    }
    // -------------------------List End---------------------------//

    // -------------------------SortSet Start-------------------------//
    public boolean zadd(String key, double score, V value) {
        RScoredSortedSet<V> sortSet = this.getRScoredSortedSet(key);
        return sortSet.add(score, value);
    }

    public Long zaddAll(String key, Map<V, Double> values) {
        RScoredSortedSet<V> sortSet = this.getRScoredSortedSet(key);
        return sortSet.addAll(values);
    }

    public Double zincrby(String key, Number scoreStep, V value) {
        RScoredSortedSet<V> sortSet = this.getRScoredSortedSet(key);
        return sortSet.addScore(value, scoreStep);
    }

    public int zcount(String key) {
        RScoredSortedSet<V> sortSet = this.getRScoredSortedSet(key);
        return sortSet.size();
    }

    public Double zscore(String key, V value) {
        RScoredSortedSet<V> sortSet = this.getRScoredSortedSet(key);
        return sortSet.getScore(value);
    }

    public Collection<V> zrange(String key, int startIndex, int endIndex) {
        RScoredSortedSet<V> sortSet = this.getRScoredSortedSet(key);
        return sortSet.valueRange(startIndex, endIndex);
    }

    public boolean zrem(String key, Object obj) {
        RScoredSortedSet<V> sortSet = this.getRScoredSortedSet(key);
        return sortSet.remove(obj);
    }
    
    public Collection<V> zrevrange(String key, int startIndex, int endIndex) {
        RScoredSortedSet<V> sortSet = this.getRScoredSortedSet(key);
        return sortSet.valueRangeReversed(startIndex, endIndex);
    }
    
    public int zremrangebyscore(String key, double startScore, boolean startScoreInclusive, double endScore, boolean endScoreInclusive) {
        RScoredSortedSet<V> sortSet = this.getRScoredSortedSet(key);
        return sortSet.removeRangeByScore(startScore, startScoreInclusive, endScore, endScoreInclusive);
    }
    
    public Collection<V> zrevrangebyscore(String key, double startScore, boolean startScoreInclusive, double endScore, boolean endScoreInclusive) {
        RScoredSortedSet<V> sortSet = this.getRScoredSortedSet(key);
        return sortSet.valueRangeReversed(startScore, startScoreInclusive, endScore, endScoreInclusive);
    }
    
    public Collection<V> zrangebyscore(String key, double startScore, boolean startScoreInclusive, double endScore, boolean endScoreInclusive){
        RScoredSortedSet<V> sortSet = this.getRScoredSortedSet(key);
        return sortSet.valueRange(startScore, startScoreInclusive, endScore, endScoreInclusive);
    }

    public Collection<V> zrangebyscore(String key, double startScore, boolean startScoreInclusive, double endScore, boolean endScoreInclusive, int offset, int pageSize){
        RScoredSortedSet<V> sortSet = this.getRScoredSortedSet(key);
        return sortSet.valueRange(startScore, startScoreInclusive, endScore, endScoreInclusive, offset, pageSize);
    }

    public Collection<V> zrangebyscoreReversed(String key, double startScore, boolean startScoreInclusive, double endScore, boolean endScoreInclusive, int offset, int count) {
        RScoredSortedSet<V> sortSet = this.getRScoredSortedSet(key);
        return sortSet.valueRangeReversed(startScore, startScoreInclusive, endScore, endScoreInclusive, offset, count);
    }
    
    // -------------------------SortSet End-------------------------//

    // -------------------------Scripting Start-----------------------//
    public <R> R eval(Mode mode, String luaScript, ReturnType returnType) {
        RScript rScript = this.getRScript();
        return rScript.eval(mode, luaScript, returnType);
    }

    public <R> R eval(Mode mode, String luaScript, ReturnType returnType, List<Object> keys, Object... values) {
        RScript rScript = this.getRScript();
        return rScript.eval(mode, luaScript, returnType, keys, values);
    }
    // -------------------------Scripting End-------------------------//
    
    // -------------------------Pub/Sub Start-----------------------//
    public <M> int subscribe (String objectName, MessageListener<M> msgListener) {
        RTopic<M> rTopic = this.getRTopic(objectName);
        return rTopic.addListener(msgListener);
    }
    
    public <M> void unSubscribe (String objectName, int listenerId) {
        RTopic<M> rTopic = this.getRTopic(objectName);
        rTopic.removeListener(listenerId);
    }
    
    public <M> long publish(String objectName, M msg) {
        RTopic<M> rTopic = this.getRTopic(objectName);
        return rTopic.publish(msg);
    }
    // -------------------------Pub/Sub End-------------------------//
    
    // -------------------------Set Start-------------------------//
    public boolean addRSet(String key, V value) {
        RSet<Object> set = this.getSet(key);
        return set.add(value);
    }

    public boolean removeRSet(String key,V value) {
        RSet<Object> set = this.getSet(key);
        return set.remove(value);
    }

    public int Slen(String key) {
        RSet<Object> set = this.getSet(key);
        return set.size();
    }

    public boolean deleteRSet(String key) {
        RSet<Object> set = this.getSet(key);
        return set.delete();
    }
    
    public RSet<Object> getSets(String key) {
       return this.getSet(key);
    }
    // -------------------------Set End---------------------------//
}
