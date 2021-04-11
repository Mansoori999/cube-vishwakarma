package com.vinners.trumanms.core.extensions


import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView


fun Spinner.onItemSelected(itemSelected: (AdapterView<*>?, View?, Int, Long) -> Unit) {

    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            itemSelected(parent, view, position, id)
        }
    }
}

fun EditText.setText(text: String?, defaultText: String?) {
    if (text.isNullOrEmpty())
        this.setText(defaultText)
    else
        this.setText(text)
}

fun TextView.setText(text: String?, defaultText: String?) {
    if (text.isNullOrEmpty())
        this.text = defaultText
    else
        this.text = text
}

fun View.setVisibilityVisible() {
    if (this.visibility != View.VISIBLE)
        this.visibility = View.VISIBLE
}

fun View.setVisibilityGone() {
    if (this.visibility != View.GONE)
        this.visibility = View.GONE
}

fun View.setVisibilityInvisible() {
    if (this.visibility != View.INVISIBLE)
        this.visibility = View.INVISIBLE
}

fun Spinner.selectItemWithText(text: String) {

    if (this.count == 0)
        return

    for (i in 0 until this.adapter.count) {
        val item = this.adapter.getItem(i)

        item ?: continue
        val stringDisplayed = item.toString().toLowerCase().trim()
        val targetString = text.toLowerCase().trim()

        if (stringDisplayed.equals(targetString)) {
            this.setSelection(i)
            return
        }
    }
}



