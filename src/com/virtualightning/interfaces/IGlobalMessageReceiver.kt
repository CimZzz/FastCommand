package com.virtualightning.interfaces

import com.virtualightning.base.generics.BaseAction

interface IGlobalMessageReceiver {
    fun onReceiverGlobalMessage(action: BaseAction)
}