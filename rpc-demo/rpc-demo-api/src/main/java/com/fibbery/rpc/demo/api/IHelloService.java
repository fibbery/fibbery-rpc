package com.fibbery.rpc.demo.api;

/**
 * @author fibbery
 * @date 17/11/30
 */
public interface IHelloService {
    /**
     * 获取返回字符串
     * @param name 名称
     * @return
     */
    public String getHelloStr(String name);
}
