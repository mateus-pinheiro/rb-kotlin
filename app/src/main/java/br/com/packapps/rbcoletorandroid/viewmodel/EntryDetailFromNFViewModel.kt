package br.com.packapps.rbcoletorandroid.viewmodel

import android.arch.lifecycle.MutableLiveData
import br.com.packapps.rbcoletorandroid.model.body.EntryDetailBody
import br.com.packapps.rbcoletorandroid.model.response.ResponseEntryDetail
import br.com.packapps.rbcoletorandroid.repository.RepositoryServer

class EntryDetailFromNFViewModel() : AppViewModel() {


    var response : MutableLiveData<ResponseEntryDetail> = MutableLiveData()

    fun getEntryDetailFromNF(body: EntryDetailBody?){
        if (body == null){
            createError()
            return
        }

        RepositoryServer.entryDetailFromNF(this, body)
    }


}