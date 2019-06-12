package com.virtualightning.core

import com.virtualightning.defaults.DefaultReporter
import com.virtualightning.interfaces.IReporter

object CoreApp {
    var reporter: IReporter = DefaultReporter

    fun reportError(any: Any?) {
        reporter.reportError(any)
    }

    fun reportWarn(any: Any?) {
        reporter.reportWarn(any)
    }

    fun reportMessage(any: Any?) {
        reporter.reportMessage(any)
    }
}