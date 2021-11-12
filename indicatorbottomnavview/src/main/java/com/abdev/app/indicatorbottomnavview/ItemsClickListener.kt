package com.abdev.app.indicatorbottomnavview

import android.view.View

interface ItemsClickListener {
    fun onItemClicked(view : View, itemId:Int)
}