package com.virtualightning.winform.widgets

import java.awt.Color
import javax.swing.JTextPane
import javax.swing.text.DefaultCaret
import javax.swing.text.Style
import javax.swing.text.StyleConstants

class LogArea: JTextPane() {
    val redStyle : Style = addStyle("red",null)
    val greenStyle : Style = addStyle("green",null)
    val blackStyle : Style = addStyle("black",null)
    val whiteStyle : Style = addStyle("black",null)
    val yellowStyle : Style = addStyle("yellow",null)
    val tempStyle : Style = addStyle("temp",null)

    init {
        isEditable = false
        autoscrolls = true
        (caret as DefaultCaret).updatePolicy = DefaultCaret.ALWAYS_UPDATE
        foreground = Color.WHITE
        StyleConstants.setForeground(redStyle, Color.RED)
        StyleConstants.setForeground(greenStyle, Color.GREEN)
        StyleConstants.setForeground(yellowStyle, Color.YELLOW)
        StyleConstants.setForeground(blackStyle, Color.BLACK)
        StyleConstants.setForeground(whiteStyle, Color.white)
    }
    fun message(str : String) = document.insertString(document.length,str + "\n",whiteStyle)
    fun warn(str : String) = document.insertString(document.length,str + "\n",yellowStyle)
    fun success(str : String) = document.insertString(document.length,str + "\n", greenStyle)
    fun error(str : String) = document.insertString(document.length,str + "\n",redStyle)
    fun temp(color: Color, str: String) {
        StyleConstants.setForeground(tempStyle, color)
        document.insertString(document.length,str + "\n", tempStyle)
    }

    fun clear() = document.remove(0,document.length)
}
