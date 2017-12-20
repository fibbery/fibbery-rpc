package com.fibbery.rpc.serialzation;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
        try {
            baos.flush();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Input input = new Input(bais);
        Kryo kryo = new Kryo();
        T object = kryo.readObject(input, clazz);
        input.close();
        try {
            bais.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }
}
