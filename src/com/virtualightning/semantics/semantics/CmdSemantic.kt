package com.virtualightning.semantics.semantics

import com.virtualightning.actions.CmdCreateAction
import com.virtualightning.base.generics.BaseAction
import com.virtualightning.base.semantics.BaseSemantic
import com.virtualightning.base.semantics.BaseSemanticRun
import com.virtualightning.tools.MessageLooper

class CmdSemantic: BaseSemantic() {
    override val syntax: String = "cmd"

    override val intro: String = "打开一个无 tty 的终端"

    override fun generateRun(): BaseSemanticRun =
        object : BaseSemanticRun(this) {
            override fun doRun(messageLooper: MessageLooper<BaseAction>?): Any? {
                messageLooper?.sendAction(CmdCreateAction)
                return super.doRun(messageLooper)
            }
        }
}