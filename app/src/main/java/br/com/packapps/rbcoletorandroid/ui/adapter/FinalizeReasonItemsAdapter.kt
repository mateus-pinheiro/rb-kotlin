package br.com.packapps.rbcoletorandroid.ui.adapter

import android.app.Activity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.model.FinalizeReasonItem
import kotlinx.android.synthetic.main.item_product_reason.view.*

class FinalizeReasonItemsAdapter(val activity: Activity, private var listItem: List<FinalizeReasonItem>) :
    RecyclerView.Adapter<FinalizeReasonItemsAdapter.MyViewHolder>(),
    View.OnClickListener, View.OnLongClickListener {

    lateinit var listener: OnReasonAdapterListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinalizeReasonItemsAdapter.MyViewHolder {
        //Init listener
        onAtachAdapter()

        //View
        var view = LayoutInflater.from(activity).inflate(R.layout.item_product_reason, parent, false)
        view.setOnClickListener(this)


        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val finalizeReasonItem = listItem[position]
        holder.setData(finalizeReasonItem, position)
    }

    override fun onClick(view: View?) {
        listener.reasonAdapterClick(listItem.get(view!!.reason_tv!!.tag.toString().toInt()))
    }

    override fun onLongClick(p0: View?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                onClick(itemView)
            }
        }

        fun setData(finalizeReasonItem: FinalizeReasonItem?, pos: Int) {
            itemView.reason_tv.tag = pos
            itemView.reason_tv.text = finalizeReasonItem!!.title
            val color = ContextCompat.getColor(activity, finalizeReasonItem.color)
            itemView.reason_item_cl.setBackgroundColor(color)
        }

    }


    fun onAtachAdapter() {
        if (activity is OnReasonAdapterListener) {
            listener = activity
        } else {
            throw RuntimeException(activity.toString() + " must implement OnReasonAdapterListener")
        }
    }

    interface OnReasonAdapterListener {
        fun reasonAdapterClick(position: FinalizeReasonItem)
    }

}