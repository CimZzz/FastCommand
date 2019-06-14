package com.virtualightning.interfaces

import com.virtualightning.base.generics.BaseAction
import com.virtualightning.tools.MessageLooper

typealias Run = () -> Unit
typealias ActionLooper = MessageLooper<BaseAction>