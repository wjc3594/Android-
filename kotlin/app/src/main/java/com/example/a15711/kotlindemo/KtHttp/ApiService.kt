package com.example.a15711.kotlindemo.KtHttp

interface ApiService {
    @Get("/repo")
    fun repos(
        @Field("lang") lang: String,
        @Field("since") since: String,
    ): RepoList
    @Get("/repo")
    fun reposAsync(
        @Field("lang") lang: String,
        @Field("since") since: String,
    ): KtCallback<RepoList>
}