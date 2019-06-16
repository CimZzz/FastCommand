package com.virtualightning.actions

import com.virtualightning.base.generics.BaseAction

class LogAction(
    val message: String,
    val messageType: Int = MESSAGE
): BaseAction() {
    companion object {
        const val MESSAGE = 0
        const val WARN = 1
        const val SUCCESS = 2
        const val ERROR = 3
    }
}