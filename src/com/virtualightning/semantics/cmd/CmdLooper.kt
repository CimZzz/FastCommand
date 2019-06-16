package com.virtualightning.semantics.cmd

import com.virtualightning.actions.*
import com.virtualightning.base.generics.BaseAction
import com.virtualightning.core.CoreApp
import com.virtualightning.interfaces.ActionLooper
import com.virtualightning.semantics.cmd.actions.InnerCmdDestroyAction
import com.virtualightning.semantics.cmd.actions.InnerCmdExecAction
import com.virtualightning.tools.RefHandler
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter

class CmdLooper(
    private val otherActionLooper: ActionLooper
) {
    private var state = CmdState.Init
    private val actionLooper: ActionLooper = ActionLooper(RefHandler(this) { looper, message ->
        looper.onReceiverMessage(message)
    })

    private var process: Process? = null
    private var printWriter: PrintWriter? = null

    fun startAsync() {
        CoreApp.exec {
            start()
        }
    }

    fun start() {
        try {
            otherActionLooper.sendAction(CmdInitAction)
            val process = Runtime.getRuntime().exec("/bin/bash")

            CoreApp.exec {
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                while (true) {
                    val x = reader.readLine() ?: break
                    otherActionLooper.sendAction(LogAction(x))
                }
            }
            CoreApp.exec {
                val reader = BufferedReader(InputStreamReader(process.errorStream))
                while (true) {
                    val x = reader.readLine() ?: break
                    otherActionLooper.sendAction(LogAction(x, LogAction.ERROR))
                }
            }

            printWriter = PrintWriter(process.outputStream)
            state = CmdState.Start
            otherActionLooper.sendAction(CmdInitCompletedAction)
            actionLooper.startAsync()
            this.process = process
            val exitCode = process.waitFor()
            if(exitCode != 0)
                otherActionLooper.sendAction(CmdErrorAction("异常退出 errorCode = $exitCode"))
        }
        catch (e: Exception) {
            e.printStackTrace()
            otherActionLooper.sendAction(CmdErrorAction("启动 Cmd 发生异常: ${e.message}"))
            actionLooper.destroy()
        }

        otherActionLooper.sendAction(CmdCompletedAction)
        destroy()
    }

    fun execCmd(execStr: String) {
        actionLooper.sendAction(InnerCmdExecAction(execStr))
    }

    private fun destroy() {
        actionLooper.sendAction(InnerCmdDestroyAction())
    }

    private fun onReceiverMessage(action: BaseAction) {
        if(state == CmdState.Destroy)
            return

        if(action is InnerCmdDestroyAction) {
            actionLooper.destroy()
            state = CmdState.Destroy
            return
        }

        when(action) {
            is InnerCmdExecAction -> {
                if(action.execStr == "#exit") {
                    try {
                        process?.destroy()
                    }
                    catch (e: Throwable){
                        e.printStackTrace()
                    }
                }
                else {
                    printWriter?.println(action.execStr)
                    printWriter?.flush()
                }
            }
        }
    }
}

private enum class CmdState {
    Init,
    Start,
    Destroy
}