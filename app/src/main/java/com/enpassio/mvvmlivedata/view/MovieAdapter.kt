package com.enpassio.mvvmlivedata.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.enpassio.mvvmlivedata.R
import kotlinx.android.synthetic.main.list_item.view.*

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.Holder>() {

    var mList: List<String> = arrayListOf()
    private lateinit var mOnClick: (position: Int) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.item_textView.text = mList[position]
        holder.itemView.setOnClickListener { mOnClick(position) }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    infix fun setItemClickMethod(onClick: (position: Int) -> Unit) {
        this.mOnClick = onClick
    }

    fun updateList(list: List<String>) {
        mList = list
    }

    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!)
}
