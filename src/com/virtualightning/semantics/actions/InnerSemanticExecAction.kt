package com.virtualightning.semantics.actions

import com.virtualightning.base.generics.BaseAction
import com.virtualightning.base.generics.BaseTreeAction
import com.virtualightning.tools.MessageLooper

class InnerSemanticExecAction(
    messageLooper: MessageLooper<BaseAction>,
    val execStr: String
) : BaseTreeAction(messageLooper)