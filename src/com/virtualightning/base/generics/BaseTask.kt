package com.virtualightning.base.generics

import com.virtualightning.interfaces.Run
import com.virtualightning.tools.RefHandler

abstract class BaseTask<E>(
    val runnable: (BaseTask<E>) -> E?,
    private val handler: RefHandler<E, *>? = null
): Run {
    var isRunning = true


    override fun invoke() {
        val result = runnable(this) ?: return
        handler?.invoke(result)
    }
}