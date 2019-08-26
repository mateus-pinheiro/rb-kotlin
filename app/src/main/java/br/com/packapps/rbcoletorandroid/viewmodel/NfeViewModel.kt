package br.com.packapps.rbcoletorandroid.viewmodel

import android.arch.lifecycle.MutableLiveData
import br.com.packapps.rbcoletorandroid.model.response.ResponseNfeCheckout
import br.com.packapps.rbcoletorandroid.repository.RepositoryServer

class NfeViewModel() : AppViewModel() {

    var response : MutableLiveData<ResponseNfeCheckout> = MutableLiveData()

    fun getNfe(keyNf : String, timestamp: String){
        if (keyNf == null || timestamp == null){
            createError()
            return
        }

        RepositoryServer.nfe(this, keyNf, timestamp)
    }

}