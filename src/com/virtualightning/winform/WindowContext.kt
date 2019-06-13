package com.virtualightning.winform

import java.awt.Toolkit

object WindowContext {
    val screenWidth: Int by lazy {
        Toolkit.getDefaultToolkit().screenSize.width
    }

    val screenHeight: Int by lazy {
        Toolkit.getDefaultToolkit().screenSize.height
    }
}