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
class MainAdapter(context: Context) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

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
            TabViewHolder(inflater.inflate(R.layout.item_list_tab, parent, false))
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

    class TabViewHolder(view: View) : MainAdapter.MainViewHolder(view)

    companion object {
        const val ITEM_COUNT = 10
        const val TYPE_ITEM_NORMAL = 0
        const val TYPE_ITEM_TAB = 1
    }
}