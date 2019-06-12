package com.virtualightning.core

import com.virtualightning.Main
import java.io.File
import java.io.FileInputStream
import java.io.PrintWriter
import java.util.*

object CoreConstants {
    /**
     * 当前 jar 包路径
     */
    lateinit var currentJarPath: String

    /**
     * 当前 jar 包所在文件夹路径
     */
    lateinit var currentJarParentPath: String

    /**
     * 命令行进程端口号
     */
    var commandLinePort: Int = 9989





    internal fun initBase() {
        currentJarPath = Main::class.java.protectionDomain.codeSource.location.path
        currentJarParentPath = File(currentJarPath).parent
    }

    private fun readProperties(properties: Properties) {
        commandLinePort = properties.getProperty("commandLinePort").toInt()
    }

    internal fun writeDefaultConfig(write: PrintWriter) {
        write.println("//命令行端口号")
        write.println("commandLinePort=$commandLinePort")
    }


    internal fun initDefault(): Boolean {
        try {
            val file = File(currentJarParentPath, "config.properties")
            if(file.exists()) {
                val prop = Properties()
                val fileInputStream = FileInputStream(file)
                prop.load(fileInputStream)
                fileInputStream.close()
                readProperties(prop)
                CoreApp.reportMessage("初始化缺省配置文件")
            }
        }
        catch (e: Throwable) {
            CoreApp.reportError("读取配置文件时发生错误: ${e.message}")
            return false
        }

        CoreApp.reportWarn("未找到缺省配置文件")
        return true
    }

    internal fun initFromFile(file: File): Boolean {
        try {
            val prop = Properties()
            val fileInputStream = FileInputStream(file)
            prop.load(fileInputStream)
            fileInputStream.close()
            readProperties(prop)
            CoreApp.reportMessage("从 ${file.absolutePath} 中初始化")
        }
        catch (e: Throwable) {
            CoreApp.reportError("读取配置文件时发生错误: ${e.message}")
            return false
        }
        return true
    }
}