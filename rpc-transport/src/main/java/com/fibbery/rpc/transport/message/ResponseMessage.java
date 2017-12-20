package com.fibbery.rpc.transport.message;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author fibbery
 * @date 17/11/30
 */
public class ResponseMessage implements Serializable{

    private static final long serialVersionUID = 5816393962960823930L;

    private static AtomicLong sequence = new AtomicLong(1);

    private Long responseId;

    private int errorCode;

    private Object result;

    public ResponseMessage() {
        this.responseId = sequence.getAndIncrement();
    }

    public static AtomicLong getSequence() {
        return sequence;
    }

    public static void setSequence(AtomicLong sequence) {
        ResponseMessage.sequence = sequence;
    }

    public Long getResponseId() {
        return responseId;
    }

    public void setResponseId(Long responseId) {
        this.responseId = responseId;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
