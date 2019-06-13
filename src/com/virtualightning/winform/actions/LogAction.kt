package com.virtualightning.winform.actions

import com.virtualightning.base.ui.BaseWindowAction
import java.awt.Color

data class LogAction(
    val logColor: Color,
    val message: String
): BaseWindowAction()