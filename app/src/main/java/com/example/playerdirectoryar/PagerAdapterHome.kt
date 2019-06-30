package com.example.playerdirectoryar

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class PagerAdapterHome(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                return HomeFragment()
            }
            1 -> {
                return TableClubsFragment()
            }
        }
        return null
    }

    override fun getPageTitle(position: Int): CharSequence? {

        return if (position == 0) {
            "クラブ"
        } else {
            "順位表"
        }
    }
}