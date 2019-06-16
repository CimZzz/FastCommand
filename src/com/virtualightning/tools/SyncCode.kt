package com.virtualightning.tools

class SyncCode {
    private var code = 0

    @Synchronized
    fun getCode() = code

    @Synchronized
    fun nextCode(): Int = code ++

    @Synchronized
    fun checkSync(code: Int): Boolean = this.code == code
}