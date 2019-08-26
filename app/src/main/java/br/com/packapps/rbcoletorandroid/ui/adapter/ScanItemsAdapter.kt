package br.com.packapps.rbcoletorandroid.ui.adapter

import android.app.Activity
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.model.ItemScanned
import br.com.packapps.rbcoletorandroid.ui.FinalizeScanItemsActivity
import br.com.packapps.rbcoletorandroid.ui.adapter.ScanItemsAdapter.MyViewHolder

class ScanItemsAdapter(var activity: Activity) : RecyclerView.Adapter<MyViewHolder>(), View.OnClickListener, View.OnLongClickListener  {

//    var listItem = mutableListOf<ResponseFinalizeCheck>()
    var listItemChecked = mutableListOf<FinalizeScanItemsActivity.WrapperFinalizeItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        var view : View? = null
        if (viewType == ItemScanned.BarcodeType.BARCODE_DATAMATRIX.code) {
            view = LayoutInflater.from(activity).inflate(R.layout.item_product_to_scan_datamatrix, parent, false)
        }
        else if (viewType == ItemScanned.BarcodeType.BARCODE_NORMAL.code) {
            view = LayoutInflater.from(activity).inflate(R.layout.item_product_to_scan, parent, false)
        }

        view!!.setOnClickListener(this)

        return MyViewHolder(view, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return listItemChecked.get(position).type?:0
    }

    override fun getItemCount(): Int {
        return listItemChecked.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var lapper = listItemChecked.get(position)

//        holder.tvBarcode.text = wrapperFinalizeItem.barCode

        try{
            if (!lapper.responseItemCheck!!.justBarcode) {
                if (lapper.type == ItemScanned.BarcodeType.BARCODE_DATAMATRIX.code) {
                    if (lapper.itemNotFound){
                        holder.containerError.visibility = View.VISIBLE
                    }else{
                        holder.containerError.visibility = View.GONE
                    }

                    var expirationMonth = lapper?.dataMatrixObject?.expiration?.substring(0, 2)
                    var expirationYear = lapper?.dataMatrixObject?.expiration?.substring(2, 4)

                    holder.tvBarcode.text = lapper.dataMatrixObject!!.IUM
                    holder.tvBatch.text = lapper?.dataMatrixObject!!.batchCode
                    holder.tvSerialNumber.text = lapper?.dataMatrixObject!!.serial
                    holder.tvExpirationDate.text = expirationMonth.plus("/").plus(expirationYear)
                    holder.tvAnvisaDate.text = lapper?.dataMatrixObject!!.anvisa
//                    holder.tvBatch.text = wrapperFinalizeItem?.data?.itemsById!![0].content!![0].batchObject!!.batch
//                    holder.tvSerialNumber.text = wrapperFinalizeItem?.data?.itemsById!![0].serialNumber
//                    holder.tvProductionDate.text =
//                            wrapperFinalizeItem?.data?.itemsById!![0].content!![0].batchObject!!.productionDate
//                    holder.tvExpirationDate.text =
//                            wrapperFinalizeItem?.data?.itemsById!![0].content!![0].batchObject!!.expirationDate

//                    holder.containerData.visibility = View.VISIBLE
//                    holder.containerProgress.visibility = View.GONE


                } else if (lapper.type == ItemScanned.BarcodeType.BARCODE_NORMAL.code) {

                    holder.tvBarcode.text = lapper.responseItemCheck!!.barCode
                    holder.tvCompanyName.text =
                            lapper?.responseItemCheck!!.data!!.itemsById!![0].content!![0].batchObject!!.product!!.company?.name
                    holder.tvItemName.text =
                            lapper?.responseItemCheck!!.data!!.itemsById!![0].content!![0].batchObject!!.product?.name
                    holder.tvItemQuantity.text = lapper?.responseItemCheck!!.data!!.itemsById!![0].content!![0].quantity.toString() +
                            " Unidades"

                    holder.containerData.visibility = View.VISIBLE
                    holder.containerProgress.visibility = View.GONE
                    holder.containerError.visibility = View.GONE
                    if (lapper.itemNotFound)
                        holder.containerError.visibility = View.VISIBLE

                }
            }
        }catch (e : Exception){
            e.printStackTrace()
            if (lapper.type == ItemScanned.BarcodeType.BARCODE_DATAMATRIX.code) {
                holder.containerError.visibility = View.VISIBLE

                return
            }
            holder.containerData.visibility = View.GONE
            holder.containerProgress.visibility = View.GONE
            holder.containerError.visibility = View.VISIBLE
        }



        //set fields with response of lapperItem request
    }


//    fun updateListItems(list : MutableList<Responselapper>){
//        this.listItem = list
//
//        notifyDataSetChanged()
//    }

    fun updateListItemsChecked(list : MutableList<FinalizeScanItemsActivity.WrapperFinalizeItem>){
        this.listItemChecked = list

        notifyDataSetChanged()
    }

    override fun onClick(p0: View?) {
    }

    override fun onLongClick(p0: View?): Boolean {
        Toast.makeText(activity, "Remove item from list", Toast.LENGTH_SHORT).show()
        return false
    }


    class MyViewHolder(view: View, viewType: Int) : RecyclerView.ViewHolder(view){

        var tvBarcode = view.findViewById<TextView>(R.id.tvBarcode)



        // AGGREGAÇÃO (CÓDIGO DE BARRAS)
        var tvItemName = view.findViewById<TextView>(R.id.serial_name_tv)
        var tvCompanyName = view.findViewById<TextView>(R.id.company_item_tv)
        var tvItemQuantity = view.findViewById<TextView>(R.id.lote_name_tv)

        // ITEM, IUM (DATA MATRIX)
        var tvSerialNumber = view.findViewById<TextView>(R.id.serial_tv)
        var tvBatch = view.findViewById<TextView>(R.id.lote_tv)
        var tvAnvisaDate = view.findViewById<TextView>(R.id.anvisa_tv)
        var tvExpirationDate = view.findViewById<TextView>(R.id.expiration_date_tv)


        //container bottom item
        var containerProgress = view.findViewById<ConstraintLayout>(R.id.containerProgress)
        var containerError = view.findViewById<ConstraintLayout>(R.id.containerError)
        var containerData = view.findViewById<ConstraintLayout>(R.id.containerData)
    }


}
