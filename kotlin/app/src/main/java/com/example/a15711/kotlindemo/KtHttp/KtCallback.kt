package com.example.a15711.kotlindemo.KtHttp

interface KtCallback<T : Any> {
    fun success(data: T)
    fun failure(throwable: Throwable)
}