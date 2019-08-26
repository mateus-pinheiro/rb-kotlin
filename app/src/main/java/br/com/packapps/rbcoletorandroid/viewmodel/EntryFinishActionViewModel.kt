package br.com.packapps.rbcoletorandroid.viewmodel

import android.arch.lifecycle.MutableLiveData
import br.com.packapps.rbcoletorandroid.model.body.EntryFinishActionBody
import br.com.packapps.rbcoletorandroid.model.response.ResponseEntryFinishAction
import br.com.packapps.rbcoletorandroid.repository.RepositoryServer

class EntryFinishActionViewModel() : AppViewModel(){

    var response : MutableLiveData<ResponseEntryFinishAction> = MutableLiveData()

    fun sendEntryFinishAction(body: EntryFinishActionBody?){
        if (body == null){
            createError()
            return
        }

        RepositoryServer.sendEntryFinishAction(this, body)
    }


}