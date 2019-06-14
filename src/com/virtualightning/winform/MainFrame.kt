package com.virtualightning.winform

import com.sun.awt.AWTUtilities
import com.virtualightning.actions.*
import com.virtualightning.base.generics.BaseAction
import com.virtualightning.base.generics.BaseErrorAction
import com.virtualightning.base.ui.BaseWindow
import com.virtualightning.core.CoreApp
import com.virtualightning.defaults.DefaultReporter
import com.virtualightning.interfaces.IGlobalMessageReceiver
import com.virtualightning.interfaces.IReporter
import com.virtualightning.semantics.cmd.CmdLooper
import com.virtualightning.tools.MessageLooper
import com.virtualightning.tools.RefHandler
import com.virtualightning.winform.actions.*
import com.virtualightning.winform.widgets.LogArea
import java.awt.BorderLayout
import java.awt.Color
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JScrollPane
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener


/**
 * 主要窗口
 */
class MainFrame: BaseWindow() {
    private lateinit var inputField: JTextField
    private lateinit var logArea: LogArea

    private lateinit var messageLooper: MessageLooper<BaseAction>

    private lateinit var frameBus: FrameBus

    private var cmdLooper: CmdLooper? = null

    override fun onInit() {
        isUndecorated
        setSize(1000, 800)
        title = "Fast Command"
        centerInScreen()
        contentPane.layout = BorderLayout()
        contentPane.background = Color(88, 88, 88)

        inputField = JTextField()
        inputField.foreground = Color.white
        inputField.caretColor = Color.white
        inputField.selectionColor = Color(88, 88, 88)
        inputField.background = Color(43, 43, 43)
        inputField.addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent?) {
                val event = e?:return
                when(event.keyChar.toInt()) {
                    KeyEvent.VK_ENTER -> frameBus.sendEnterAction()
                    KeyEvent.VK_ESCAPE -> frameBus.sendEscapeAction()
                    KeyEvent.VK_UP -> frameBus.sendUpAction()
                    KeyEvent.VK_DOWN -> frameBus.sendDownAction()
                }
            }

            override fun keyPressed(e: KeyEvent?) {
            }

            override fun keyReleased(e: KeyEvent?) {
            }
        })
        inputField.document.addDocumentListener(object: DocumentListener{
            override fun changedUpdate(e: DocumentEvent?) {
            }

            override fun insertUpdate(e: DocumentEvent?) {
            }

            override fun removeUpdate(e: DocumentEvent?) {
            }
        })

        logArea = LogArea()
        logArea.background = Color(43, 43, 43)
        val scrollPane = JScrollPane(logArea)
        scrollPane.background = Color(88,88,88)
        scrollPane.verticalScrollBar.background = Color(88,88,88)

        contentPane.add(scrollPane, BorderLayout.CENTER)
        contentPane.add(inputField, BorderLayout.SOUTH)

        messageLooper = MessageLooper(RefHandler(this@MainFrame) {
            frame, message ->
            frame.onReceiverMessage(message)
        })
        /*更改全局配置*/
        frameBus = FrameBus(messageLooper)
        CoreApp.configReporter(frameBus)
        CoreApp.configGlobalMessageReceiver(frameBus)
        CoreApp.initEnvironment()
        messageLooper.startAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        messageLooper.destroy()
        /*还原配置*/
        CoreApp.destroyEnvironment()
    }

    private fun onReceiverMessage(message: BaseAction) {
        when(message) {
            /*log message*/
            is LogAction -> log(message)

            /*operator message*/
            is EnterAction -> execSemantic()

            /*semantic message*/
            is InitTreeAction -> log(LogAction("初始化语法树中，请稍后"))
            is InitTreeCompletedAction -> log(LogAction("初始化语法树完成", LogAction.SUCCESS))
            is CmdCreateAction -> createCmdLooper(message)
            is CmdInitAction -> log(LogAction("初始化命令行中..."))
            is CmdInitCompletedAction -> log(LogAction("初始化命令行完成", LogAction.SUCCESS))
            is CmdCompletedAction -> destroyCmdLooper(message)
            is BaseErrorAction -> error(message)

            /*global message*/
            is ClearAction -> clear(message)
            is CloseAction -> dispose()
        }
    }

    private fun log(action: LogAction) {
        when(action.messageType) {
            LogAction.WARN -> logArea.warn(action.message)
            LogAction.SUCCESS -> logArea.success(action.message)
            LogAction.ERROR -> logArea.error(action.message)
            else -> logArea.message(action.message)
        }
        logArea.caretPosition = logArea.text.length
    }

    private fun error(action: BaseErrorAction) {
        logArea.error(action.toString())
    }

    @Suppress("UNUSED_PARAMETER")
    private fun clear(action: ClearAction) {
        logArea.clear()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun createCmdLooper(action: CmdCreateAction) {
        val cmdLooper = CmdLooper(this.messageLooper)
        cmdLooper.startAsync()
        this.cmdLooper = cmdLooper
        logArea.message("hello world!")
    }

    @Suppress("UNUSED_PARAMETER")
    private fun destroyCmdLooper(action: CmdCompletedAction) {
        this.cmdLooper = null
        logArea.message("bye bye world!")
    }

    private fun execSemantic() {
        val execStr = inputField.text
        inputField.text = ""
        logArea.message("> $execStr")
        resetInputUI()
        val cmdLooper = this.cmdLooper
        if(cmdLooper != null)
            cmdLooper.execCmd(execStr)
        else CoreApp.execSemantic(this.messageLooper, execStr)
    }

    private fun resetInputUI() {
        inputField.text = ""
    }
}



private class FrameBus(
    private val messageLooper: MessageLooper<BaseAction>
): IReporter, IGlobalMessageReceiver {

    override fun onReceiverGlobalMessage(action: BaseAction) {
        messageLooper.sendAction(action)
    }

    fun sendEnterAction() {
        messageLooper.sendAction(EnterAction)
    }

    fun sendEscapeAction() {
        messageLooper.sendAction(EscAction)
    }

    fun sendUpAction() {
        messageLooper.sendAction(UpAction)
    }

    fun sendDownAction() {
        messageLooper.sendAction(DownAction)
    }

    override fun reportMessage(any: Any?) {
        DefaultReporter.reportMessage(any)
        messageLooper.sendAction(LogAction(any.toString()))
    }

    override fun reportWarn(any: Any?) {
        DefaultReporter.reportWarn(any)
        messageLooper.sendAction(LogAction(any.toString(), LogAction.WARN))
    }

    override fun reportError(any: Any?) {
        DefaultReporter.reportError(any)
        messageLooper.sendAction(LogAction(any.toString(), LogAction.ERROR))
    }
}