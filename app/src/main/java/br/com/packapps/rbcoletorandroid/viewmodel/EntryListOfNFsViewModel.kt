package br.com.packapps.rbcoletorandroid.viewmodel

import android.arch.lifecycle.MutableLiveData
import br.com.packapps.rbcoletorandroid.model.body.EntryListOfNFsBody
import br.com.packapps.rbcoletorandroid.model.response.ResponseEntryListNFs
import br.com.packapps.rbcoletorandroid.repository.RepositoryServer

class EntryListOfNFsViewModel() : AppViewModel(){


    var response : MutableLiveData<ResponseEntryListNFs> = MutableLiveData()

    fun getEntryListOfNFs(body: EntryListOfNFsBody?){
        if (body == null){
            createError()
            return
        }

        RepositoryServer.entryListOfNFs(this, body)
    }


}