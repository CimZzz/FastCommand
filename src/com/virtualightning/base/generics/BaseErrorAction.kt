package com.virtualightning.base.generics

abstract class BaseErrorAction(
    private val errorName: String,
    private val errorText: String
): BaseAction() {
    override fun toString(): String {
        return "$errorName: $errorText"
    }
}