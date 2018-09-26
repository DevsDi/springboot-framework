package com.dev.exception;


/**
 * @ClassName: HeroException
 * @Description: HeroException
 * @author: wen.dai
 * @date: 2018年5月22日 下午6:43:39
 */
public class HeroException extends Exception {

    public HeroException() {
        super();
    }

    public HeroException(Throwable cause) {
        super(cause);
    }

    public HeroException(String message) {
        super(message);
    }

    public HeroException(String message , Throwable throwable) {
        super(message , throwable);
    }
}
