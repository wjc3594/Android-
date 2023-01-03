package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class DemoCollectionAdapter(fragment:Fragment):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment=DemoObjectFragment()
        fragment.arguments= Bundle().apply {
            putInt(ARG_OBJECT,position)
        }
        return fragment
    }

}