package br.com.packapps.rbcoletorandroid.viewmodel

import android.arch.lifecycle.MutableLiveData
import br.com.packapps.rbcoletorandroid.model.body.CompanyIdBody
import br.com.packapps.rbcoletorandroid.model.response.ResponseCompanyId
import br.com.packapps.rbcoletorandroid.repository.RepositoryServer

class CompanyIdViewModel() : AppViewModel(){


    var response : MutableLiveData<ResponseCompanyId> = MutableLiveData()

    fun getCompanyIdFromLogin(body: CompanyIdBody?){
        if (body == null){
            createError()
            return
        }

        RepositoryServer.companyId(this, body)
    }


}