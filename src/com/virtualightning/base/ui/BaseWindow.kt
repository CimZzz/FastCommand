package com.virtualightning.base.ui

import java.awt.GraphicsConfiguration
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import javax.swing.JFrame
import javax.swing.JRootPane

abstract class BaseWindow: JFrame {

    constructor() : super()
    constructor(gc: GraphicsConfiguration?) : super(gc)
    constructor(title: String?) : super(title)
    constructor(title: String?, gc: GraphicsConfiguration?) : super(title, gc)

    init {
        defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        doInit()
        isVisible = true
    }
    override fun dispose() {
        super.dispose()
        doDestroy()
    }


    private fun doInit() {
        addComponentListener(object: ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                doUpdate()
            }
        })
        onInit()
        windowState = WindowState.Init
    }

    private fun doUpdate() {
        onUpdate()
    }

    private fun doDestroy() {
        onDestroy()
        windowState = WindowState.Destroy
    }


    /*生命周期回调*/
    var windowState: WindowState = WindowState.None
    private set

    /**
     * 初始化回调
     * 在构造函数时调用，然后显示窗口
     */
    abstract fun onInit()

    /**
     * 更新回调
     * 窗体尺寸发生变化时触发此方法
     */
    open fun onUpdate() {}

    /**
     * 销毁回调
     */
    open fun onDestroy() {}



    /*快捷方法*/

    /**
     * 去掉顶部操作栏
     */
    protected fun removeActionBar() {
        this.isUndecorated = true // 去掉窗口的装饰
        this.getRootPane().windowDecorationStyle = JRootPane.NONE
    }

    /**
     * 屏幕居中
     */
    protected fun centerInScreen() {
        this.setLocationRelativeTo(null)
    }
}


enum class WindowState {
    None,
    Init,
    Visible,
    Destroy
}