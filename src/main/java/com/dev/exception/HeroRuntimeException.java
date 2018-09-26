package com.dev.exception;

/**
 * @ClassName: HeroRuntimeException
 * @Description: HeroRuntimeException
 * @author: wen.dai
 * @date: 2018年5月22日 下午6:44:59
 */
public class HeroRuntimeException extends RuntimeException {

    public HeroRuntimeException() {

    }

    public HeroRuntimeException(Throwable cause) {
        super(cause);
    }

    public HeroRuntimeException(String message) {
        super(message);
    }
}
