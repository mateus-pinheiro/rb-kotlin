package br.com.packapps.rbcoletorandroid.viewmodel

import android.arch.lifecycle.MutableLiveData
import br.com.packapps.rbcoletorandroid.model.body.AggregationItemsBody
import br.com.packapps.rbcoletorandroid.model.response.ResponseAggregationItems
import br.com.packapps.rbcoletorandroid.repository.RepositoryServer

class AggregationListViewModel : AppViewModel() {


    var response : MutableLiveData<ResponseAggregationItems> = MutableLiveData()

    fun getAggregationList(body: AggregationItemsBody?){
        if (body == null){
            createError()
            return
        }

        RepositoryServer.aggregationList(this, body)
    }


}