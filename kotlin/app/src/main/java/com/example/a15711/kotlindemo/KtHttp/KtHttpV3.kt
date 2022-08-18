package com.example.a15711.kotlindemo.KtHttp

import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`.getRawType
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.lang.Exception
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Proxy
import java.lang.reflect.Type
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

// https://trendings.herokuapp.com/repo?lang=java&since=weekly

suspend fun <T : Any> KtCall<T>.await(): T = suspendCancellableCoroutine { continuation ->
    val call = call(object : Callback<T> {
        override fun onSuccess(data: T) {
            continuation.resume(data)
        }

        override fun onFail(throwable: Throwable) {
            continuation.resumeWithException(throwable)
        }
    })
    continuation.invokeOnCancellation {
        call.cancel()
    }
}

interface Callback<T: Any> {
    fun onSuccess(data: T)
    fun onFail(throwable: Throwable)
}

class KtCall<T: Any>(
    private val call: Call,
    private val gson: Gson,
    private val type: Type
) {
    fun call(callback: Callback<T>): Call {
        call.enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFail(e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val t = gson.fromJson<T>(response.body?.string(), type)
                    callback.onSuccess(t)
                } catch (e: Exception) {
                    callback.onFail(e)
                }
            }
        })
        return call
    }
}

interface ApiServiceV3 {
    @Get("/repo")
    fun repos(
        @Field("lang") lang: String,
        @Field("since") since: String
    ): KtCall<RepoList>

    @Get("/repo")
    fun reposSync(
        @Field("lang") lang: String,
        @Field("since") since: String
    ): RepoList
}

object KtHttpV3 {

    private var okHttpClient: OkHttpClient = OkHttpClient()
    private var gson: Gson = Gson()
    var baseUrl = "https://trendings.herokuapp.com"

    fun <T: Any> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf<Class<*>>(service)
        ) { proxy, method, args ->
            val annotations = method.annotations
            for (annotation in annotations) {
                if (annotation is Get) {
                    val url = baseUrl + annotation.value
                    return@newProxyInstance invoke(url, method, args!!)
                }
            }
            return@newProxyInstance null

        } as T
    }

    private fun invoke(path: String, method: Method, args: Array<Any>): Any? {
        if (method.parameterAnnotations.size != args.size) return null

        var url = path
        val parameterAnnotations = method.parameterAnnotations
        for (i in parameterAnnotations.indices) {
            for (parameterAnnotation in parameterAnnotations[i]) {
                if (parameterAnnotation is Field) {
                    val key = parameterAnnotation.value
                    val value = args[i].toString()
                    if (!url.contains("?")) {
                        url += "?$key=$value"
                    } else {
                        url += "&$key=$value"
                    }
                    break
                }
            }
        }

        val request = Request.Builder()
            .url(url)
            .build()

        val call = okHttpClient.newCall(request)

        return if (isKtCallReturn(method)) {
            val genericReturnType = getTypeArgument(method)
            KtCall<Unit>(call, gson, genericReturnType)
        } else {
            val response = okHttpClient.newCall(request).execute()

            val genericReturnType = method.genericReturnType
            val json = response.body?.string()
            gson.fromJson<Any?>(json, genericReturnType)
        }
    }

    private fun getTypeArgument(method: Method) =
        (method.genericReturnType as ParameterizedType).actualTypeArguments[0]

    private fun isKtCallReturn(method: Method) =
        method.returnType == KtCall::class.java

}

fun main() {
//    testSync()
    testAsync()
}

private fun testSync() {
    val api: ApiServiceV3 = KtHttpV3.create(ApiServiceV3::class.java)
    val data: RepoList = api.reposSync(lang = "Kotlin", since = "weekly")
    println(data)
}

private fun testAsync() {
//    KtHttpV3.create(ApiServiceV3::class.java).repos(
//        lang = "Kotlin",
//        since = "weekly"
//    ).call(object : Callback<RepoList> {
//        override fun onSuccess(data: RepoList) {
//            println(data)
//        }
//
//        override fun onFail(throwable: Throwable) {
//            println(throwable)
//        }
//    })
    runBlocking {
        val res = async {
            KtHttpV3.create(ApiServiceV3::class.java).repos(
                lang = "Kotlin",
                since = "weekly"
            ).await()
        }
        res.cancel()
        println(res.await())
    }

}
