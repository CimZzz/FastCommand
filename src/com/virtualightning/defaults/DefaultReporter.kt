package com.virtualightning.defaults

import com.virtualightning.interfaces.IReporter

object DefaultReporter: IReporter {
    override fun reportMessage(any: Any?) {
        System.out.println(any)
    }

    override fun reportWarn(any: Any?) {
        System.err.println("Warning: $any")
    }

    override fun reportError(any: Any?) {
        System.err.println("Error: $any")
    }
}