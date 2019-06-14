package com.virtualightning

import java.io.BufferedReader
import java.io.InputStreamReader

fun main(args: Array<String>) {
    val process = Runtime.getRuntime().exec("bash")
    Thread {
        val reader = process.inputStream
        val buffer = ByteArray(128)
        while (true) {
            val byte = reader.read(buffer, 0, buffer.size)
            print("${byte.toChar()}-${reader.available()}")
        }
        print("msg end")
    }.start()
    Thread {
        val reader = BufferedReader(InputStreamReader(process.errorStream))
        val buffer = ByteArray(128)
        while (reader.ready()) {
            val x = reader.readLine() ?: break
            print("$x")
        }
        print("err end")
    }.start()

    val writer = process.outputStream

    while (true) {
        val line = readLine()
        writer.write("$line\n".toByteArray())
        writer.flush()
    }
}