package com.stream.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class JedisClusterConfig {

    @Value("${spring.redis.cluster.nodes}")
    private String nodes;

    @Value("${spring.redis.max-attempt}")
    private int maxAttempt;

    @Value("${spring.redis.timeout}")
    private int timeout;

//    @Value("${redis.cluster.password}")
//    private String password;

    /**
     * 加载 spring.redis.pool 为前缀的配置，赋值给JedisPoolConfig对象
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.redis.pool")
    public JedisPoolConfig getRedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        return config;
    }

    /**
     * 从连接池获取JedisCluster对象，注入spring
     *
     * @return
     */
    @Bean
    public JedisCluster getJedisCluster() {
        // nodes
        String[] serverArray = nodes.split(",");
        Set<HostAndPort> nodes = new HashSet<>();
        for (String ipPort : serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
        }

        // pool
        JedisPoolConfig config = getRedisPoolConfig();
        return new JedisCluster(nodes, timeout, maxAttempt, config);
    }
}