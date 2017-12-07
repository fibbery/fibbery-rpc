package com.fibbery.rpc.registry;

import java.io.Serializable;

/**
 * 发布服务信息
 * @author fibbery
 * @date 17/11/30
 */
public class PublishMeta implements Serializable{

    private static final long serialVersionUID = -1899443314731302585L;

    private Address address;

    private SubscribeMeta subscribeMeta;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public SubscribeMeta getSubscribeMeta() {
        return subscribeMeta;
    }

    public void setSubscribeMeta(SubscribeMeta subscribeMeta) {
        this.subscribeMeta = subscribeMeta;
    }
}
