package com.virtualightning.semantics.tasks

import com.virtualightning.base.generics.BaseAction
import com.virtualightning.base.generics.BaseTask
import com.virtualightning.semantics.actions.InnerInitTreeCompletedAction
import com.virtualightning.semantics.semantics.ClearSemantic
import com.virtualightning.semantics.tree.TreeManager
import com.virtualightning.tools.MessageLooper

class InitTreeTask(private val messageLooper: MessageLooper<BaseAction>?): BaseTask<Unit>(
        runnable = {
            val treeManager = TreeManager()
            try {
                treeManager.addSemantic("", ClearSemantic())
            }
            catch (e: Exception) {

            }
            messageLooper?.sendAction(InnerInitTreeCompletedAction(treeManager))
            Unit
        }
)