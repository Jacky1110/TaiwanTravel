package com.jotangi.nickyen.argame.JiaoBanShan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.jotangi.nickyen.databinding.ActivityQrcodeBinding;
import com.jotangi.nickyen.databinding.ActivityQuestionnaireBinding;
import com.jotangi.nickyen.databinding.ActivityQuintupleBinding;

public class QuintupleActivity extends AppCompatActivity {

    ActivityQuintupleBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuintupleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}