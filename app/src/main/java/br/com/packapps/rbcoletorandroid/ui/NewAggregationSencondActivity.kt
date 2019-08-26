package br.com.packapps.rbcoletorandroid.ui

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.util.Const

import kotlinx.android.synthetic.main.activity_new_aggregation_sencond.*
import kotlinx.android.synthetic.main.content_new_aggregation_sencond.*

class NewAggregationSencondActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_aggregation_sencond)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btBox.setOnClickListener {
            var intent = Intent(this, NewAggregationScanAcitivty::class.java)
            intent.putExtra(Const.TYPE_AGRREGATION, TypesAggregations.BOX.label)
            startActivity(intent)
        }
        btPalete.setOnClickListener {
            var intent = Intent(this, NewAggregationScanAcitivty::class.java)
            intent.putExtra(Const.TYPE_AGRREGATION, TypesAggregations.PALETE.label)
            startActivity(intent)
        }
        btPackge.setOnClickListener {
            var intent = Intent(this, NewAggregationScanAcitivty::class.java)
            intent.putExtra(Const.TYPE_AGRREGATION, TypesAggregations.PACKAGE.label)
            startActivity(intent)
        }
    }



    enum class TypesAggregations(val label : Int){
        BOX(100),
        PALETE(200),
        PACKAGE(300)
    }

}
