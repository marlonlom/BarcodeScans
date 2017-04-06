package com.github.marlonlom.demos.barcodescans

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import java.util.ArrayList

/**
 * Viewpager adapter class

 * @author marlonlom
 */
internal class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val mFragments: ArrayList<Fragment>

    init {
        mFragments = ArrayList<Fragment>(2)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if (position >= 0 && position < 2) {
            return "Dummy,Camera".split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[position]
        }
        return null
    }

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    fun addFragment(fragment: Fragment) {
        mFragments.add(fragment)
    }
}
