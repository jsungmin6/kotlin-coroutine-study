package org.study.kotlincoroutine.clients

interface BlogClient {
    suspend fun getPosts(day: Long): MutableList<String>
}