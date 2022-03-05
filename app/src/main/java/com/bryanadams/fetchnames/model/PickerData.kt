package com.bryanadams.fetchnames.model

abstract class PickerData {

    private var visibility: Boolean = false
    private var pickerDataSubText: String? = "Qty: 0"
    private var subTextHighlightColor: Int? = null

    abstract fun getPickerDataId(): Int?
    abstract fun getPickerDataParentId(): Int?
    abstract fun getPickerDataImage(): Int?
    abstract fun getPickerDataExpandImage(): Int?
    abstract fun getFullName(): String?
    abstract fun getPickerDataParentAndSelfText(): String?
    abstract fun getPickerDataText(): String?
    abstract fun getPickerDataHighlightColor(): Int?
    fun getPickerDataSubTextHighlightColor() = subTextHighlightColor
    abstract fun getPickerDataParentText(): String?
    fun getPickerDataSubText() = pickerDataSubText
    fun setPickerDataSubText(text: String) {
        pickerDataSubText = text
    }
    fun setPickerDataSubTextHighlightColor(color: Int?) {
        subTextHighlightColor = color
    }

    abstract fun getPickerDataIndentLevel(): Int

    fun setPickerDataVisibility(visibility: Boolean) {
        this.visibility = visibility
    }

    fun getPickerDataVisibility(): Boolean {
        if (getPickerDataIndentLevel() == 0) {
            visibility = true
            return visibility
        }
        return visibility
    }
}

