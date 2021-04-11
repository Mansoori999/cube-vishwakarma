package com.vinners.trumanms.feature_myjobs.views

import android.content.Context
import android.util.AttributeSet
import android.view.View.OnClickListener
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.google.android.material.button.MaterialButton
import com.vinners.trumanms.feature_myjobs.R
import timber.log.Timber

class MyJobsTabLayout : HorizontalScrollView {

    companion object {

        const val POSITION_MY_OFFERS = 0
        const val POSITION_IN_HAND = 1
        const val POSITION_APPLIED_JOBS = 2
        const val POSITION_SAVED_JOBS = 3
    }

    //Constructors
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var myOffersButton: MaterialButton
    private var inHandButton: MaterialButton
    private var appliedButton: MaterialButton
    private var savedButton: MaterialButton

    private var mCurrentlySelectedPosition = -1
    private var mOnTabClickListener: ((Int) -> Unit)? = null

    private val onTabButtonClickListener = OnClickListener { clickedButton ->

        when (clickedButton.id) {
            R.id.tab_myOffers -> {
                setItemAsSelected(POSITION_MY_OFFERS)
                mOnTabClickListener?.invoke(POSITION_MY_OFFERS)
            }
            R.id.tab_inhand -> {
                setItemAsSelected(POSITION_IN_HAND)
                mOnTabClickListener?.invoke(POSITION_IN_HAND)
            }
            R.id.tab_applied -> {
                setItemAsSelected(POSITION_APPLIED_JOBS)
                mOnTabClickListener?.invoke(POSITION_APPLIED_JOBS)
            }
            R.id.tab_saved -> {
                setItemAsSelected(POSITION_SAVED_JOBS)
                mOnTabClickListener?.invoke(POSITION_SAVED_JOBS)
            }
        }
    }

    init {
        inflate(context, R.layout.tablayout_my_jobs_tab, this)

        isHorizontalScrollBarEnabled = false
        isVerticalScrollBarEnabled = false

        myOffersButton = findViewById(R.id.tab_myOffers)
        inHandButton = findViewById(R.id.tab_inhand)
        appliedButton = findViewById(R.id.tab_applied)
        savedButton = findViewById(R.id.tab_saved)

        this.myOffersButton.setOnClickListener(onTabButtonClickListener)
        this.inHandButton.setOnClickListener(onTabButtonClickListener)
        this.appliedButton.setOnClickListener(onTabButtonClickListener)
        this.savedButton.setOnClickListener(onTabButtonClickListener)

        setItemAsSelected(POSITION_MY_OFFERS)
    }

    fun setOnTabClickListener(listener: (Int) -> Unit) {
        this.mOnTabClickListener = listener
    }

    fun setItemAsSelected(position: Int) {
        Timber.d("Current Pos : $mCurrentlySelectedPosition")
        Timber.d("New Pos received : $position")

        if (mCurrentlySelectedPosition == position) {
            //no-op
            return
        }

        when (position) {
            POSITION_MY_OFFERS -> {
                unselectedButton(appliedButton)
                unselectedButton(savedButton)
                unselectedButton(inHandButton)
                selectedButton(myOffersButton)
            }
            POSITION_IN_HAND -> {
                unselectedButton(appliedButton)
                unselectedButton(savedButton)
                unselectedButton(myOffersButton)
                selectedButton(inHandButton)
            }
            POSITION_APPLIED_JOBS -> {
                unselectedButton(myOffersButton)
                unselectedButton(savedButton)
                unselectedButton(inHandButton)
                selectedButton(appliedButton)
            }
            POSITION_SAVED_JOBS -> {
                unselectedButton(appliedButton)
                unselectedButton(myOffersButton)
                unselectedButton(inHandButton)
                selectedButton(savedButton)
            }
            else -> {
                //no-op
            }
        }

        this.mCurrentlySelectedPosition = position
    }

    private fun selectedButton(button: MaterialButton) {
        ViewCompat.setElevation(button, 8.0f)
        button.backgroundTintList = ContextCompat.getColorStateList(context, R.color.color_primary)
        button.setTextColor(ResourcesCompat.getColor(context.resources, R.color.white, null))
    }

    private fun unselectedButton(button: MaterialButton) {
        ViewCompat.setElevation(button, 12.0f)
        button.backgroundTintList = ContextCompat.getColorStateList(context, R.color.white)
        button.setTextColor(ResourcesCompat.getColor(context.resources, R.color.black, null))
    }
}