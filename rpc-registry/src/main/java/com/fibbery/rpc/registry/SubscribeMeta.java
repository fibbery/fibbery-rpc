package com.fibbery.rpc.registry;

import java.io.Serializable;

/**
 * 订阅的服务信息
 * @author fibbery
 * @date 17/11/30
 */
public class SubscribeMeta implements Serializable{

    private static final long serialVersionUID = 158218115334265753L;

    private String serviceName;

    private String group;

    private String version;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
