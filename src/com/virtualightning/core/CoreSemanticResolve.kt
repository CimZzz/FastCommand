package com.virtualightning.core

import com.virtualightning.base.bundle.BaseResultBundle
import com.virtualightning.base.generics.BaseAction
import com.virtualightning.base.semantics.BaseBoundedConfig
import com.virtualightning.base.semantics.BaseConfig
import com.virtualightning.base.semantics.BaseParameter
import com.virtualightning.base.semantics.BaseSemantic
import com.virtualightning.been.SemanticResolveBean
import com.virtualightning.tools.MessageLooper
import java.util.*

object CoreSemanticResolve {
    fun resolve(semantic: BaseSemantic, args: Array<String>?, messageLooper: MessageLooper<BaseAction>? = null): ResolveResultBean {
        val baseRun = semantic.generateRun()

        var config: BaseConfig? = null
        var configParamIdx = 0
        var configParameter: BaseParameter? = null
        var configParameterList: LinkedList<Any?>? = null

        if(args != null) {
            for (cmd in args) {
                if (baseRun.isMarkEnd()) {
                    baseRun.errorText = "${semantic.syntax} 多余的参数 $cmd"
                    break
                }
                val lastConfig = config
                val lastConfigParameter = configParameter
                if (lastConfig != null) {
                    if (lastConfigParameter != null) {
                        val paramValue = lastConfigParameter.checkType(cmd)

                        if (paramValue == null) {
                            baseRun.errorText = "${lastConfig.syntax} 的参数类型不正确，错误参数: $cmd"
                            break
                        }

                        if (configParameterList == null)
                            configParameterList = LinkedList()

                        configParameterList.add(paramValue)

                        configParamIdx++

                        if (lastConfig.parameter!!.size <= configParamIdx) {
                            //end
                            baseRun.setConfig(lastConfig.syntax, *configParameterList.toArray())
                            config = null
                            configParameter = null
                            configParameterList.clear()
                            configParameterList = null

                            if (baseRun.errorText != null)
                                break
                        } else configParameter = lastConfig.parameter!![configParamIdx]

                        continue
                    }
                }

                val configArr = semantic.configs
                if (configArr != null) {
                    var isFound = false
                    for (conf in configArr) {
                        if (conf.syntax == cmd) {
                            if (conf.parameter != null && conf.parameter!!.isNotEmpty()) {
                                config = conf
                                configParamIdx = 0
                                configParameter = conf.parameter!![0]
                            } else baseRun.setConfig(conf.syntax)

                            isFound = true
                            break
                        }
                    }

                    if (baseRun.errorText != null)
                        break

                    if (isFound)
                        continue
                }

                val parameter = semantic.parameter
                if (parameter != null) {
                    val paramValue = parameter.checkType(cmd)
                    if (paramValue == null) {
                        baseRun.errorText = "执行参数类型不正确，错误参数：$cmd"
                        break
                    }
                    baseRun.setParameter(paramValue)
                } else baseRun.errorText = "${semantic.syntax}多余的参数 $cmd"

                if (baseRun.errorText != null)
                    break
            }
        }

        if(baseRun.errorText != null)
            return ResolveResultBean(false, null, baseRun.errorText)

        if(config != null) {
            if(baseRun.isMarkEnd())
                return ResolveResultBean(false, null, "多余的参数 ${config.syntax}")

            if(config is BaseBoundedConfig)
                return ResolveResultBean(false, null, "${config.syntax} 缺少所需参数")

            if(configParameterList != null)
                baseRun.setConfig(config.syntax, *configParameterList.toArray())
            else baseRun.setConfig(config.syntax)

            if(baseRun.errorText != null)
                return ResolveResultBean(false, null, baseRun.errorText)
        }

        val result = baseRun.run(messageLooper)
        if(baseRun.errorText != null)
            return ResolveResultBean(false, null, baseRun.errorText)

        return ResolveResultBean(true, result, null)
    }

    fun resolveExecStr(execStr: String): SemanticResolveBean? {
        val currentExecStr = execStr.trim()
        var namespace = ""
        var syntax = ""
        var params: Array<String>? = null

        var isForNamespace = true
        var idx = 0
        val length = currentExecStr.length

        loop@ while(idx < length) {
            val char = currentExecStr[idx]
            idx ++
            when(char) {
                '.' -> {
                    if(idx == 1)
                        return null
                    isForNamespace = false
                }
                ' ' -> {
                    break@loop
                }
                else -> {
                    if(isForNamespace)
                        namespace += char
                    else syntax += char
                }
            }
        }

        if(syntax.isEmpty()) {
            if(isForNamespace && namespace.isNotEmpty()) {
                syntax = namespace
                namespace = ""
            }
            else return null
        }

        val paramsStr = currentExecStr.substring(idx)
        if(paramsStr.isNotEmpty())
            params = paramsStr.split(' ').toTypedArray()

        return SemanticResolveBean(
            namespace = namespace,
            syntax = syntax,
            params = params
        )
    }
}


data class ResolveResultBean(
    val isSuccess: Boolean,
    val result: Any?,
    val msg: String?
): BaseResultBundle()