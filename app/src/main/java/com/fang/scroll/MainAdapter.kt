package com.fang.scroll

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

/**
 * @author fangkw on 2020-10-23
 **/
class MainAdapter(context: Context, recyclerView: RecyclerView) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private val homeRecyclerView = recyclerView
    private var inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var tabLinearLayout = TabLinearLayout(context, homeRecyclerView)

    override fun getItemCount(): Int {
        return ITEM_COUNT
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == ITEM_COUNT - 1) {
            TYPE_ITEM_TAB
        } else {
            TYPE_ITEM_NORMAL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return if (viewType == TYPE_ITEM_NORMAL) {
            NormalViewHolder(inflater.inflate(R.layout.list_item_normal, parent, false))
        } else {
            TabViewHolder(inflater.inflate(R.layout.item_list_tab, parent, false), parent, tabLinearLayout)
        }
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = Utils.pickOneColor(position)
        holder.bindView(item)
    }

    open class MainViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        open fun bindView(item : String) {

        }
    }

    class NormalViewHolder(view : View) : MainViewHolder(view) {

        var imageView : ImageView = view.findViewById(R.id.iv_image)

        override fun bindView(item: String) {
            super.bindView(item)

            imageView.setBackgroundColor(Color.parseColor(item))
        }
    }

    class TabViewHolder(view: View, parent: ViewGroup, tabLinearLayout: TabLinearLayout) : MainAdapter.MainViewHolder(view) {
        private val _parent = parent
        private val _linearLayout = tabLinearLayout
        private val frameLayout = view.findViewById<FrameLayout>(R.id.fl_container)
        private var ns = false

        override fun bindView(item: String) {
            super.bindView(item)
            if (ns) {
                return
            }
            ns = true
            val lp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, _parent.height)
            frameLayout.addView(_linearLayout, lp)
        }
    }

    companion object {
        const val ITEM_COUNT = 10
        const val TYPE_ITEM_NORMAL = 0
        const val TYPE_ITEM_TAB = 1
    }
}