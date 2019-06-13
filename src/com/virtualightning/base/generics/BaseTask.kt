package com.virtualightning.base.generics

import com.virtualightning.tools.RefHandler

class BaseTask<T, E>(
    val runnable: (BaseTask<T, E>) -> T?,
    val handler: RefHandler<T, E>
): Runnable {
    var isRunning = true

    override fun run() {
        val result = runnable(this) ?: return
        handler.invoke(result)
    }
}