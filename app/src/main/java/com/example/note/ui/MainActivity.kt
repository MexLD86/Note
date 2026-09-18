package com.example.note.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.note.R
import com.example.note.databinding.ActivityMainBinding
import com.example.note.ui.fragments.NotesListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NotesListFragment())
                .commit()
        }

    }
}