package com.virtualightning.semantics.semantics

import com.virtualightning.actions.LogAction
import com.virtualightning.actions.SemanticErrorAction
import com.virtualightning.base.generics.BaseAction
import com.virtualightning.base.semantics.BaseParameter
import com.virtualightning.base.semantics.BaseSemantic
import com.virtualightning.base.semantics.BaseSemanticRun
import com.virtualightning.core.CoreApp
import com.virtualightning.semantics.params.StringParams
import com.virtualightning.semantics.tree.SemanticTree
import com.virtualightning.tools.MessageLooper

class HelpSemantic: BaseSemantic() {
    override val syntax: String = "help"
    override val intro: String = "打印命令详细帮助文本"

    override val parameter: BaseParameter? = StringParams("命令名")

    override fun generateRun(): BaseSemanticRun =
        object : BaseSemanticRun(this) {
            var semanticName: String? = null


            override fun setParameter(value: Any?) {
                semanticName = value as String
                markEnd()
            }

            override fun doRun(messageLooper: MessageLooper<BaseAction>?): Any? {
                val name = semanticName
                if(name != null) {
                    val semantic = CoreApp.findSemanticOnly(name)
                    if(semantic != null)
                        messageLooper?.sendAction(LogAction(semantic.toDetailString()))
                    else messageLooper?.sendAction(SemanticErrorAction("不存在此命令 $name"))
                }
                else messageLooper?.sendAction(SemanticErrorAction("缺少命令名"))

                return super.doRun(messageLooper)
            }
        }
}