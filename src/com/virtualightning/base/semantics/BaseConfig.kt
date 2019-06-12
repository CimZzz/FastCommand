package com.virtualightning.base.semantics

abstract class BaseConfig {
    abstract val syntax: String
    abstract val syntaxIntro: String
    open val parameter: Array<BaseParameter>? = null
}