package com.virtualightning.semantics.tree

import com.virtualightning.base.semantics.BaseSemantic

class TreeManager {
    private val semanticMap = HashMap<String, HashMap<String, BaseSemantic>>()
    private val rootTreeNode: TreeNode = TreeNode('0')

    fun addSemantic(namespace: String, semantic: BaseSemantic): Boolean {
        val subMap = semanticMap[namespace]?: {
            val map = HashMap<String, BaseSemantic>()
            semanticMap[namespace] = map
            map
        }()

        if(subMap.containsKey(semantic.syntax))
            return false

        addTreeNode(namespace, semantic)
        subMap[semantic.syntax] = semantic
        return true
    }

    fun destroy() {

    }

    fun findSemanticBy(namespace: String, syntax: String): BaseSemantic? {
        val subMap = semanticMap[namespace]?:return null
        return subMap[syntax]
    }

    private fun addTreeNode(namespace: String, semantic: BaseSemantic) {
        var currentNode = rootTreeNode
        if(namespace.isNotEmpty()) {
            for(char in namespace)
                currentNode = currentNode.appendNext(char = char)

            currentNode = currentNode.appendNext('.')
        }

        for(char in semantic.syntax)
            currentNode = currentNode.appendNext(char = char)

        currentNode.semantic = semantic
    }
}