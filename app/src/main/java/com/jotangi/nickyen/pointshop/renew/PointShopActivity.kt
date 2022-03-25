package com.jotangi.nickyen.pointshop.renew

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.jotangi.nickyen.R
import com.jotangi.nickyen.databinding.ActivityPointShopBinding

class PointShopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPointShopBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPointShopBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.typeRed)
    }

    fun nextPage(requireView: View, id: Int, bundle: Bundle?) {
        Navigation.findNavController(requireView)
            .navigate(id)
    }

    fun back(requireView: View) {
        Navigation.findNavController(requireView).popBackStack()
    }
}