package br.com.packapps.rbcoletorandroid.viewmodel

import android.arch.lifecycle.MutableLiveData
import br.com.packapps.rbcoletorandroid.model.body.CompanyIdBody
import br.com.packapps.rbcoletorandroid.model.body.PrivilegesBody
import br.com.packapps.rbcoletorandroid.model.response.ResponseCompanyId
import br.com.packapps.rbcoletorandroid.model.response.ResponsePrivileges
import br.com.packapps.rbcoletorandroid.repository.RepositoryServer

class PrivilegesViewModel() : AppViewModel() {

    var response : MutableLiveData<ResponsePrivileges> = MutableLiveData()


    fun getPrivileges(body: PrivilegesBody){
        RepositoryServer.privileges(this, body)
    }

}
