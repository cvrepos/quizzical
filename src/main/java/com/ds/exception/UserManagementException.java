package com.ds.exception;

public class UserManagementException extends QuizException{
    public UserManagementException(){
        super();
    }
    public UserManagementException(String msg){
        super(msg);
    }
    public UserManagementException(String msg, Throwable t){
        super(msg, t);
    }

}
