package com.virtualightning.base.semantics

import com.virtualightning.base.generics.BaseAction
import com.virtualightning.tools.MessageLooper

abstract class BaseSemanticRun(
    val semantic: BaseSemantic
) {
    var errorText: String? = null
    private var runnable: ((messageLooper: MessageLooper<BaseAction>?) -> Any?)? = null
    private var markEnd: Boolean = false

    fun run(messageLooper: MessageLooper<BaseAction>?): Any? {
        val runnable = this.runnable
        if(runnable != null)
            return runnable(messageLooper)
        return doRun(messageLooper)
    }

    fun markEnd(runnable: ((messageLooper: MessageLooper<BaseAction>?) -> Any?)? = null) {
        this.runnable = runnable
        markEnd = true
    }

    fun isMarkEnd() = markEnd

    fun checkParamsLength(syntaxStr: String, length: Int, params: Array<out Any?>): Boolean {
        if(params.size != length) {
            errorText = "$syntaxStr 所需参数长度不对，需要 $length 个，实际 ${params.size} 个"
            return false
        }
        return true
    }

    fun catchRun(run: () -> Unit) {
        try {
            run()
        }
        catch (e: Throwable) {
            errorText = e.message
        }
    }

    open fun doRun(messageLooper: MessageLooper<BaseAction>?): Any? = null
    open fun setConfig(configSyntax: String, vararg params: Any?) {
        errorText = "错误的配置 $configSyntax"
    }
    open fun setParameter(value: Any?) {
        errorText = "错误的参数 $value"
    }

}