package com.bryanadams.fetchnames.model

data class ListItem(val int: Int): LineItemObject() {
    private val id: Int = int
    override fun isTitle(): Boolean {
        return true
    }

    override fun getText(): String? {
        return "List: $id"
    }

}