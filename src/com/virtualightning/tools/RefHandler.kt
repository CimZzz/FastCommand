package com.virtualightning.tools

import java.lang.ref.WeakReference


class RefHandler<T, E>(
    obj: E,
    private val handler: MessageHandlerWidthObject<E, T>
): MessageHandler<T> {
    private val weakRef = WeakReference<E>(obj)

    override fun invoke(message: T) {
        val obj = weakRef.get()?:return
        handler(obj, message)
    }
}