package com.spinthedrum.presentation.ui

import android.animation.AnimatorListenerAdapter


interface WheelViewInterface {
    fun rotate(animatorListener: AnimatorListenerAdapter)
    fun getSelectedColor(): Int
    fun getSelectedSectorIndex(): Int
    fun setWheelSize(size: Int)
}