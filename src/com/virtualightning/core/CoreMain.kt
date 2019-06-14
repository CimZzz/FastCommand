package com.virtualightning.core

import com.virtualightning.semantics.semantics.InitializationSemantic
import com.virtualightning.winform.MainFrame

object CoreMain {

    /**
     * 启动
     */
    fun start(args: Array<String>) {
        CoreConstants.initBase()

        if(args.isEmpty()) {
            CoreConstants.initDefault()
            startCommand()
            return
        }

        initParams(args)
    }

    private fun initParams(args: Array<String>) {
        val result = CoreSemanticResolve.resolve(InitializationSemantic(), args)
        if(!result.isSuccess)
            CoreApp.reportError(result.msg)
    }

    fun startCommand() {
//        CoreApp.reportMessage("启动命令行进程")
        CoreApp.reportMessage("暂不支持命令行进程")
    }

    fun startFrame() {
        CoreApp.reportMessage("启动窗口应用程序")
        MainFrame()
    }
}