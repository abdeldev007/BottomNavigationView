package com.abdev.app.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.abdev.app.indicatorbottomnavview.ItemsClickListener
import com.abdev.app.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ItemsClickListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavView.setListener(this)
        binding.bottomNavView.setMenuItemsText("menu 1", "menu 2", "menu 3", "menu 4")
        binding.bottomNavView.setMenuIcons(
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
        )
    }

    override fun onItemClicked(view: View, itemId: Int) {


    }
}