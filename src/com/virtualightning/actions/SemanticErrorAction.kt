package com.virtualightning.actions

import com.virtualightning.base.generics.BaseErrorAction

class SemanticErrorAction(
    errorText: String
): BaseErrorAction("语法错误", errorText)