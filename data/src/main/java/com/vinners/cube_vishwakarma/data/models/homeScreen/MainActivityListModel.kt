package com.vinners.cube_vishwakarma.data.models.homeScreen

class MainActivityListModel(title: String?,cardcolour:String?) {
    private var title: String
    private var  cardcolour:String
    init {
        this.title = title!!
        this.cardcolour = cardcolour!!
    }

    fun getTitle(): String? {
        return title
    }
    fun setTitle(name: String?) {
        title = name!!
    }

    fun getColor(): String? {
        return cardcolour
    }
    fun setColor(colour: String?) {
        cardcolour = colour!!
    }
}