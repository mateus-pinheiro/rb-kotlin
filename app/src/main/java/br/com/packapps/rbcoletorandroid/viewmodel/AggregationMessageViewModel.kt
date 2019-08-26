package br.com.packapps.rbcoletorandroid.viewmodel

import android.arch.lifecycle.MutableLiveData
import br.com.packapps.rbcoletorandroid.model.body.AggregationMessageBody
import br.com.packapps.rbcoletorandroid.model.response.ResponseEntryFinishAction
import br.com.packapps.rbcoletorandroid.repository.RepositoryServer

class AggregationMessageViewModel() : AppViewModel() {

    var response : MutableLiveData<ResponseEntryFinishAction> = MutableLiveData()

    fun sendAggregation(body: AggregationMessageBody){
        if (body == null){
            createError()
            return
        }

        RepositoryServer.aggregationMessage(this, body)
    }
}