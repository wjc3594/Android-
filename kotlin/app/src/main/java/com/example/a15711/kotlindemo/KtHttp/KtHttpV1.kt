package com.example.a15711.kotlindemo.KtHttp

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.reflect.Method
import java.lang.reflect.Proxy

object KtHttpV1 {
    private val okHttpClient by lazy { OkHttpClient() }
    private val gson by lazy { Gson() }
    val baseUrl = "https://baseurl.com"
    inline fun <reified T> create(): T {
        return Proxy.newProxyInstance(
            T::class.java.classLoader, arrayOf<Class<*>>(T::class.java)
        ) { proxy, method, args ->
            return@newProxyInstance method.annotations.filterIsInstance<Get>()
                .takeIf { it.size == 1 }?.let {
                    invoke("$baseUrl${it[0].value}", method, args)
                }
        } as T
    }

    fun invoke(path: String, method: Method, args: Array<Any>): Any? {
        //1.拼接url
//        if (method.parameterAnnotations.size != args.size) return null
//        val url = StringBuilder().append(path)
//        for (i in method.parameterAnnotations.indices) {//带注解的参数个数
//            for (p in method.parameterAnnotations[i]) {//注解个数
//                if (p is Field) {
//                    val key = p.value
//                    val value = args[i].toString()
//                    if (!url.contains("?")) {
//                        url.append("?").append("$key=$value")
//                    } else {
//                        url.append("&").append("$key=$value")
//                    }
//                }
//            }
//        }
//        val request = Request.Builder().url(url.toString()).build()
//        val response = okHttpClient.newCall(request).execute()
//        val body = response.body
//        val json = body?.string()
//        return gson.fromJson(json, method.genericReturnType)

        return method.parameterAnnotations.takeIf {
            it.size == args.size
        }?.mapIndexed { index, arrayOfAnnotations -> Pair(arrayOfAnnotations, args[index]) }
            ?.fold(path, KtHttpV1::parseUrl)?.let {
                Request.Builder().url(it).build()
            }?.let {
                okHttpClient.newCall(it).execute().body?.string()
            }?.let {
                gson.fromJson(it, method.genericReturnType)
            }
    }

    private fun parseUrl(acc: String, pair: Pair<Array<Annotation>, Any>) =
        pair.first.filterIsInstance<Field>().first().let {
            if (acc.contains("?")) {
                "$acc&${it.value}=${pair.second}"
            } else {
                "$acc?${it.value}=${pair.second}"
            }
        }
}