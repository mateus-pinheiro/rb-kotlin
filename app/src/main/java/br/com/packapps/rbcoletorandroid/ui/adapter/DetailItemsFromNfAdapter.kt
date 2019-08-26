package br.com.packapps.rbcoletorandroid.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.model.response.ResponseEntryDetail
import br.com.packapps.rbcoletorandroid.ui.EntryDetailNFActivity

class DetailItemsFromNfAdapter(var context: EntryDetailNFActivity) : RecyclerView.Adapter<DetailItemsFromNfAdapter.MyViewHolder>() {

    var listItem: MutableList<ResponseEntryDetail.InvolvedBatches> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_batch, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var involvedBatches = listItem.get(position)
        holder.tvQuantityOnNF.text = involvedBatches.quantity.toString()
        holder.tvPresentation.text = involvedBatches.batchObject!!.presentation
        holder.tvProductName.text = involvedBatches.batchObject!!.product!!.name
        holder.tvBatch.text = involvedBatches.batchObject!!.batch

    }

    fun updateListItens(list: MutableList<ResponseEntryDetail.InvolvedBatches>) {
        this.listItem = list
        notifyDataSetChanged()
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvQuantityOnNF = view.findViewById<TextView>(R.id.tvQuantity)
        var tvPresentation = view.findViewById<TextView>(R.id.presentation_tv)
        var tvProductName = view.findViewById<TextView>(R.id.product_name_tv)
        var tvBatch = view.findViewById<TextView>(R.id.batch_tv)

//        var tvQuantityScanned = view.findViewById<TextView>(R.id.tvQuantityScanned)
    }


}
