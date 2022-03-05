package com.bryanadams.fetchnames.model

class Name: PickerData() {
    val name: String? = null
    val listId: Int? = null
    val id: Int? = null

    override fun getPickerDataId(): Int? {
        return id
    }

    override fun getPickerDataParentId(): Int? {
        return listId?.times(1000000)
    }

    override fun getPickerDataImage(): Int? {
        return null
    }

    override fun getPickerDataExpandImage(): Int? {
        return null

    }

    override fun getFullName(): String? {
        return name
    }

    override fun getPickerDataParentAndSelfText(): String? {
        return null
    }

    override fun getPickerDataText(): String? {
        return getFullName()
    }

    override fun getPickerDataHighlightColor(): Int? {
        return null
    }

    override fun getPickerDataParentText(): String? {
        return "$name (${listId})"
    }

    override fun getPickerDataIndentLevel(): Int {
        return 1
    }

}