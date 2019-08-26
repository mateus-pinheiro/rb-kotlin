package br.com.packapps.rbcoletorandroid.viewmodel

import android.arch.lifecycle.MutableLiveData
import br.com.packapps.rbcoletorandroid.model.body.LoginBody
import br.com.packapps.rbcoletorandroid.model.response.ResponseLogin
import br.com.packapps.rbcoletorandroid.repository.RepositoryServer
import java.lang.Error

class LoginViewModel() : AppViewModel(){

    var responseLogin : MutableLiveData<ResponseLogin> = MutableLiveData()


    fun login(loginBody: LoginBody){
        RepositoryServer.login(loginBody, this)
    }


}