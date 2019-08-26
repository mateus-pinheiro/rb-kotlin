package br.com.packapps.rbcoletorandroid.viewmodel

import android.arch.lifecycle.MutableLiveData
import br.com.packapps.rbcoletorandroid.model.body.CompaniesUserBody
import br.com.packapps.rbcoletorandroid.model.response.ResponseCompanies
import br.com.packapps.rbcoletorandroid.model.response.ResponseCompaniesByUser
import br.com.packapps.rbcoletorandroid.repository.RepositoryServer

class CompanyListViewModel() : AppViewModel() {

    var response : MutableLiveData<MutableList<ResponseCompanies>> = MutableLiveData()

    var responseCompaniesByUser : MutableLiveData<ResponseCompaniesByUser> = MutableLiveData()

    fun checkCompanies(company: String, timestamp: String, limit: Int){
        if (company == null || timestamp == null || limit == null){
            createError()
            return
        }

        RepositoryServer.companies(this, company, timestamp, limit)
    }

    fun checkCompaniesByUser(body : CompaniesUserBody){
        if (body == null){
            createError()
            return
        }

        RepositoryServer.companiesUser(this, body)
    }

}