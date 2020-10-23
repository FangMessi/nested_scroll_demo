package com.fang.scroll

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

/**
 * 提前初始化 TabLinearLayout
 * @author fangkw on 2020-10-23
 **/
@SuppressLint("ViewConstructor")
class TabLinearLayout(context: Context, recyclerView: RecyclerView) : LinearLayout(context) {

    private var homeRecyclerView = recyclerView
    private var viewPager : ViewPager? = null
    val innerRecyclerView : InnerRecyclerView?
        get() {
            return viewPager?.findViewWithTag("inner_${viewPager?.currentItem}")
        }

    init {
        orientation = VERTICAL
        View.inflate(context, R.layout.item_sub, this)
        initView()
    }

    private fun initView() {
        val fragmentManager = (context as AppCompatActivity).supportFragmentManager
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        viewPager = findViewById(R.id.vp_pager)
        tabLayout.addTab(tabLayout.newTab().setText("tab0"))
        tabLayout.addTab(tabLayout.newTab().setText("tab1"))
        tabLayout.addTab(tabLayout.newTab().setText("tab2"))
        tabLayout.addTab(tabLayout.newTab().setText("tab3"))
        // TODO: fangkw 2019-06-02  setupWithViewPager 会导致轻微卡顿感
        tabLayout.setupWithViewPager(viewPager)
        viewPager?.adapter = ViewPagerAdapter(fragmentManager, homeRecyclerView.recycledViewPool)
    }

    class ViewPagerAdapter(fragmentManager: FragmentManager, pool : RecyclerView.RecycledViewPool?) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val recyclerViewPool = pool

        override fun getItem(position: Int): Fragment {
            return PagerItemFragment.getInstance(position, recyclerViewPool)
        }

        override fun getCount(): Int {
            return 4
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return position.toString()
        }
    }
}