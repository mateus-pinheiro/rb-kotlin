package br.com.packapps.rbcoletorandroid.ui

import android.content.Intent
import android.os.Bundle
import br.com.packapps.rbcoletorandroid.R

import kotlinx.android.synthetic.main.activity_aggregation_options.*
import kotlinx.android.synthetic.main.content_aggregation_options.*

class AggregationOptionsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aggregation_options)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btNewAggregation.setOnClickListener {
            startActivity(Intent(this, NewAggregationSencondActivity::class.java))
        }

        btEditAggregation.setOnClickListener {
            startActivity(Intent(this, EditAggregationActivity::class.java))
        }
    }

}
