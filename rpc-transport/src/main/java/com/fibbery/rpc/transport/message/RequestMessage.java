package com.fibbery.rpc.transport.message;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author fibbery
 * @date 17/11/30
 */
public class RequestMessage implements Serializable{

    private static final long serialVersionUID = -2783677231331345899L;

    private static AtomicLong sequence = new AtomicLong(1);

    private Long requestId;

    private String className;

    private String methodName;

    private Class[] parameterTypes;

    private Object[] paramters;

    public RequestMessage() {
        this.requestId = sequence.getAndDecrement();
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParamters() {
        return paramters;
    }

    public void setParamters(Object[] paramters) {
        this.paramters = paramters;
    }
}
