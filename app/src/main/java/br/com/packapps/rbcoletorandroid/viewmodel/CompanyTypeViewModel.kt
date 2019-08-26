package br.com.packapps.rbcoletorandroid.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.support.v7.app.AppCompatActivity
import br.com.packapps.rbcoletorandroid.model.body.CompanyIdBody
import br.com.packapps.rbcoletorandroid.model.body.CompanyTypeBody
import br.com.packapps.rbcoletorandroid.model.response.ResponseCompanyId
import br.com.packapps.rbcoletorandroid.model.response.ResponseCompanyType
import br.com.packapps.rbcoletorandroid.repository.RepositoryServer

class CompanyTypeViewModel : AppViewModel() {


    var response: MutableLiveData<ResponseCompanyType> = MutableLiveData()

    fun getCompanyTypeFrom(body: CompanyTypeBody?) {
        if (body == null) {
            createError()
            return
        }

        RepositoryServer.companyType(this, body)
    }

}