package com.fibbery.rpc.serialzation;

/**
 * @author fibbery
 * @date 17/12/1
 */
public interface Serializer {

    /**
     * 序列化对象
     * @param object
     * @return
     */
    byte[] serialize(Object object);

    /**
     * 反序列化对象
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
