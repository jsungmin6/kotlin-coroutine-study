package org.study.kotlincoroutine.clients

import kotlinx.coroutines.delay
import org.springframework.stereotype.Component

@Component
class NaverBlogClient : BlogClient {
    override suspend fun getPosts(day: Long): MutableList<String> {
        println("${this.javaClass.name} 시작 스레드: ${Thread.currentThread().name}")
        delay(5000)
        return mutableListOf(
            "네이버에서 일하는 법",
            "네이버 kafka(1)"
        )
    }
}