package br.com.packapps.rbcoletorandroid.repository

import android.util.Log
import br.com.packapps.rbcoletorandroid.core.SingletonApp
import br.com.packapps.rbcoletorandroid.model.MyErrorBody
import br.com.packapps.rbcoletorandroid.model.body.*
import br.com.packapps.rbcoletorandroid.model.response.*
import br.com.packapps.rbcoletorandroid.repository.api.ApiService
import br.com.packapps.rbcoletorandroid.viewmodel.*
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


object RepositoryServer {
    val TAG = RepositoryServer.javaClass.simpleName

    fun login(body: LoginBody, viewModel: AppViewModel) {
        viewModel as LoginViewModel
        val retrofit = ApiService.configRetrofit(ApiService.BASE_URL_SANDBOX)
        val service = retrofit.create(ApiService::class.java)
        val call = service.login(body)
        call.enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                try {
                    Log.i(TAG, "onResponse login: ${response.body().toString()}")
                    if (response.code() == 401) {
                        viewModel.error.value = Error(response.code(), mutableListOf("Unauthorized login"))
//                        viewModel.responseLogin.value = null
                        return
                    }

                    val rl = response.body()
                    //Set on singleton
                    SingletonApp.getInstance().jwt = rl!!.jwt
                    SingletonApp.getInstance().authToken = rl!!.authToken

                    viewModel.responseLogin.value = ResponseLogin(rl!!.authToken, rl.login, rl.jwt)
                } catch (e: Exception) {
                    e.printStackTrace()
                    createError(response.errorBody(), viewModel)

                }

            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.e(TAG, "onFailure login: ${t.message}")
                viewModel.error.value = Error(488, mutableListOf(t.message.toString()))
//                viewModel.responseLogin.value = null
            }
        })

    }

    private fun createError(
        responseBody: ResponseBody,
        viewModel: AppViewModel
    ) {
        val errorBody = getErrorBody(responseBody)
        //Show Error critical
        viewModel.error.value = Error(errorBody!!.httpCode, mutableListOf(errorBody.name))
    }

    private fun getErrorBody(responseBody: ResponseBody): MyErrorBody? {
        var reader: BufferedReader? = null
        val sb = StringBuilder()
        try {
            reader = BufferedReader(InputStreamReader(responseBody.byteStream()))
            var line: String
            try {
                reader.forEachLine {
                    if (it != null){
                        sb.append(it)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val finallyError = sb.toString()
        val errorBody = Gson().fromJson<MyErrorBody>(finallyError, MyErrorBody::class.java)

        return errorBody
    }


    fun companyId(viewModel: AppViewModel, body: CompanyIdBody) {
        viewModel as CompanyIdViewModel
        val retrofit = ApiService.configRetrofit(ApiService.BASE_URL_PHARMA)
        val service = retrofit.create(ApiService::class.java)
        val call = service.companyId(body.user, body.privilege, ApiService.createHeader())
        call.enqueue(object : Callback<ResponseCompanyId> {
            override fun onResponse(call: Call<ResponseCompanyId>, response: Response<ResponseCompanyId>) {
                try {
                    Log.i(TAG, "onResponse companyId: ${response.body().toString()}")
                    val rl = response.body()
                    viewModel.response.value = rl

//                    SingletonApp.getInstance().companyId = rl!!.companies!!.get(0)

                } catch (e: Exception) {
                    e.printStackTrace()
                    //Show Error critical
                    createError(response.errorBody(), viewModel)

                }

            }

            override fun onFailure(call: Call<ResponseCompanyId>, t: Throwable) {
                Log.e(TAG, "onFailure companyId: ${t.message}")
                viewModel.error.value = Error(488, mutableListOf(t.message.toString()))
            }
        })


    }

    fun companyType(viewModel: AppViewModel, body: CompanyTypeBody) {
        viewModel as CompanyTypeViewModel
        val retrofit = ApiService.configRetrofit(ApiService.BASE_URL_REPORTS)
        val service = retrofit.create(ApiService::class.java)
        val call = service.companyType(body)
        call.enqueue(object : Callback<ResponseCompanyType> {
            override fun onResponse(call: Call<ResponseCompanyType>, response: Response<ResponseCompanyType>) {
                try {

                    Log.i(TAG, "onResponse companyType: ${response.body().toString()}")
                    val rl = response.body()
                    viewModel.response.value = rl

                } catch (e: Exception) {
                    e.printStackTrace()
                    //Show Error critical
                    createError(response.errorBody(), viewModel)

                }

            }

            override fun onFailure(call: Call<ResponseCompanyType>, t: Throwable) {
                Log.e(TAG, "onFailure companyType: ${t.message}")
                viewModel.error.value = Error(488, mutableListOf(t.message.toString()))
            }
        })


    }

    fun privileges(viewModel: AppViewModel, body: PrivilegesBody) {
        viewModel as PrivilegesViewModel
        val retrofit = ApiService.configRetrofit(ApiService.BASE_URL_PHARMA)
        val service = retrofit.create(ApiService::class.java)
        val call = service.privileges(body.user, body.companyId, ApiService.createHeader())
        call.enqueue(object : Callback<ResponsePrivileges> {
            override fun onResponse(call: Call<ResponsePrivileges>, response: Response<ResponsePrivileges>) {
                try {
                    Log.i(TAG, "onResponse privileges: ${response.body().toString()}")
                    val rl = response.body()
                    viewModel.response.value = rl

                } catch (e: Exception) {
                    e.printStackTrace()
                    //Show Error critical
                    createError(response.errorBody(), viewModel)

                }

            }

            override fun onFailure(call: Call<ResponsePrivileges>, t: Throwable) {
                Log.e(TAG, "onFailure privileges: ${t.message}")
                viewModel.error.value = Error(488, mutableListOf(t.message.toString()))
            }
        })
    }


    fun finalizeCheckItem(viewModel: AppViewModel, body: FinalizeCheckItemBody) {
        viewModel as FinalizeCheckItemViewModel
        val retrofit = ApiService.configRetrofit(ApiService.BASE_URL_REPORTS)
        val service = retrofit.create(ApiService::class.java)
        val call = service.finalizeCheckItem(body, SingletonApp.getInstance().jwt!!)
        call.enqueue(object : Callback<ResponseFinalizeCheck> {
            override fun onResponse(call: Call<ResponseFinalizeCheck>, response: Response<ResponseFinalizeCheck>) {
                try {
                    Log.i(TAG, "onResponse finalizeCheckItem: ${response.body().toString()}")
                    val rl = response.body()
                    viewModel.response.value = rl

                } catch (e: Exception) {
                    e.printStackTrace()
                    //Show Error critical
                    createError(response.errorBody(), viewModel)

                }

            }

            override fun onFailure(call: Call<ResponseFinalizeCheck>, t: Throwable) {
                Log.e(TAG, "onFailure finalizeCheckItem: ${t.message}")
                viewModel.error.value = Error(488, mutableListOf(t.message.toString()))
            }
        })
    }

    fun finalizeCheckNfCount(viewModel: AppViewModel, body: FinalizeCheckItemBody) {
        viewModel as FinalizeCheckItemViewModel
        val retrofit = ApiService.configRetrofit(ApiService.BASE_URL_REPORTS)
        val service = retrofit.create(ApiService::class.java)
        val call = service.finalizeCheckNfCount(body, SingletonApp.getInstance().jwt!!)
        call.enqueue(object : Callback<ResponseExitMovement> {
            override fun onResponse(call: Call<ResponseExitMovement>, response: Response<ResponseExitMovement>) {
                try {
                    Log.i(TAG, "onResponse finalizeCheckNfCount: ${response.body().toString()}")
                    val rl = response.body()
                    viewModel.responseNfCount.value = rl

                } catch (e: Exception) {
                    e.printStackTrace()
                    //Show Error critical
                    createError(response.errorBody(), viewModel)

                }

            }

            override fun onFailure(call: Call<ResponseExitMovement>, t: Throwable) {
                Log.e(TAG, "onFailure finalizeCheckNfCount: ${t.message}")
                viewModel.error.value = Error(488, mutableListOf(t.message.toString()))
            }
        })
    }

    fun finalize(viewModel: AppViewModel, body: FinalizeBody) {
        viewModel as FinalizeItemViewModel
        val retrofit = ApiService.configRetrofit(ApiService.BASE_URL_API)
        val service = retrofit.create(ApiService::class.java)
        val call = service.finalizeItem(body)
        call.enqueue(object : Callback<ResponseFinalize> {
            override fun onResponse(call: Call<ResponseFinalize>, response: Response<ResponseFinalize>) {
                try {
                    Log.i(TAG, "onResponse: ${response.body().toString()}")
                    val rl = response.body()
                    viewModel.response.value = rl

                } catch (e: Exception) {
                    e.printStackTrace()
                    //Show Error critical
                    createError(response.errorBody(), viewModel)

                }

            }

            override fun onFailure(call: Call<ResponseFinalize>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                viewModel.error.value = Error(488, mutableListOf(t.message.toString()))
            }
        })
    }




    //Sprint 2
    fun entryListOfNFs(viewModel: AppViewModel, body: EntryListOfNFsBody) {
        viewModel as EntryListOfNFsViewModel
        val retrofit = ApiService.configRetrofit(ApiService.BASE_URL_REPORTS)
        val service = retrofit.create(ApiService::class.java)
        val call = service.entryGetListOfNF(body)
        call.enqueue(object : Callback<ResponseEntryListNFs> {
            override fun onResponse(
                call: Call<ResponseEntryListNFs>,
                response: Response<ResponseEntryListNFs>
            ) {
                try {

                    Log.i(TAG, "onResponse entryListOfNFs: ${response.body().toString()}")
                    val rl = response.body()
                    viewModel.response.value = rl

                } catch (e: Exception) {
                    e.printStackTrace()
                    //Show Error critical
                    createError(response.errorBody(), viewModel)

                }

            }

            override fun onFailure(call: Call<ResponseEntryListNFs>, t: Throwable) {
                Log.e(TAG, "onFailure entryListOfNFs: ${t.message}")
                viewModel.error.value = Error(488, mutableListOf(t.message.toString()))
            }
        })


    }

    fun entryDetailFromNF(viewModel: AppViewModel, body: EntryDetailBody) {
        viewModel as EntryDetailFromNFViewModel
        val retrofit = ApiService.configRetrofit(ApiService.BASE_URL_REPORTS)
        val service = retrofit.create(ApiService::class.java)
        val call = service.entryGetDetailsFromNF(body)
        call.enqueue(object : Callback<ResponseEntryDetail> {
            override fun onResponse(call: Call<ResponseEntryDetail>, response: Response<ResponseEntryDetail>) {
                try {

                    Log.i(TAG, "onResponse entryDetailFromNF: ${response.body().toString()}")
                    val rl = response.body()
                    viewModel.response.value = rl

                } catch (e: Exception) {
                    e.printStackTrace()
                    //Show Error critical
                    createError(response.errorBody(), viewModel)

                }

            }

            override fun onFailure(call: Call<ResponseEntryDetail>, t: Throwable) {
                Log.e(TAG, "onFailure entryDetailFromNF: ${t.message}")
                viewModel.error.value = Error(488, mutableListOf(t.message.toString()))
            }
        })
    }

    fun iumList(viewModel: AppViewModel, body: IumItemsBody) {
        viewModel as IumListViewModel
        val retrofit = ApiService.configRetrofit(ApiService.BASE_URL_REPORTS)
        val service = retrofit.create(ApiService::class.java)
        val call = service.getIUMItems(body)
        call.enqueue(object : Callback<ResponseIumItems> {
            override fun onResponse(call: Call<ResponseIumItems>, response: Response<ResponseIumItems>) {
                try {

                    Log.i(TAG, "onResponse iumList: ${response.body().toString()}")
                    val rl = response.body()
                    viewModel.response.value = rl

                } catch (e: Exception) {
                    e.printStackTrace()
                    //Show Error critical
                    createError(response.errorBody(), viewModel)
//                    viewModel.error.value = Error(response.code(), mutableListOf("Erro em busca de items. Contate o suporte."))

                }

            }

            override fun onFailure(call: Call<ResponseIumItems>, t: Throwable) {
                Log.e(TAG, "onFailure iumList: ${t.message}")
                viewModel.error.value = Error(488, mutableListOf(t.message.toString()))
            }
        })
    }

    fun aggregationList(viewModel: AppViewModel, body: AggregationItemsBody) {
        viewModel as AggregationListViewModel
        val retrofit = ApiService.configRetrofit(ApiService.BASE_URL_REPORTS)
        val service = retrofit.create(ApiService::class.java)
        val call = service.getAggregationItems(body)
        call.enqueue(object : Callback<ResponseAggregationItems> {
            override fun onResponse(
                call: Call<ResponseAggregationItems>,
                response: Response<ResponseAggregationItems>
            ) {
                try {

                    Log.i(TAG, "onResponse aggregationList: ${response.body().toString()}")
                    val rl = response.body()
                    viewModel.response.value = rl

                } catch (e: Exception) {
                    e.printStackTrace()
                    //Show Error critical
                    createError(response.errorBody(), viewModel)

                }

            }

            override fun onFailure(call: Call<ResponseAggregationItems>, t: Throwable) {
                Log.e(TAG, "onFailure aggregationList: ${t.message}")
                viewModel.error.value = Error(488, mutableListOf(t.message.toString()))
            }
        })
    }

    fun sendEntryFinishAction(viewModel: AppViewModel, body: EntryFinishActionBody) {
        viewModel as EntryFinishActionViewModel
        val retrofit = ApiService.configRetrofit(ApiService.BASE_URL_API)
        val service = retrofit.create(ApiService::class.java)
        val call = service.sendEntryFinishAction(body)
        call.enqueue(object : Callback<ResponseEntryFinishAction> {
            override fun onResponse(
                call: Call<ResponseEntryFinishAction>,
                response: Response<ResponseEntryFinishAction>
            ) {
                try {

                    Log.i(TAG, "onResponse sendCheckoutCheckin: ${response.body().toString()}")
                    val rl = response.body()
                    viewModel.response.value = rl

                } catch (e: Exception) {
                    e.printStackTrace()
                    //Show Error critical
                    createError(response.errorBody(), viewModel)

                }

            }

            override fun onFailure(call: Call<ResponseEntryFinishAction>, t: Throwable) {
                Log.e(TAG, "onFailure sendCheckoutCheckin: ${t.message}")
                viewModel.error.value = Error(488, mutableListOf(t.message.toString()))
            }
        })
    }

    fun nfe(viewModel: AppViewModel, key: String, timestamp: String) {
        viewModel as NfeViewModel
        val retrofit = ApiService.configRetrofit(ApiService.BASE_URL_API)
        val service = retrofit.create(ApiService::class.java)
        val call = service.getNfe(key, timestamp)
        call.enqueue(object : Callback<ResponseNfeCheckout> {
            override fun onResponse(
                call: Call<ResponseNfeCheckout>,
                response: Response<ResponseNfeCheckout>
            ) {
                try {

                    Log.i(TAG, "onResponse nfe: ${response.body().toString()}")
                    val rl = response.body()
                    viewModel.response.value = rl

                } catch (e: Exception) {
                    e.printStackTrace()
                    //Show Error critical
                    createError(response.errorBody(), viewModel)

                }

            }

            override fun onFailure(call: Call<ResponseNfeCheckout>, t: Throwable) {
                Log.e(TAG, "onFailure nfe: ${t.message}")
                viewModel.error.value = Error(488, mutableListOf(t.message.toString()))
            }
        })
    }


    fun companies(viewModel: AppViewModel, company : String, timestamp: String, limit : Int) {
        viewModel as CompanyListViewModel
        val retrofit = ApiService.configRetrofit(ApiService.BASE_URL_SANDBOX)
        val service = retrofit.create(ApiService::class.java)
        val call = service.getCompanies(company, limit, timestamp)
        call.enqueue(object : Callback<MutableList<ResponseCompanies>> {
            override fun onResponse(
                call: Call<MutableList<ResponseCompanies>>,
                response: Response<MutableList<ResponseCompanies>>
            ) {
                try {

                    Log.i(TAG, "onResponse companies: ${response.body().toString()}")
                    val rl = response.body()
                    viewModel.response.value = rl

                } catch (e: Exception) {
                    e.printStackTrace()
                    //Show Error critical
                    createError(response.errorBody(), viewModel)

                }

            }

            override fun onFailure(call: Call<MutableList<ResponseCompanies>>, t: Throwable) {
                Log.e(TAG, "onFailure companies: ${t.message}")
                viewModel.error.value = Error(488, mutableListOf(t.message.toString()))
            }
        })
    }

    fun companiesUser(viewModel: AppViewModel, body: CompaniesUserBody) {
        viewModel as CompanyListViewModel
        val retrofit = ApiService.configRetrofit(ApiService.BASE_URL_REPORTS)
        val service = retrofit.create(ApiService::class.java)
        val call = service.getCompaniesByUser(body)
        call.enqueue(object : Callback<ResponseCompaniesByUser> {
            override fun onResponse(
                call: Call<ResponseCompaniesByUser>,
                response: Response<ResponseCompaniesByUser>
            ) {
                try {

                    Log.i(TAG, "onResponse companies: ${response.body().toString()}")
                    val rl = response.body()
                    viewModel.responseCompaniesByUser.value = rl

                } catch (e: Exception) {
                    e.printStackTrace()
                    //Show Error critical
                    createError(response.errorBody(), viewModel)

                }

            }

            override fun onFailure(call: Call<ResponseCompaniesByUser>, t: Throwable) {
                Log.e(TAG, "onFailure companies: ${t.message}")
                viewModel.error.value = Error(488, mutableListOf(t.message.toString()))
            }
        })
    }

    //Aggregation
    fun aggregationMessage(viewModel: AppViewModel, body : AggregationMessageBody) {
        viewModel as AggregationMessageViewModel
        val retrofit = ApiService.configRetrofit(ApiService.BASE_URL_API)
        val service = retrofit.create(ApiService::class.java)
        val call = service.sendAggregationMessage(body)
        call.enqueue(object : Callback<ResponseEntryFinishAction> {
            override fun onResponse(
                call: Call<ResponseEntryFinishAction>,
                response: Response<ResponseEntryFinishAction>
            ) {
                try {

                    Log.i(TAG, "onResponse aggregationMessage: ${response.body().toString()}")
                    val rl = response.body()
                    viewModel.response.value = rl

                } catch (e: Exception) {
                    e.printStackTrace()
                    //Show Error critical
                    createError(response.errorBody(), viewModel)

                }

            }

            override fun onFailure(call: Call<ResponseEntryFinishAction>, t: Throwable) {
                Log.e(TAG, "onFailure aggregationMessage: ${t.message}")
                viewModel.error.value = Error(488, mutableListOf(t.message.toString()))
            }
        })
    }


}