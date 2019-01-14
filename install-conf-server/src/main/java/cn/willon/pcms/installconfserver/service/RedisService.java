package cn.willon.pcms.installconfserver.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

/**
 * RedisService
 *
 * @author Willon
 * @since 2019-01-13
 */
@Service
public class RedisService {

    @Value("${spring.redis.host}")
    private String host;

    private static final String INSTALL_LOCK = "INSTALL_LOCK";
    private static final Integer EXPIRE_TIME = 10 * 60;


    /**
     * 尝试获取锁
     *
     * @return 是否得到锁
     */
    public synchronized boolean tryLock() {
        Jedis jedis = jedis();
        String s = jedis.get(INSTALL_LOCK);
        return StringUtils.isEmpty(s);
    }

    public synchronized boolean lock(String hostname) {
        Jedis jedis = jedis();
        Long result = jedis.setnx(INSTALL_LOCK, hostname);
        // 0 代表已经存在
        if (0 == result) {
            return false;
        }
        jedis.expire(INSTALL_LOCK, EXPIRE_TIME);
        return true;
    }

    public synchronized void unlock() {
        Jedis jedis = jedis();
        jedis.del(INSTALL_LOCK);
    }


    @Bean
    public Jedis jedis() {
        Jedis jedis = new Jedis(host);
        return jedis;
    }


}
