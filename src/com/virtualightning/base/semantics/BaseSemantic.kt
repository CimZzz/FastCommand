package com.virtualightning.base.semantics

abstract class BaseSemantic {
    /**
     * 句法
     */
    abstract val syntax: String

    /**
     * 配置参数
     */
    open val configs: Array<BaseConfig>? = null

    /**
     * 值参数
     */
    open val parameter: BaseParameter? = null

    /**
     * 生成可执行对象
     */
    abstract fun generateRun(): BaseSemanticRun

    fun toDetailString(): String {
        return buildString {
            appendln("命令名: $syntax")
            if(parameter != null)
                appendln("接受的参数为: ${parameter!!.parameterIntro}")

            appendln()

            val configArr = configs
            if(configArr != null) {
                appendln("可选配置:")
                for (config in configArr) {
                    appendln("\t ${config.syntax}\t${config.syntaxIntro}")
                    val configParamArr = config.parameter
                    if(configParamArr != null) {
                        appendln("\t\t所需参数:")
                        for(param in configParamArr) {
                            appendln("\t\t\t${param.parameterIntro}\t类型: ${param.parameterType}")
                        }
                    }
                    appendln()
                }
            }
            else appendln("没有其他可选配置")
        }
    }
}