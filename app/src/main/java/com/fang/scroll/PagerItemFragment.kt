package com.fang.scroll

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author fangkw on 2020-10-23
 **/
class PagerItemFragment : Fragment() {

    private lateinit var recyclerView : RecyclerView
    private var index : Int = 0
    private var pool : RecyclerView.RecycledViewPool? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rl_list_inner)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.setHasFixedSize(true)
        pool?.let {
            recyclerView.setRecycledViewPool(it)
        }
        recyclerView.tag = "inner_$index"

        lifecycleScope.launchWhenResumed {
            recyclerView.adapter = InnerAdapter(view.context)
        }
    }

    companion object {
        fun getInstance(index : Int, pool : RecyclerView.RecycledViewPool?) : PagerItemFragment {
            val fragment = PagerItemFragment()
            fragment.index = index
            fragment.pool = pool
            return fragment
        }
    }

    class InnerAdapter(context: Context) : RecyclerView.Adapter<MainAdapter.NormalViewHolder>() {
        private var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getItemCount(): Int {
            return 10
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.NormalViewHolder {
            return MainAdapter.NormalViewHolder(inflater.inflate(R.layout.list_item_normal, parent, false))
        }

        override fun onBindViewHolder(holder: MainAdapter.NormalViewHolder, position: Int) {
            val color = Utils.pickOneColor(position)
            holder.imageView.setBackgroundColor(Color.parseColor(color))
        }
    }
}