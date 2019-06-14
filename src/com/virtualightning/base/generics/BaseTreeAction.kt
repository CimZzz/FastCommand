package com.virtualightning.base.generics

import com.virtualightning.tools.MessageLooper

abstract class BaseTreeAction(val messageLooper: MessageLooper<BaseAction>): BaseAction()