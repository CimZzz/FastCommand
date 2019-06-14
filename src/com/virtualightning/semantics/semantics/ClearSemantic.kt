package com.virtualightning.semantics.semantics

import com.virtualightning.actions.ClearAction
import com.virtualightning.base.generics.BaseAction
import com.virtualightning.base.semantics.BaseSemantic
import com.virtualightning.base.semantics.BaseSemanticRun
import com.virtualightning.tools.MessageLooper

class ClearSemantic: BaseSemantic() {
    override val syntax: String = "clear"

    override val intro: String = "清空当前控制台全部信息"



    override fun generateRun(): BaseSemanticRun =
        object: BaseSemanticRun(this) {
            override fun doRun(messageLooper: MessageLooper<BaseAction>?): Any? {
                messageLooper?.sendAction(ClearAction)
                return super.doRun(messageLooper)
            }
        }
}