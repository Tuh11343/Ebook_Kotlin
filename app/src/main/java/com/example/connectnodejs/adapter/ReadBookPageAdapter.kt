package com.example.connectnodejs.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.connectnodejs.fragment.FragmentLyric
import com.example.connectnodejs.fragment.FragmentAudio


class ReadBookPageAdapter(
    private var activity: AppCompatActivity,
    private var mainViewPage:ViewPager2,
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentAudio()
            1 -> FragmentLyric()

            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    fun getTitle(position: Int): String {
        return when (position) {
            0 -> "Main"
            1 -> "Lyrics"
            else -> "WTF"
        }
    }
}