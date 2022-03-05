package com.example.a15711.kotlindemo.ClassExtend

/**
 * 继承的两种写法
 * 1、使用constructor关键字
 */
class SonClass : ParentClass {
    constructor(param1: Int) : super()
    constructor(param1: Int, param2: Int) : this(param1)
}
