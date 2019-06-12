package com.virtualightning.base.semantics

abstract class BaseParameter {
    abstract val parameterType: String
    abstract val parameterIntro: String
    abstract fun checkType(parameter: Any?): Any?
}