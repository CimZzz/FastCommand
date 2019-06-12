package com.virtualightning.interfaces

interface IReporter {
    fun reportMessage(any: Any?)
    fun reportWarn(any: Any?)
    fun reportError(any: Any?)
}