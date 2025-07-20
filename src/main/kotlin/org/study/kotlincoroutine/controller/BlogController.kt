package org.study.kotlincoroutine.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.study.kotlincoroutine.dto.BlogResponse
import org.study.kotlincoroutine.service.BlogService

@RestController
@RequestMapping("/v1")
class BlogController(
    val blogService: BlogService
) {
    @GetMapping("/blogs/recent")
    suspend fun getBlogs(
        @RequestParam(defaultValue = "1") days: Long
    ): BlogResponse {
        return blogService.getBlogList(days)
    }
}