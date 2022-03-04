package com.bryanadams.fetchnames.model

class Name: LineItemObject() {
    val name: String? = null
    val listId: Int? = null
    val id: Int? = null
    override fun isTitle(): Boolean {
        return false
    }

    override fun getText(): String? {
        return name
    }

}