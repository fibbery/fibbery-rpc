package com.fibbery.rpc.registry;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理zookeeper的连接
 * @author fibbery
 * @date 17/11/30
 */
@Slf4j
public class ZookeeperManager {

    private static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    /**
     * 默认过期时间
     */
    private static Integer DEFAULT_CONNECT_TIME_OUT_MS = 15 * 1000;

    private static Integer DEFAULT_SESSION_TIME_OUT_MS = 60 * 1000;

    private CuratorFramework client;



    public ZookeeperManager(String connectString) {
        this(connectString, DEFAULT_CONNECT_TIME_OUT_MS, DEFAULT_SESSION_TIME_OUT_MS);
    }

    public ZookeeperManager(String connectString, Integer connectionTimeoutMs, Integer sessionTimeoutMs) {
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        this.client = CuratorFrameworkFactory.builder().connectString(connectString)
                .retryPolicy(policy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                .build();
        this.client.start();
    }

    public String createNode(String path, String value) {
        try {
            if (StringUtils.isEmpty(value)) {
                return client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
            }
            return client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, value.getBytes(DEFAULT_CHARSET));
        } catch (Exception e) {
            log.error("zk manager create persistent node fail", e);
        }
        return StringUtils.EMPTY;
    }


    public String createEphemeralNode(String path, String value) {
        try {
            return client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, value.getBytes(DEFAULT_CHARSET));
        } catch (Exception e) {
            log.error("zk manager create ephemeral node fail", e);
        }
        return StringUtils.EMPTY;
    }

    public void deleteNode(String path) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            log.error("zk manager delete node fail", e);
        }
    }

    public List<String> listChildren(String path) {
        List<String> childrens = new ArrayList<String>();
        try {
            childrens = client.getChildren().forPath(path);
        } catch (Exception e) {
            log.error("zk manager get childrens error", e);
        }
        return childrens;
    }

    public boolean isExsit(String path) {
        Stat stat = null;
        try {
            stat = client.checkExists().forPath(path);
        } catch (Exception e) {
            log.error("zk manager check node exist error", e);
        }
        return stat != null;
    }

    public void addListener(ConnectionStateListener... listeners) {
        for (ConnectionStateListener listener : listeners) {
            this.client.getConnectionStateListenable().addListener(listener);
        }
    }

    public void close() {
        client.close();
    }
}
