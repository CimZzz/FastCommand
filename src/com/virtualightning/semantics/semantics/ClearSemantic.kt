package com.virtualightning.semantics.semantics

import com.virtualightning.actions.ClearAction
import com.virtualightning.base.semantics.BaseSemantic
import com.virtualightning.base.semantics.BaseSemanticRun
import com.virtualightning.core.CoreApp

class ClearSemantic: BaseSemantic() {
    override val syntax: String
        get() = "clear"

    override fun generateRun(): BaseSemanticRun =
        object: BaseSemanticRun(this) {
            override fun doRun(): Any? {
                CoreApp.sendGlobalMessage(ClearAction)
                return super.doRun()
            }
        }
}