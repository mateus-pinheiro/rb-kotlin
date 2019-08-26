package br.com.packapps.rbcoletorandroid.ui.adapter

import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.model.MenuItem
import br.com.packapps.rbcoletorandroid.ui.*
import kotlinx.android.synthetic.main.item_menu.view.*

class MenuItemsAdapter(var activity: Activity, private var listItem: List<MenuItem>) :
    RecyclerView.Adapter<MenuItemsAdapter.MyViewHolder>(),
    View.OnClickListener, View.OnLongClickListener {

//    var listItem = mutableListOf<MenuItem>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        var view = LayoutInflater.from(activity).inflate(R.layout.item_menu, parent, false)
        view.setOnClickListener(this)


        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val menuItem = listItem[position]
        holder.setData(menuItem, position)

    }

    override fun onClick(p0: View?) {

//        Toast.makeText(activity, "Open View detail" + p0!!.menu_item_name_tv.text.toString(), Toast.LENGTH_SHORT).show()


        // Ir para tela de scaneamento
        if (p0!!.menu_item_name_tv.text.equals("Finalização")) {
            var intent = Intent(activity, FinalizeScanItemsActivity::class.java)
            activity.startActivity(intent)
        }
        if (p0!!.menu_item_name_tv.text.equals("Entrada")) {
            var intent = Intent(activity, EntryListINFsFromUserActivity::class.java)
            activity.startActivity(intent)
        }
        if (p0!!.menu_item_name_tv.text.equals("Saída")) {
            var intent = Intent(activity, ExitActivity::class.java)
            activity.startActivity(intent)
        }
        if (p0!!.menu_item_name_tv.text.equals("Agregação")) {
            var intent = Intent(activity, AggregationOptionsActivity::class.java)
            activity.startActivity(intent)
        }
//        ActivityCompat.finishAffinity(activity)

    }

    override fun onLongClick(p0: View?): Boolean {
        Toast.makeText(activity, "Remove item from list", Toast.LENGTH_SHORT).show()
        return false
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                onClick(itemView)
            }
        }

        fun setData(menuItem: MenuItem?, pos: Int) {
            itemView.menu_item_name_tv.text = menuItem!!.title
            val color = ContextCompat.getColor(activity, menuItem.color)
            itemView.menu_item_cl.setBackgroundColor(color)

//            itemView.men
        }
    }


}