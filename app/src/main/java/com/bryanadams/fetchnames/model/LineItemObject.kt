package com.bryanadams.fetchnames.model

abstract class LineItemObject {
    abstract fun isTitle(): Boolean?
    abstract fun getText(): String?
}