package com.jotangi.nickyen.pointshop.renew

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jotangi.nickyen.R
import com.jotangi.nickyen.databinding.ActivityPointshopPayResultBinding

class PointShopPayResultActivity:AppCompatActivity() {
    private lateinit var binding:ActivityPointshopPayResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPointshopPayResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.typeRed)
    }
}