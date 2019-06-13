package com.virtualightning.winform

import com.virtualightning.base.generics.BaseAction
import com.virtualightning.base.ui.BaseWindow
import com.virtualightning.base.ui.BaseWindowAction
import com.virtualightning.core.CoreApp
import com.virtualightning.interfaces.IGlobalMessageReceiver
import com.virtualightning.interfaces.IReporter
import com.virtualightning.semantics.tree.actions.InitTreeAction
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

    override fun onInit() {
        setSize(1000, 800)
        title = "Fast Command"
        centerInScreen()
        contentPane.layout = BorderLayout()


        inputField = JTextField()
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
                CoreApp.reportMessage("123")
            }

            override fun insertUpdate(e: DocumentEvent?) {
                CoreApp.reportWarn("456")
            }

            override fun removeUpdate(e: DocumentEvent?) {
                CoreApp.reportError("789")
            }
        })

        logArea = LogArea()
        val scrollPane = JScrollPane(logArea)

        contentPane.add(scrollPane, BorderLayout.CENTER)
        contentPane.add(inputField, BorderLayout.SOUTH)

        messageLooper = MessageLooper(RefHandler(this@MainFrame) {
            frame, message ->
            frame.onReceiverMessage(message)
        })
        messageLooper.startAsync()
        /*更改全局配置*/
        frameBus = FrameBus(messageLooper)
        CoreApp.configReporter(frameBus)
        CoreApp.configGlobalMessageReceiver(frameBus)
        CoreApp.initEnvironment()
    }

    override fun onDestroy() {
        super.onDestroy()

        messageLooper.destroy()
        /*还原配置*/
        CoreApp.destroyEnvironment()
    }

    private fun onReceiverMessage(message: BaseAction) {
        when(message) {
            is InitTreeAction -> log(LogAction(Color.BLACK, "初始化语法树中，请稍后"))
            is LogAction -> log(message)
        }
    }

    private fun log(action: LogAction) {
        logArea.temp(action.logColor, action.message)
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
        messageLooper.sendAction(LogAction(
            logColor = Color.BLACK,
            message = any.toString()
        ))
    }

    override fun reportWarn(any: Any?) {
        messageLooper.sendAction(LogAction(
            logColor = Color.YELLOW,
            message = any.toString()
        ))
    }

    override fun reportError(any: Any?) {
        messageLooper.sendAction(LogAction(
            logColor = Color.RED,
            message = any.toString()
        ))
    }
}