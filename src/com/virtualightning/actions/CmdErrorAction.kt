package com.virtualightning.actions

import com.virtualightning.base.generics.BaseErrorAction

class CmdErrorAction(
    errorText: String
): BaseErrorAction("Cmd错误", errorText)