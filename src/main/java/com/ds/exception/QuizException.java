package com.ds.exception;

public class QuizException extends Exception{

    public QuizException(){
        super();
    }
    public QuizException(String msg){
        super(msg);
    }
    public QuizException(String msg, Throwable t){
        super(msg, t);
    }
}
