package com.example.a15711.kotlindemo.KtHttp

fun main() {
    println("Hello world.")
    val repo = KtHttpV1.create<ApiService>()
    repo.repos("Kotlin", "Weekly")
}