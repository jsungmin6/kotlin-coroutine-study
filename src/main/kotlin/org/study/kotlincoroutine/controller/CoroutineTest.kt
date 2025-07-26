package org.study.kotlincoroutine.controller

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import org.springframework.web.servlet.function.ServerResponse.async
import kotlin.system.measureTimeMillis


class CoroutineTest {

    //coroutineScope 내에서 async 코루틴 실패시
    suspend fun coroutineScope() = coroutineScope {
        val a = async {
            delay(500)
            println("A 실행 중...")
            throw RuntimeException("A 실패 발생")
        }

        val b = async {
            delay(1000)
            println("B 실행 중...")
            "B 결과"
        }

        val resultB = b.await()
        println("▶ B 완료: $resultB")
    }

    //coroutineScope 내에서 async 코루틴 실패시
    suspend fun supervisorScope() = supervisorScope {
        val a = async {
            delay(500)
            println("A 실행 중...")
            throw RuntimeException("A 실패 발생")
        }

        val b = async {
            delay(1000)
            println("B 실행 중...")
            "B 결과"
        }

        try {
            val resultA = a.await()
            println("▶ A 완료: $resultA")
        } catch (e: Exception) {
            println("예외 발생: ${e.message}")
        }
        val resultB = b.await()
        println("▶ B 완료: $resultB")
    }

    //async와 supervisorScope를 사용한 예외 처리
    suspend fun test() {
        val time = measureTimeMillis {
            supervisorScope {
                val deferred = async(Dispatchers.IO) {
                    delay(500)
                    throw RuntimeException("async 내부에서 오류 발생")
                }

                try {
                    val result = deferred.await()
                    println("결과: $result")
                } catch (e: Exception) {
                    println("예외 잡힘: ${e.message}")
                }
            }
        }
        println("총 실행 시간: $time ms")
    }

    //withContext는 스코프 없어도 됨.
    //withContext의 에러처리
    suspend fun test2() {
        val time = measureTimeMillis {
            try {
                val result = withContext(Dispatchers.IO) {
                    delay(500)
                    throw RuntimeException("withContext 내부에서 오류 발생")
                }
                println("결과: $result") // 실행되지 않음
            } catch (e: Exception) {
                println("예외 잡힘: ${e.message}")
            }
        }

        println("총 실행 시간: $time ms")
    }

    //coroutineScope 와 async로 동시처리
    suspend fun test3() {
        val time = measureTimeMillis {
            coroutineScope {
                val a = async(Dispatchers.IO) {
                    delay(1000)
                    "A"
                }

                val b = async(Dispatchers.IO) {
                    delay(1000)
                    "B"
                }

                println("결과: ${a.await()} + ${b.await()}")
            }
        }

        println("총 실행 시간: $time ms")
    }

    //withContext로 순차처리
    suspend fun test4() {
        val time = measureTimeMillis {
            // withContext는 현재 코루틴에서 컨텍스트(IO)만 전환하고, 순차적으로 실행됨
            val a = withContext(Dispatchers.IO) {
                delay(1000) // 1초 걸리는 작업
                "A"
            }

            val b = withContext(Dispatchers.IO) {
                delay(1000) // 또 1초 걸리는 작업
                "B"
            }

            println("결과: $a + $b")
        }

        println("총 실행 시간: $time ms")
    }

    //supervisorScope 와 async 에서도 에러나는 케이스
    suspend fun test5() {
        supervisorScope {
            val deferred = async(Dispatchers.IO) {
                delay(500)
                throw RuntimeException("async 내부에서 오류 발생")
            }

            val result = deferred.await() // ❗ 예외를 잡지 않음
            println("결과: $result") // 실행되지 않음
        }
    }
}

suspend fun main() {
    val coroutineTest = CoroutineTest()
    coroutineTest.test5()
}