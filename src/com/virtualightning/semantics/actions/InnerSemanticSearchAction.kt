package com.virtualightning.semantics.actions

import com.virtualightning.base.generics.BaseAction
import com.virtualightning.base.generics.BaseTreeAction
import com.virtualightning.tools.MessageLooper

class InnerSemanticSearchAction(
    messageLooper: MessageLooper<BaseAction>,
    val syntaxStr: String
) : BaseTreeAction(messageLooper)