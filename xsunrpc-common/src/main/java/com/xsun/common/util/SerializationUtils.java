package com.xsun.common.util;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * created at 20:40, 2019/3/23
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
public class SerializationUtils {

    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>() ;

    private static Objenesis objenesis = new ObjenesisStd(true);


    public static <T> T deserialize(byte[] data, Class<T> decoderClass) {
        T objMsg = objenesis.newInstance(decoderClass) ;
        Schema<T> schema = getSchema(decoderClass) ;
        ProtostuffIOUtil.mergeFrom(data, objMsg, schema) ;
        return objMsg;
    }

    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getSchema(Class<T> decoderClass) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(decoderClass);
        if(schema == null){
            schema = RuntimeSchema.createFrom(decoderClass) ;
            cachedSchema.put(decoderClass, schema) ;
        }
        return schema ;
    }

    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T msg) {
        Class<T> clazz = (Class<T>) msg.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE) ;
        Schema<T> schema = getSchema(clazz) ;
        return ProtostuffIOUtil.toByteArray(msg, schema, buffer) ;
    }
}
