package com.virtualightning.semantics.tasks

import com.virtualightning.base.generics.BaseAction
import com.virtualightning.base.generics.BaseTask
import com.virtualightning.semantics.actions.InnerInitTreeCompletedAction
import com.virtualightning.semantics.semantics.*
import com.virtualightning.semantics.tree.TreeManager
import com.virtualightning.tools.MessageLooper

class InitTreeTask(private val messageLooper: MessageLooper<BaseAction>?): BaseTask<Unit>(
        runnable = {
            val treeManager = TreeManager()
            try {
                treeManager.addSemantic("", ClearSemantic())
                treeManager.addSemantic("", CmdSemantic())
                treeManager.addSemantic("", CloseSemantic())
                treeManager.addSemantic("", PrintSemantic())
                treeManager.addSemantic("", HelpSemantic())
            }
            catch (e: Exception) {

            }
            messageLooper?.sendAction(InnerInitTreeCompletedAction(treeManager))
            Unit
        }
)