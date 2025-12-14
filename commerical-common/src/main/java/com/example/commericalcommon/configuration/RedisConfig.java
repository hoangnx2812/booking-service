package com.example.commericalcommon.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.util.Pool;

import java.time.Duration;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.jedis.pool.min-idle}")
    private int minIdle;

    @Value("${spring.data.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.data.redis.jedis.pool.max-total}")
    private int maxTotal;

    @Value("${spring.data.redis.jedis.pool.min-evictable-idle-time}")
    private long minEvictableIdleTimeMillis;

    @Value("${spring.data.redis.jedis.pool.time-between-eviction-runs}")
    private long timeBetweenEvictionRunsMillis;

    @Value("${spring.data.redis.jedis.pool.num-tests-per-eviction-run}")
    private int numTestsPerEvictionRun;

    @Value("${spring.data.redis.jedis.pool.max-wait}")
    private int maxWaitMillis;

    @Value("${spring.data.redis.password}")
    private String password;

    @Bean
    public Pool<Jedis> jedisPool() {
        if (StringUtils.hasText(password)) {
            return new JedisPool(getJedisPoolConfig(), host, port, maxWaitMillis, password);
        } else return new JedisPool(getJedisPoolConfig(), host, port);
    }

    private JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setJmxEnabled(false);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMinEvictableIdleDuration(Duration.ofMillis(minEvictableIdleTimeMillis));
        poolConfig.setTimeBetweenEvictionRuns(Duration.ofMillis(timeBetweenEvictionRunsMillis));
        poolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        poolConfig.setMaxWait(Duration.ofMillis(maxWaitMillis));
        return poolConfig;
    }
}
