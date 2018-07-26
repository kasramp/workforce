package com.madadipouya.workforce.rest.v1.response.impl;

import com.madadipouya.workforce.rest.v1.response.RestResponse;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AbstractRestResponse implements RestResponse {

    private final String timestamp;

    private final int status;

    private final String message;

    protected AbstractRestResponse(int status, String message) {
        this.status = status;
        this.message = message;
        timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSZZ").format(new Date());
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
