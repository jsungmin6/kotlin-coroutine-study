package org.study.kotlincoroutine.clients

import kotlinx.coroutines.delay
import org.springframework.stereotype.Component

@Component
class KakaoBlogClient : BlogClient {
    override suspend fun getPosts(day: Long): MutableList<String>{
        println("${this.javaClass.name} 시작 스레드: ${Thread.currentThread().name}")
        delay(5000)
        println("${this.javaClass.name} 종료 스레드: ${Thread.currentThread().name}")

        return mutableListOf(
            "카카오에서 일하는 법",
            "카카오 Restapi"
        )
    }
}