package br.com.packapps.rbcoletorandroid.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.model.TypeItem
import br.com.packapps.rbcoletorandroid.model.WrapperListExit
import br.com.packapps.rbcoletorandroid.ui.ExitMovimentationActivity

class ExitMovimentationAdapter(var context: ExitMovimentationActivity) : RecyclerView.Adapter<ExitMovimentationAdapter.MyViewHolder>() {

//    var listItem: MutableList<ResponseNfeCheckout.Product> = mutableListOf()
    var listItem: MutableList<WrapperListExit> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view : View? = null
        if (viewType == TypeItem.FROM_BUNDLE.code)
            view = LayoutInflater.from(context).inflate(R.layout.item_movimentation, parent, false)
        else if (viewType == TypeItem.FROM_SCAN_DATAMATRIX.code)
            view = LayoutInflater.from(context).inflate(R.layout.item_product_to_scan_datamatrix_exit, parent, false)
        else if (viewType == TypeItem.FROM_SCAN_AGGREGATION.code)
            view = LayoutInflater.from(context).inflate(R.layout.item_product_to_scan_exit, parent, false)


        return MyViewHolder(view, viewType)
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var wrapper = listItem.get(position)

        when(wrapper.type.code){
            TypeItem.FROM_BUNDLE.code -> {
                var products = wrapper.itemFromBundle
                var index : Int
                var productQuantity: String

                if (products?.quant!!.contains(",")){
                    index = products?.quant!!.indexOf(",")
                    productQuantity = products?.quant!!.substring(0, index)
                } else {
                    productQuantity = products?.quant!!
                }

                holder.tvQuantityOnNF?.text = productQuantity
                holder.tvEanComercial?.text = products?.eANcomercial
                holder.tvName?.text = products?.name
                holder.tvBatchCode?.text = products?.batchCode

            }
            TypeItem.FROM_SCAN_DATAMATRIX.code -> {
                var itemScannedDataMatrix = listItem.get(position).itemFromScanIUM

                holder.tvBarcode?.text = itemScannedDataMatrix?.IUM
                holder.tvAnvisaDate?.text = itemScannedDataMatrix?.anvisa
                holder.tvBatch?.text = itemScannedDataMatrix?.batchCode
                holder.tvExpirationDate?.text = itemScannedDataMatrix?.expiration
                holder.tvSerialNumber?.text = itemScannedDataMatrix?.serial

            }
            TypeItem.FROM_SCAN_AGGREGATION.code -> {
                var itemScanned = listItem.get(position).itemFromScanAggregation

                holder.tvBarcode?.text = itemScanned

            }
        }
    }

    fun updateListItens(list: MutableList<WrapperListExit>) {
        this.listItem = list
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return listItem.get(position).type.code
    }


    class MyViewHolder(view: View?, viewType: Int) : RecyclerView.ViewHolder(view) {
        //FROM bundle
        var tvQuantityOnNF = view?.findViewById<TextView>(R.id.tvQuantity)
        var tvEanComercial = view?.findViewById<TextView>(R.id.tvEanComercial)
        var tvName = view?.findViewById<TextView>(R.id.tvName)
        var tvBatchCode = view?.findViewById<TextView>(R.id.tvBatchCode)

        // AGGREGAÇÃO (CÓDIGO DE BARRAS)
        // ITEM, IUM (DATA MATRIX)
        var tvBarcode = view?.findViewById<TextView>(R.id.tvBarcode)

        var tvSerialNumber = view?.findViewById<TextView>(R.id.serial_tv)
        var tvBatch = view?.findViewById<TextView>(R.id.lote_tv)
        var tvAnvisaDate = view?.findViewById<TextView>(R.id.anvisa_tv)
        var tvExpirationDate = view?.findViewById<TextView>(R.id.expiration_date_tv)


    }


}