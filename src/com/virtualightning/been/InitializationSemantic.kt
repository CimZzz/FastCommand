package com.virtualightning.been

import com.virtualightning.base.semantics.BaseConfig
import com.virtualightning.base.semantics.BaseParameter
import com.virtualightning.base.semantics.BaseSemantic
import com.virtualightning.base.semantics.BaseSemanticRun
import com.virtualightning.core.CoreApp
import com.virtualightning.core.CoreConstants
import com.virtualightning.core.CoreMain
import com.virtualightning.semantics.HelpConfig
import com.virtualightning.semantics.StringParams
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter

class InitializationSemantic: BaseSemantic() {
    override val syntax: String = "初始化"
    override val configs: Array<BaseConfig>? = arrayOf(
        HelpConfig,
        StartCommandConfig(),
        StartFrameConfig(),
        ConfigurationConfig(),
        DefaultConfigurationConfig()
    )
    override val parameter: BaseParameter? = null

    override fun generateRun(): BaseSemanticRun = InitializationSemanticRun(this)


    private class StartCommandConfig: BaseConfig() {
        override val syntax: String = "-start"
        override val syntaxIntro: String = "启动命令行进程"
    }

    private class StartFrameConfig: BaseConfig() {
        override val syntax: String = "-startFrame"
        override val syntaxIntro: String = "启动窗口应用程序"
    }

    private class ConfigurationConfig: BaseConfig() {
        override val syntax: String = "-config"
        override val syntaxIntro: String = "设置配置文件位置，缺省为同目录下"

        override val parameter: Array<BaseParameter>?
            get() = arrayOf(StringParams("配置文件路径"))
    }

    private class DefaultConfigurationConfig: BaseConfig() {
        override val syntax: String = "-defaultConfig"
        override val syntaxIntro: String = "在指定目录下生成缺省的配置文件"

        override val parameter: Array<BaseParameter>?
            get() = arrayOf(StringParams("配置文件夹路径"))
    }

    private class InitializationSemanticRun(semantic: BaseSemantic) : BaseSemanticRun(semantic) {
        var configFile: File? = null
        var openApproach: Int = 0

        override fun setConfig(configSyntax: String, vararg params: Any?) {
            when (configSyntax) {
                "-defaultConfig"-> {
                    catchRun {
                        if (checkParamsLength(configSyntax, 1, params)) {
                            val configPath = params[0] as String
                            val file = File(configPath)
                            if (!file.exists()) {
                                errorText = "$configSyntax 路径不存在"
                                return@catchRun
                            }

                            if(!file.isDirectory) {
                                errorText = "$configSyntax 必须指定文件夹"
                                return@catchRun
                            }

                            markEnd {
                                catchRun {
                                    val writer = PrintWriter(FileOutputStream(File(file, "config.properties")))
                                    CoreConstants.writeDefaultConfig(writer)
                                    writer.flush()
                                    writer.close()
                                    CoreApp.reportMessage("在 ${file.absolutePath} 下成功生成缺省配置文件 config.properties")
                                }
                            }
                        }
                    }
                }
                "-config" -> {
                    catchRun {
                        if (checkParamsLength(configSyntax, 1, params)) {
                            val configPath = params[0] as String
                            val file = File(configPath)
                            if (!file.exists())
                                errorText = "$configSyntax 配置文件不存在"
                            else configFile = file
                        }
                    }
                }
                "-start" -> {
                    openApproach = 0
                    markEnd()
                }
                "-startFrame" -> {
                    openApproach = 1
                    markEnd()
                }
                "-help" -> {
                    markEnd {
                        CoreApp.reportMessage(semantic.toDetailString())
                        return@markEnd null
                    }
                }
            }
        }

        override fun doRun(): Any? {
            val configFile = this.configFile
            val initResult = if (configFile != null)
                CoreConstants.initFromFile(configFile)
            else CoreConstants.initDefault()

            if(!initResult)
                return null

            when (openApproach) {
                0 -> {
                    CoreMain.startCommand()
                }
                1 -> {
                    CoreMain.startFrame()
                }
            }

            return null
        }

    }
}