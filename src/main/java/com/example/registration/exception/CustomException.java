
package com.example.registration.exception;


public abstract class CustomException extends Throwable {
    public CustomException(String msg){
        super(msg);
    }

}

//generic class can't extend Throwable
//public interface CustomException<T extends Throwable>{
//
//    String customEx (String msg) throws T;
//}