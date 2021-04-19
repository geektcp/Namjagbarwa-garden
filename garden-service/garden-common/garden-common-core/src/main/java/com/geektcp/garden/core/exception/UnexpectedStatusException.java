package com.geektcp.garden.core.exception;


import com.geektcp.garden.core.constant.Status;

/**
 * Created by chengmo on 2018/5/16.
 */
public class UnexpectedStatusException extends BaseException {

    private Status status;

    public UnexpectedStatusException(Status status, Object... args) {
        this(status, null, args);
    }

    public UnexpectedStatusException(Status status, Throwable cause) {
        super(status.getCode(), status.getDesc(), cause);
        this.status = status;
    }

    public UnexpectedStatusException(Status status, Throwable cause, Object... args) {
        super(status.getCode(), status.getDesc(), cause, args);
        this.status = status;
    }

    public UnexpectedStatusException(String descPattern, Throwable cause, Object... args) {
        super(descPattern, cause, args);
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        if (status != null){
            return getDesc();
        } else{
            return "";
        }
    }
}
