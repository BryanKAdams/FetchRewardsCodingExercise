package com.bryanadams.fetchnames.model

data class ListItem(val int: Int): PickerData() {
    private val id: Int = int

    override fun getPickerDataId(): Int? {
        return id * 1000000
    }

    override fun getPickerDataParentId(): Int? {
        return null
    }

    override fun getPickerDataImage(): Int? {
        return null
    }

    override fun getPickerDataExpandImage(): Int? {
        return null

    }

    override fun getFullName(): String? {
        return "List: $id"
    }

    override fun getPickerDataParentAndSelfText(): String? {
        return "$id"
    }

    override fun getPickerDataText(): String? {
        return getFullName()
    }

    override fun getPickerDataHighlightColor(): Int? {
        return null
    }

    override fun getPickerDataParentText(): String {
        return "0"
    }

    override fun getPickerDataIndentLevel(): Int {
        return 0
    }

}