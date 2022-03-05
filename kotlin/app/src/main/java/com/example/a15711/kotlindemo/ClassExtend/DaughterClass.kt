package com.example.a15711.kotlindemo.ClassExtend

/**
 * 继承的第二种写法
 * 2、使用主构造函数
 */
class DaughterClass() : ParentClass() {
    constructor(param1: Int) : this()
}