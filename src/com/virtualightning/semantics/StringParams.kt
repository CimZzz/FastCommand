package com.virtualightning.semantics

import com.virtualightning.base.semantics.BaseParameter

class StringParams(
    override val parameterIntro: String
): BaseParameter() {
    override val parameterType: String = "String(字符串)"


    override fun checkType(parameter: Any?): Any? {
        if(parameter == null)
            return null
        return parameter as? String ?: parameter.toString()
    }
}