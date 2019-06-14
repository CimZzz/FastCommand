package com.virtualightning.been

data class SemanticResolveBean (
    val namespace: String,
    val syntax: String,
    val params: Array<String>?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SemanticResolveBean

        if (namespace != other.namespace) return false
        if (syntax != other.syntax) return false
        if (params != null) {
            if (other.params == null) return false
            if (!params.contentEquals(other.params)) return false
        } else if (other.params != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = namespace.hashCode()
        result = 31 * result + syntax.hashCode()
        result = 31 * result + (params?.contentHashCode() ?: 0)
        return result
    }
}