package br.com.packapps.rbcoletorandroid.viewmodel

import android.arch.lifecycle.MutableLiveData
import br.com.packapps.rbcoletorandroid.model.body.IumItemsBody
import br.com.packapps.rbcoletorandroid.model.response.ResponseIumItems
import br.com.packapps.rbcoletorandroid.repository.RepositoryServer

class IumListViewModel() : AppViewModel() {


    var response : MutableLiveData<ResponseIumItems> = MutableLiveData()

    fun getEntryDetailIum(body: IumItemsBody?){
        if (body == null){
            createError()
            return
        }

        RepositoryServer.iumList(this, body)
    }


}