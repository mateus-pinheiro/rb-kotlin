package br.com.packapps.rbcoletorandroid.viewmodel

import android.arch.lifecycle.MutableLiveData
import br.com.packapps.rbcoletorandroid.model.body.FinalizeCheckItemBody
import br.com.packapps.rbcoletorandroid.model.response.ResponseExitMovement
import br.com.packapps.rbcoletorandroid.model.response.ResponseFinalizeCheck
import br.com.packapps.rbcoletorandroid.repository.RepositoryServer

class FinalizeCheckItemViewModel() : AppViewModel() {

    var response: MutableLiveData<ResponseFinalizeCheck> = MutableLiveData()
    var responseNfCount: MutableLiveData<ResponseExitMovement> = MutableLiveData()

    fun check(body: FinalizeCheckItemBody) {
        RepositoryServer.finalizeCheckItem(this, body)

    }

    fun checkNfCount(body: FinalizeCheckItemBody) {
        RepositoryServer.finalizeCheckNfCount(this, body)
    }

}
