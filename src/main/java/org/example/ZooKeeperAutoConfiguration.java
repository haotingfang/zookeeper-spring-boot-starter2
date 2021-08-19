package org.example;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "zookeeper", name = "servers")
@EnableConfigurationProperties(value = ZooKeeperProperty.class)
public class ZooKeeperAutoConfiguration {

    private Logger logger = LoggerFactory.getLogger(ZooKeeperAutoConfiguration.class);

    /**
     * 初始化连接以及重试
     * @param zooKeeperProperty 配置属性
     * @return 连接
     */
    @Bean(initMethod = "start", destroyMethod = "close")
    @ConditionalOnMissingBean
    public CuratorFramework curatorFramework(ZooKeeperProperty zooKeeperProperty) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(zooKeeperProperty.getBaseSleepTime(), zooKeeperProperty.getMaxRetries());
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(zooKeeperProperty.getServers())
                .connectionTimeoutMs(zooKeeperProperty.getConnectionTimeout())
                .sessionTimeoutMs(zooKeeperProperty.getSessionTimeout())
                .retryPolicy(retryPolicy)
                .build();
        return client;
    }


    @Bean
    @ConditionalOnMissingBean
    public ZooKeeperTemplate zooKeeperTemplate(CuratorFramework client) {
        return new ZooKeeperTemplate(client);
    }

}
