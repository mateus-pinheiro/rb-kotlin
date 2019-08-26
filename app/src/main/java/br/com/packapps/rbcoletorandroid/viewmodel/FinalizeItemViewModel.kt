package br.com.packapps.rbcoletorandroid.viewmodel

import android.arch.lifecycle.MutableLiveData
import br.com.packapps.rbcoletorandroid.model.body.FinalizeBody
import br.com.packapps.rbcoletorandroid.model.body.FinalizeCheckItemBody
import br.com.packapps.rbcoletorandroid.model.response.ResponseFinalize
import br.com.packapps.rbcoletorandroid.model.response.ResponseFinalizeCheck
import br.com.packapps.rbcoletorandroid.repository.RepositoryServer

class FinalizeItemViewModel() : AppViewModel(){

    var response : MutableLiveData<ResponseFinalize> = MutableLiveData()

    fun finalize(body : FinalizeBody){
        RepositoryServer.finalize(this, body)

    }

}
