package com.fang.scroll

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * @author fangkw on 2020-10-23
 **/
class MainAdapter(context: Context) : ListAdapter<ItemModel, MainAdapter.MainViewHolder>(DiffCallback()) {

    private var inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    init {
        buildItems()
    }

    private fun buildItems() {
        val items = mutableListOf<ItemModel>()
        repeat(9) {
            items.add(ItemModel(TYPE_ITEM_NORMAL, it))
        }
        items.add(ItemModel(TYPE_ITEM_LOADING))
        submitList(items)
    }

    fun loadTabData(lifecycleCoroutineScope: LifecycleCoroutineScope) {
        lifecycleCoroutineScope.launchWhenResumed {
            val newItems = withContext(Dispatchers.IO) {
                val temp = mutableListOf<ItemModel>()
                temp.addAll(currentList)
                temp.add(ItemModel(TYPE_ITEM_TAB))
                temp.removeAll {
                    it.type == TYPE_ITEM_LOADING
                }
                temp
            }
            delay(50)
            submitList(newItems)
        }
    }

    override fun getItem(position: Int): ItemModel {
        return currentList[position]
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return when(viewType) {
            TYPE_ITEM_NORMAL -> {
                NormalViewHolder(inflater.inflate(R.layout.list_item_normal, parent, false))
            }

            TYPE_ITEM_LOADING -> {
                StubViewHolder(inflater.inflate(R.layout.item_loading, parent, false))
            }

            TYPE_ITEM_TAB -> {
                StubViewHolder(inflater.inflate(R.layout.item_list_tab, parent, false))
            }

            else -> {
                StubViewHolder(inflater.inflate(R.layout.item_empty, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        if (holder is NormalViewHolder) {
            val item = Utils.pickOneColor(position)
            holder.bindView(item)
        }
    }

    open class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bindView(item: String) {

        }
    }

    class NormalViewHolder(view: View) : MainViewHolder(view) {

        var imageView: ImageView = view.findViewById(R.id.iv_image)

        override fun bindView(item: String) {
            super.bindView(item)

            imageView.setBackgroundColor(Color.parseColor(item))
        }
    }

    class StubViewHolder(view: View) : MainAdapter.MainViewHolder(view)

    companion object {
        const val TYPE_ITEM_NORMAL = 0
        const val TYPE_ITEM_LOADING = 1
        const val TYPE_ITEM_TAB = 2
    }
}

class DiffCallback : DiffUtil.ItemCallback<ItemModel>() {
    override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
        return oldItem.type == newItem.type && oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
        return oldItem == newItem
    }
}