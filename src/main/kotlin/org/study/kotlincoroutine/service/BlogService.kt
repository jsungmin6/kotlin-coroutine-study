package org.study.kotlincoroutine.service

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service
import org.study.kotlincoroutine.clients.BlogClient
import org.study.kotlincoroutine.dto.BlogResponse

@Service
class BlogService(
    private val blogClients: List<BlogClient>,
) {

    suspend fun getBlogList(days: Long): BlogResponse = coroutineScope {

        println("[Main] 실행 스레드: ${Thread.currentThread().name}")

        val results = blogClients.mapIndexed { index, client ->
            async {
                try {
                    val result = client.getPosts(days)
                    println("${this.javaClass.name} 종료 스레드: ${Thread.currentThread().name}")
                    result
                } catch (e: Exception) {
                    emptyList()
                }
            }
        }.map { it.await() }.flatten()

        println("[Main] 종료 스레드: ${Thread.currentThread().name}")

        BlogResponse(results)
    }
}