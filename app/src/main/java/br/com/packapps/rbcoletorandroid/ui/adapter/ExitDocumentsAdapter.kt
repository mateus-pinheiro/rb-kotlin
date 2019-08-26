package br.com.packapps.rbcoletorandroid.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.model.body.EntryFinishActionBody
import br.com.packapps.rbcoletorandroid.model.response.Documents

class ExitDocumentsAdapter() : RecyclerView.Adapter<ExitDocumentsAdapter.MyViewHoleder>() {
    val TAG = ExitDocumentsAdapter::class.java.simpleName
    var listDocuments = mutableListOf<EntryFinishActionBody.Doc>()
    var listener: OnDocumentAdpterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHoleder {
        var mView = LayoutInflater.from(parent.context).inflate(R.layout.item_exit_document, parent, false)

        return MyViewHoleder(mView)
    }

    override fun getItemCount(): Int {
        return listDocuments.size
    }

    override fun onBindViewHolder(holder: MyViewHoleder, position: Int) {
        var document = listDocuments.get(position)
        if (document.key != null)
            holder.tvDocument.text = document.key
        if (document.id != null)
            holder.tvDocument.text = document.id

        holder.tvTypeDocument.text = document.type

        //click ic delete
        holder.ibDeleteItem.setOnClickListener {
            listener?.onAdapterDocumentClickIcDeleteItem(position)
        }

        holder.itemView.setOnClickListener {
            //TODO Dilas how to do when click on item
            Log.i(TAG, "click item positin: ${position}")
        }
    }


    class MyViewHoleder(view: View) : RecyclerView.ViewHolder(view) {
        var tvDocument = view.findViewById<TextView>(R.id.tvDocument)
        var tvTypeDocument = view.findViewById<TextView>(R.id.tvTypeDocument)
        var ibDeleteItem = view.findViewById<ImageButton>(R.id.ibDeleteItem)

    }


    fun onAttach(context: Context) {
        if (context is OnDocumentAdpterListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnDocumentAdpterListener")
        }
    }

    fun updateListDocuements(list: MutableList<EntryFinishActionBody.Doc>) {
        this.listDocuments = list

        notifyDataSetChanged()
    }


    //Listener
    interface OnDocumentAdpterListener {
        fun onAdapterDocumentClickIcDeleteItem(position: Int)
    }

}
