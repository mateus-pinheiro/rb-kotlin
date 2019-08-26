package br.com.packapps.rbcoletorandroid.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.packapps.rbcoletorandroid.ui.fragment.ProgressFragment

open class BaseActivity : AppCompatActivity() {

    var progress : ProgressFragment? = null

    fun showProgress(show: Boolean, message: String? = null) {
        if (show){
            if (progress == null)
                progress = ProgressFragment.newInstance(message)

            progress!!.show(supportFragmentManager, "PROGRESS")

        }else{
            if (progress != null)
                progress!!.dismiss()
        }
    }
}
