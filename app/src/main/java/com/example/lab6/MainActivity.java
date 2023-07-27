package com.example.lab6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.lab6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.lesson1.setOnClickListener(v -> {
            startActivity(new Intent(this, LessonOneActivity.class));
        });

        binding.lesson2.setOnClickListener(v -> {
            startActivity(new Intent(this, LessonTwoActivity.class));
        });
    }
}