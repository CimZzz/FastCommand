package com.virtualightning.semantics.semantics

import com.virtualightning.actions.CloseAction
import com.virtualightning.base.generics.BaseAction
import com.virtualightning.base.semantics.BaseSemantic
import com.virtualightning.base.semantics.BaseSemanticRun
import com.virtualightning.tools.MessageLooper

class CloseSemantic: BaseSemantic() {
    override val syntax: String = "close"

    override val intro: String = "关闭应用程序"

    override fun generateRun(): BaseSemanticRun =
        object: BaseSemanticRun(this) {
            override fun doRun(messageLooper: MessageLooper<BaseAction>?): Any? {
                messageLooper?.sendAction(CloseAction())
                return super.doRun(messageLooper)
            }
        }
}