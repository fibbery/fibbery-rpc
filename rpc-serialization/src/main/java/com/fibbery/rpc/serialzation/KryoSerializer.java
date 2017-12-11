package com.fibbery.rpc.serialzation;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayOutputStream;
import java.net.URL;

/**
 * @author fibbery
 * @date 17/12/1
 */
public class KryoSerializer implements Serializer{
    @Override
    public byte[] serialize(Object object) {
        Kryo kryo = new Kryo();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeObject(output,object);
        output.flush();
        output.close();
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return null;
    }

    public static void main(String[] args) {
        URL location = KryoSerializer.class.getProtectionDomain().getCodeSource().getLocation();
        System.out.println(location);
    }
}
