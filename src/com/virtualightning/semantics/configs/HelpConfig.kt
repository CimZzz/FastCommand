package com.virtualightning.semantics.configs

import com.virtualightning.base.semantics.BaseConfig
import com.virtualightning.base.semantics.BaseParameter

object HelpConfig: BaseConfig() {
    override val syntax: String = "-help"
    override val syntaxIntro: String = "显示命令详细文本"
    override val parameter: Array<BaseParameter>? = null
}