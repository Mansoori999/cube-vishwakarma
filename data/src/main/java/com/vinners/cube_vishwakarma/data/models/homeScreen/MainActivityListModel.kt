package com.vinners.cube_vishwakarma.data.models.homeScreen

class MainActivityListModel(title: String?) {
    private var title: String
    init {
        this.title = title!!
    }

    fun getTitle(): String? {
        return title
    }
    fun setTitle(name: String?) {
        title = name!!
    }
}