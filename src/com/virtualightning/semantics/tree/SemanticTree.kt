package com.virtualightning.semantics.tree

import com.virtualightning.actions.InitTreeAction
import com.virtualightning.actions.InitTreeCompletedAction
import com.virtualightning.actions.SemanticErrorAction
import com.virtualightning.base.generics.BaseAction
import com.virtualightning.base.generics.BaseTreeAction
import com.virtualightning.base.semantics.BaseSemantic
import com.virtualightning.been.SemanticResolveBean
import com.virtualightning.core.CoreApp
import com.virtualightning.core.CoreSemanticResolve
import com.virtualightning.core.ResolveResultBean
import com.virtualightning.semantics.actions.*
import com.virtualightning.semantics.tasks.InitTreeTask
import com.virtualightning.tools.MessageLooper
import com.virtualightning.tools.RefHandler
import com.virtualightning.tools.SyncCode

class SemanticTree {
    private var state = TreeState.Default
    private var messageLooper: MessageLooper<BaseAction>? = null
    private var cmdLooper: MessageLooper<BaseAction>? = null

    private var treeManager: TreeManager? = null

    private val syncCode: SyncCode = SyncCode()

    fun initTree() {
        val messageLooper = MessageLooper<BaseAction>(RefHandler(this) {
            tree, message ->
            tree.onReceiverMessage(message)
        })
        messageLooper.startAsync()
        this.messageLooper = messageLooper

        sendAction(InnerInitTreeAction(messageLooper))
    }

    fun destroyTree() {
        syncCode.nextCode()
        this.messageLooper?.sendAction(InnerDestroyTreeAction())
    }

    fun tryExecSemantic(messageLooper: MessageLooper<BaseAction>, execString: String) {
        this.messageLooper?.sendAction(InnerSemanticExecAction(messageLooper, execString))
    }

    fun trySearchSemantic(messageLooper: MessageLooper<BaseAction>, syntaxStr: String) {
        this.messageLooper?.sendAction(InnerSemanticSearchAction(messageLooper, syntaxStr))
    }

    private fun sendAction(action: BaseAction) {
        this.messageLooper?.sendAction(action)
    }

    private fun onReceiverMessage(action: BaseAction) {
        if(state == TreeState.Destroy)
            return

        if(action is InnerDestroyTreeAction) {
            this.messageLooper?.destroy()
            this.treeManager?.destroy()
            this.cmdLooper?.destroy()
            this.state = TreeState.Destroy
            return
        }

        if(action is InnerInitTreeAction) {
            CoreApp.sendGlobalMessage(InitTreeAction())
            CoreApp.exec(InitTreeTask(messageLooper))
            return
        }

        if(action is InnerInitTreeCompletedAction) {
            this.treeManager = action.treeManager
            this.state = TreeState.All
            CoreApp.sendGlobalMessage(InitTreeCompletedAction())
            return
        }

        if(state == TreeState.Default) {
            sendSemanticError("语法树尚未初始化", action)
            return
        }

        when(action) {
            is InnerSemanticExecAction -> execSemantic(action)
        }
    }

    fun findSemanticBy(resolveBean: SemanticResolveBean): BaseSemantic? {
        return treeManager?.findSemanticBy(resolveBean.namespace, resolveBean.syntax)
    }

    fun findSemanticOnly(str: String): BaseSemantic? {
        val resolveBean = CoreSemanticResolve.resolveExecStr(str) ?: return null
        if(resolveBean.params != null)
            return null
        return findSemanticBy(resolveBean)
    }

    private fun execSemantic(action: InnerSemanticExecAction) {
        val resolveBean = CoreSemanticResolve.resolveExecStr(action.execStr)
        if(resolveBean == null) {
            sendSemanticError("未找到对应语法 code = 1 : ${action.execStr}", action)
            return
        }
        val semantic = findSemanticBy(resolveBean)
        if(semantic == null) {
            sendSemanticError("未找到对应语法 code = 2 : ${action.execStr}", action)
            return
        }

        val resultBean = CoreSemanticResolve.resolve(semantic, resolveBean.params, action.messageLooper)
        if(!resultBean.isSuccess)
            sendSemanticError(resultBean.msg?:"执行语法错误", action)
    }



    private fun sendSemanticError(errorText: String, fromAction: BaseAction) {
        sendPrivateAction(SemanticErrorAction(errorText), fromAction)
    }

    private fun sendPrivateAction(action: BaseAction, fromAction: BaseAction) {
        if(fromAction is BaseTreeAction)
            fromAction.messageLooper.sendAction(action)
        else CoreApp.sendGlobalMessage(action)
    }
}

private enum class TreeState {
    Default,
    All,
    CMD,
    Locked,
    Destroy
}