package com.ttokey.blog.exception;

import com.ttokey.blog.enumeration.TtokeyErrorType;

public class TtokeyException extends RuntimeException {
    public TtokeyException(TtokeyErrorType ttokeyErrorType) {
        super(ttokeyErrorType.getMessage());
    }
}
