package com.virtualightning.semantics.actions

import com.virtualightning.base.generics.BaseAction
import com.virtualightning.base.generics.BaseTreeAction
import com.virtualightning.tools.MessageLooper

class InnerModifyBaseAction(messageLooper: MessageLooper<BaseAction>, private val syncCode: Int) : BaseTreeAction(messageLooper) {

}