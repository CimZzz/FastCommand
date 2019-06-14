package com.virtualightning.core

import com.virtualightning.base.generics.BaseAction
import com.virtualightning.defaults.DefaultReporter
import com.virtualightning.interfaces.IGlobalMessageReceiver
import com.virtualightning.interfaces.IReporter
import com.virtualightning.interfaces.Run
import com.virtualightning.semantics.tree.SemanticTree
import com.virtualightning.tools.MessageLooper
import com.virtualightning.tools.RefHandler
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object CoreApp {
    /*环境初始化*/


    /**
     * 初始化环境
     */
    fun initEnvironment() {
        messageLooper.startAsync()
        threadPool = Executors.newCachedThreadPool()
        semanticTree = SemanticTree()
        semanticTree?.initTree()
    }

    /**
     * 销毁环境
     */
    fun destroyEnvironment() {
        messageLooper.destroy()
        threadPool?.shutdown()
        semanticTree?.destroyTree()
        semanticTree = null
        reporter = DefaultReporter
        globalMessageReceiver = null
    }

    /*日志*/
    private var reporter: IReporter = DefaultReporter

    fun configReporter(reporter: IReporter?) {
        this.reporter = reporter?:DefaultReporter
    }

    fun reportError(any: Any?) {
        reporter.reportError(any)
    }

    fun reportWarn(any: Any?) {
        reporter.reportWarn(any)
    }

    fun reportMessage(any: Any?) {
        reporter.reportMessage(any)
    }


    /*线程池*/
    private var threadPool: ExecutorService? = null

    fun exec(runnable: Run) {
        threadPool?.execute(runnable)
    }

    /*全局消息中心*/
    private var globalMessageReceiver: IGlobalMessageReceiver? = null
    private var messageLooper = MessageLooper<BaseAction> (
        RefHandler(this) {
            app, message ->
            app.onReceiverMessage(message)
        }
    )

    fun configGlobalMessageReceiver(messageReceiver: IGlobalMessageReceiver?) {
        this.globalMessageReceiver = messageReceiver
    }

    fun sendGlobalMessage(baseAction: BaseAction) {
        this.messageLooper.sendAction(baseAction)
    }

    private fun onReceiverMessage(action: BaseAction) {
        this.globalMessageReceiver?.onReceiverGlobalMessage(action)
    }

    /*语法树*/
    private var semanticTree: SemanticTree? = null

    fun execSemantic(messageLooper: MessageLooper<BaseAction>, execStr: String) {
        semanticTree?.tryExecSemantic(messageLooper, execStr)
    }
}