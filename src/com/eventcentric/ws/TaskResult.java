package com.eventcentric.ws;

import java.util.HashMap;

public class TaskResult<T> {
    T data;
    HashMap<String, Object> store = new HashMap<String, Object>();
    ErrorResponse response = new ErrorResponse();;

    public T getResult() {
        return data;
    };

    void setData(T data) {
        this.data = data;
    }

    public void putObject(String key, Object value) {
        store.put(key, value);
    }
    public Object getObject(String key) {
        return store.get(key);
    }
    public ErrorResponse getErrorResponse() {
        return response;
    }
}
