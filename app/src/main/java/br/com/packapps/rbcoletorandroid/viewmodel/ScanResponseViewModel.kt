package br.com.packapps.rbcoletorandroid.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class ScanResponseViewModel() : ViewModel(){

    var barcode : MutableLiveData<String> = MutableLiveData()
}
