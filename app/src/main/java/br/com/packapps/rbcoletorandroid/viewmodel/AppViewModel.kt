package br.com.packapps.rbcoletorandroid.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.packapps.rbcoletorandroid.model.response.Error

open class AppViewModel() : ViewModel(){

    var error : MutableLiveData<Error> = MutableLiveData()


    protected fun createError() {
        var error = Error(420, mutableListOf("Data to Body cannot be null"))
        this.error.value = error
    }


}