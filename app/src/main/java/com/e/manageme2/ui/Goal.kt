package com.e.manageme2.ui

import com.yalantis.beamazingtoday.interfaces.BatModel
class Goal(name: String) : BatModel {
    private var name: String? = name

    private var isChecked = false

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    override fun setChecked(checked: Boolean) {
        isChecked = checked
    }

    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun getText(): String? {
        return getName()
    }
}