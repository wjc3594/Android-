package com.example.a15711.kotlindemo.ExtendClass

/**
 * 最好扩展哪个类，扩展文件的名字就定义为扩展类的名字，这样方便以后进行查找
 **/
fun String.isBig(): Boolean {
    if (this.length > 1024)
        return true
    return false
}