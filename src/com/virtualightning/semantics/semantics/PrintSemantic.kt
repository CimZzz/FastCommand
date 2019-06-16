package com.virtualightning.semantics.semantics

import com.virtualightning.actions.LogAction
import com.virtualightning.base.generics.BaseAction
import com.virtualightning.base.semantics.BaseParameter
import com.virtualightning.base.semantics.BaseSemantic
import com.virtualightning.base.semantics.BaseSemanticRun
import com.virtualightning.semantics.params.StringParams
import com.virtualightning.tools.MessageLooper
import java.util.*

class PrintSemantic: BaseSemantic() {
    override val syntax: String = "print"

    override val intro: String = "打印指定字符串"

    override val parameter: BaseParameter?
        get() = StringParams("打印的字符串")

    override fun generateRun(): BaseSemanticRun =
        object : BaseSemanticRun(this) {
            var printList: LinkedList<String>? = null

            override fun setParameter(value: Any?) {
                val printList = this.printList?:{
                    val printList = LinkedList<String>()
                    this.printList = printList
                    printList
                }()

                printList.add(value as String)
            }


            override fun doRun(messageLooper: MessageLooper<BaseAction>?): Any? {
                val str = buildString {
                    printList?.forEach { str ->
                        append(str)
                    }
                }
                messageLooper?.sendAction(LogAction(str))
                return super.doRun(messageLooper)
            }
        }
}