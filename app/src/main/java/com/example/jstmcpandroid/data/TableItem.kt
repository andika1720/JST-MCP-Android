package com.example.jstmcpandroid.data

data class TableItem(
    val category: String,
    var values: Array<String> = arrayOf("", "", "", "")
) {
    constructor(category: String, value1: String, value2: String, value3: String, value4: String) :
            this(category, arrayOf(value1, value2, value3, value4))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TableItem

        if (category != other.category) return false
        if (!values.contentEquals(other.values)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = category.hashCode()
        result = 31 * result + values.contentHashCode()
        return result
    }
}