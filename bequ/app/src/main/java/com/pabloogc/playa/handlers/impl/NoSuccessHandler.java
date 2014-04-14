package com.pabloogc.playa.handlers.impl;


import com.pabloogc.playa.handlers.SuccessHandler;

/**
 * Created by Pablo Orgaz - 10/27/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class NoSuccessHandler<T> extends SuccessHandler<T> {
    @Override public void onSuccess(T result) {
    }
}
