package com.fibbery.rpc.registry;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fibbery
 * @date 17/11/30
 */
public class ZookeeperRegistry {

    private ZookeeperManager manager;

    public ZookeeperRegistry(ZookeeperManager manager) {
        this.manager = manager;
    }

    /**
     * 发布服务
     */
    public void publish(PublishMeta meta) {
        String path = String.format("/fibbery/rpc/%s/%s/%s/%s:%s",
                meta.getSubscribeMeta().getServiceName(),
                meta.getSubscribeMeta().getGroup(),
                meta.getSubscribeMeta().getVersion(),
                meta.getAddress().getHost(),
                meta.getAddress().getPort());
        manager.createNode(path, StringUtils.EMPTY);
    }


    public List<Address> searchServiceAddress(SubscribeMeta meta) {
        String path = String.format("/fibbery/rpc/%s/%s/%s",
                meta.getServiceName(),
                meta.getGroup(),
                meta.getVersion());
        List<String> children = manager.listChildren(path);
        List<Address> addresses = new ArrayList<Address>();
        children.forEach(child -> {
            Address address = new Address();
            String[] strs = child.split(":");
            address.setHost(strs[0]);
            address.setPort(Integer.parseInt(strs[1]));
            addresses.add(address);
        });
        return addresses;
    }

}
