package com.virtualightning.semantics.tree

import com.virtualightning.base.semantics.BaseSemantic

class TreeNode (
    val nodeName: Char
) {
    private val nodeMap: HashMap<Char, TreeNode> by lazy { HashMap<Char, TreeNode>() }
    var semantic: BaseSemantic? = null

    fun appendNext(char: Char): TreeNode {
        return nodeMap[char]?:{
            val node = TreeNode(char)
            nodeMap[char] = node
            node
        }()
    }
}