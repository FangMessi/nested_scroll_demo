package com.fang.scroll

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author fangkw on 2020-10-23
 **/
class ItemFragment : Fragment() {

    private lateinit var recyclerView : RecyclerView
    private var index : Int = 0
    private var pool : RecyclerView.RecycledViewPool? = null
    private var init : Boolean = false

    override fun onResume() {
        super.onResume()
        if (recyclerView.adapter == null) {
            view?.postDelayed({
                context?.let {
                    recyclerView.adapter = InnerAdapter(it)
                }
            }, 300)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        init = view == null
        return view?: inflater.inflate(R.layout.fragment_main, container, false)
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
    }

    companion object {
        fun getInstance(index : Int, pool : RecyclerView.RecycledViewPool?) : ItemFragment {
            val fragment = ItemFragment()
            fragment.index = index
            fragment.pool = pool
            return fragment
        }
    }

    class InnerAdapter(context: Context) : RecyclerView.Adapter<MainAdapter.NormalViewHolder>() {
        private var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getItemCount(): Int {
            return 18
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.NormalViewHolder {
            return MainAdapter.NormalViewHolder(inflater.inflate(R.layout.list_item_normal, parent, false))
        }

        override fun onBindViewHolder(holder: MainAdapter.NormalViewHolder, position: Int) {
            holder.imageView.setBackgroundResource(android.R.color.holo_red_dark)
        }

    }
}