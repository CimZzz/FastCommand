package com.virtualightning.semantics.tree

import com.virtualightning.base.generics.BaseTreeAction
import com.virtualightning.core.CoreApp
import com.virtualightning.semantics.tree.actions.InitTreeAction
import com.virtualightning.tools.MessageLooper
import com.virtualightning.tools.RefHandler

object SemanticTree {
    private var state = TreeState.default
    private var messageLooper: MessageLooper<BaseTreeAction>? = null

    fun initTree() {
        val messageLooper = MessageLooper<BaseTreeAction>(RefHandler(this) {
            tree, message ->
            tree.onReceiverMessage(message)
        })
        messageLooper.startAsync()
        this.messageLooper = messageLooper

        sendAction(InitTreeAction(messageLooper))
    }

    fun destroyTree() {
        this.messageLooper?.destroy()
    }

    private fun sendAction(action: BaseTreeAction) {
        this.messageLooper?.sendAction(action)
    }

    private fun onReceiverMessage(action: BaseTreeAction) {
        if(action is InitTreeAction) {
            CoreApp.sendGlobalMessage(action)

        }
    }
}

private enum class TreeState {
    default,
    all,
    readOnly,
    locked
}