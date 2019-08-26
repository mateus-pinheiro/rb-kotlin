package br.com.packapps.rbcoletorandroid.core

import java.lang.RuntimeException

class SingletonApp{

    var jwt : String? = null
    var authToken : String? = null
    var companyId: Long? = null
    var privileges: MutableList<String>? = mutableListOf()
    var companyType: String? = null

    var paramBody: String? = null


    companion object {
        private var INSTANCE: SingletonApp? = null

        fun getInstance() : SingletonApp{
            if (INSTANCE == null){
                synchronized(SingletonApp::class.java){
                    if (INSTANCE == null) INSTANCE = SingletonApp()
                }

            }

            return INSTANCE!!
        }
    }


    private constructor(){
        if (INSTANCE != null){
            throw RuntimeException("This class is an singleton. Use getInstance() method :)")
        }
    }
}