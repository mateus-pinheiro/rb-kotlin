package br.com.packapps.rbcoletorandroid.ui

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.core.SingletonApp
import br.com.packapps.rbcoletorandroid.model.ItemScanned
import br.com.packapps.rbcoletorandroid.model.MenuItem
import br.com.packapps.rbcoletorandroid.model.SupplierMenu
import br.com.packapps.rbcoletorandroid.model.response.ResponsePrivileges
import br.com.packapps.rbcoletorandroid.ui.adapter.MenuItemsAdapter
import br.com.packapps.rbcoletorandroid.ui.adapter.ScanItemsAdapter
import br.com.packapps.rbcoletorandroid.util.Const
import kotlinx.android.synthetic.main.activity_finalize_scan_items.*
import kotlinx.android.synthetic.main.content_finalize_scan_items.*
import kotlinx.android.synthetic.main.content_menu.*

class MenuActivity : AppCompatActivity() {

    lateinit var adapter: MenuItemsAdapter
    var responsePrivileges: ResponsePrivileges? = null
    var privilegesList: MutableList<MenuItem> = mutableListOf()
    var adapterList: List<MenuItem>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
//        setSupportActionBar(toolbar)

        val bundle = intent.extras
        if (bundle != null) {
            responsePrivileges = bundle.getParcelable(Const.PRIVILEGES)
            SingletonApp.getInstance().privileges = responsePrivileges!!.permissions!!.toMutableList()
        }

        if (SingletonApp.getInstance().privileges != null) {
            SingletonApp.getInstance().privileges?.forEach {
                if (it == "AGGREGATION")
                    privilegesList.add(SupplierMenu.aggregationItem)
                if (it == "CHANGE_ITEM_STATE")
                    privilegesList.add(SupplierMenu.finalizeItem)
                if (it == "CHECKOUT")
                    privilegesList.add(SupplierMenu.checkoutItem)
                if (it == "CHECKIN")
                    privilegesList.add(SupplierMenu.entryItem)
            }
        }


        adapterList = privilegesList

        rvItemsMenu.layoutManager = GridLayoutManager(this, 2)
        adapter = MenuItemsAdapter(this, adapterList!!)
        rvItemsMenu.adapter = adapter

    }


    fun onButtonSearchBarcode(view: View) {
        startActivity(Intent(this, SearchBarcodeTraceableActivity::class.java))
    }

    override fun onBackPressed() {

        finishAffinity()
        finish()

        super.onBackPressed()
    }

}