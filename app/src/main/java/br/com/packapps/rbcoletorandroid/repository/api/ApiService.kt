package br.com.packapps.rbcoletorandroid.repository.api

import br.com.packapps.rbcoletorandroid.core.SingletonApp
import br.com.packapps.rbcoletorandroid.model.body.*
import br.com.packapps.rbcoletorandroid.model.response.*
import okhttp3.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.security.cert.X509Certificate
import javax.net.ssl.*
import java.util.*
import java.util.concurrent.TimeUnit


interface ApiService {

    companion object {
        val BASE_URL_SANDBOX: String
            get() = "https://dapisandbox.rastreabilidadebrasil.com.br/"

        val BASE_URL_PHARMA: String
            get() = "https://minossandbox.rastreabilidadebrasil.com.br/"

        val BASE_URL_REPORTS: String
            get() = "https://reportssandbox.rastreabilidadebrasil.com.br/"

        val BASE_URL_BARCODE_1: String
            get() = "https://qlt-reports.rastreabilidadebrasil.com.br/"

        val BASE_URL_API: String
            get() = "https://apisandbox.rastreabilidadebrasil.com.br/"


        fun configRetrofit(baseUrl: String): Retrofit {
//            val okHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient

            val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
                )
                .build()

            val client = OkHttpClient.Builder()
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .connectionSpecs(Collections.singletonList(spec))
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder().baseUrl(baseUrl).client(client).addConverterFactory(GsonConverterFactory.create())
                .build()

        }

        fun createHeader(): HashMap<String, String> {
            var map = HashMap<String, String>()

            //para host "BASE_URL_SANDBOX"
            map.put("Content-Type:", "application/json")
            map.put("Accept-Encoding:", "gzip, deflate, br")
            map.put("origin:", "https://pharmasandbox.rastreabilidadebrasil.com.br")
            if (SingletonApp.getInstance().jwt != null)
                map.put("Authorization:", SingletonApp.getInstance().jwt ?: "")


            //para host "BASE_URL_REPORTS"
//            map.put("Authorization", "ed5454243lsknfdslktrenfds")
//            map.put("Company-id", "24461")
//            map.put("Content-Type" ,"application/json")

            //para host "BASE_URL_PHARMA"
//            map.put("Content-Type" ,"application/json")
//            map.put("Authorization", "ed5454243lsknfdslktrenfds")


            return map
        }


    }


    object UnsafeOkHttpClient {
        // Create a trust manager that does not validate certificate chains
        // Install the all-trusting trust manager
        // Create an ssl socket factory with our all-trusting manager
        val unsafeOkHttpClient: OkHttpClient
            get() {
                try {
                    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                        override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {

                        }

                        override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {

                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()

                        }


                    })
                    val sslContext = SSLContext.getInstance("SSL")
                    sslContext.init(null, trustAllCerts, java.security.SecureRandom())
                    val sslSocketFactory = sslContext.getSocketFactory()

                    val builder = OkHttpClient.Builder()
                    builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                    builder.hostnameVerifier(object : HostnameVerifier {
                        override fun verify(p0: String?, p1: SSLSession?): Boolean {
                            return true
                        }

                    })

                    return builder.build()
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }

            }
    }


    @POST("rest/1/user/login")
    fun login(@Body loginBody: LoginBody): Call<ResponseLogin>

    @GET("companies/user/{username}/permission/{permission}/")
    fun companyId(@Path("username") username: String, @Path("permission") permission: String, @HeaderMap headers: HashMap<String, String>): Call<ResponseCompanyId>

    @GET("permissions/user/{username}/company/{companyId}")
    fun privileges(@Path("username") username: String, @Path("companyId") companyId: Long, @HeaderMap headers: HashMap<String, String>): Call<ResponsePrivileges>

    @POST("rest/1/report")
    fun companyType(
        @Body companyTypeBody: CompanyTypeBody, @Header("Content-Type") contenType: String = "application/json",
        @Header("Accept-Encoding") accept: String = "gzip, deflate, br",
        @Header("origin") origin: String = "https://pharmasandbox.rastreabilidadebrasil.com.br",
        @Header("Authorization") header: String = if (SingletonApp.getInstance().jwt != null) SingletonApp.getInstance().jwt!! else ""
    ): Call<ResponseCompanyType>

    @POST("rest/1/report")
    fun finalizeCheckItem(@Body body: FinalizeCheckItemBody, @Header("Authorization") header: String): Call<ResponseFinalizeCheck>

    @POST("rest/1/report")
    fun finalizeCheckNfCount(@Body body: FinalizeCheckItemBody, @Header("Authorization") header: String): Call<ResponseExitMovement>

    @POST("rest/2/event/change_state/batch")
    fun finalizeItem(
        @Body body: FinalizeBody,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Accept-Encoding") accept: String = "gzip, deflate, br",
        @Header("Company-id") companyId: String? = SingletonApp.getInstance().companyId.toString(),
        @Header("Authorization") header: String? = SingletonApp.getInstance().jwt ?: "",
        @Header("auth-token") authToken: String? = SingletonApp.getInstance().authToken ?: ""
    ): Call<ResponseFinalize>

    //Sprint2
    @POST("rest/1/report")
    fun entryGetListOfNF(
        @Body entryListOfNFsBody: EntryListOfNFsBody,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Accept-Encoding") accept: String = "gzip, deflate, br",
        @Header("origin") origin: String = "https://pharmasandbox.rastreabilidadebrasil.com.br",
        @Header("auth-token") authToken: String? = SingletonApp.getInstance().authToken ?: "",
        @Header("Authorization") header: String? = SingletonApp.getInstance().jwt ?: ""
    ): Call<ResponseEntryListNFs>


    @POST("rest/1/report")
    fun entryGetDetailsFromNF(
        @Body entryDetailBody: EntryDetailBody,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Accept-Encoding") accept: String = "gzip, deflate, br",
        @Header("origin") origin: String = "https://pharmasandbox.rastreabilidadebrasil.com.br",
        @Header("auth-token") authToken: String? = SingletonApp.getInstance().authToken ?: "",
        @Header("Authorization") header: String? = SingletonApp.getInstance().jwt ?: ""
    ): Call<ResponseEntryDetail>


    @POST("rest/1/report")
    fun getIUMItems(
        @Body iumItemsBody: IumItemsBody,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Accept-Encoding") accept: String = "gzip, deflate, br",
        @Header("origin") origin: String = "https://pharmasandbox.rastreabilidadebrasil.com.br",
        @Header("Authorization") header: String? = SingletonApp.getInstance().jwt ?: ""
    ): Call<ResponseIumItems>


    @POST("rest/1/report")
    fun getAggregationItems(
        @Body aggregationItemsBody: AggregationItemsBody,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Accept-Encoding") accept: String = "gzip, deflate, br",
        @Header("origin") origin: String = "https://pharmasandbox.rastreabilidadebrasil.com.br",
        @Header("Authorization") header: String? = SingletonApp.getInstance().jwt ?: ""
    ): Call<ResponseAggregationItems>

    @POST("rest/2/event/change_state/batch")
    fun sendEntryFinishAction(
        @Body entryFinishActionBody: EntryFinishActionBody,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Accept-Encoding") accept: String = "gzip, deflate, br",
        @Header("origin") origin: String = "https://pharmasandbox.rastreabilidadebrasil.com.br",
        @Header("Company-id") companyId: String? = SingletonApp.getInstance().companyId.toString(),
        @Header("Authorization") header: String? = SingletonApp.getInstance().jwt ?: "",
        @Header("auth-token") authToken: String? = SingletonApp.getInstance().authToken ?: ""
    ): Call<ResponseEntryFinishAction>

    //Sprint3
    //example: https://apisandbox.rastreabilidadebrasil.com.br/rest/2/nf/fetchInfo/35161061230314000760550010000000131316145415?_=1547748194138
    @GET("/rest/2/nf/fetchInfo/{key}")
    fun getNfe(
        @Path("key") nfeKey : String,
        @Query("_") timestamp: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Accept") accept: String = "*/*",
        @Header("origin") origin: String = "https://pharmasandbox.rastreabilidadebrasil.com.br",
        @Header("Authorization") header: String? = SingletonApp.getInstance().jwt ?: ""
    ) : Call<ResponseNfeCheckout>


    //example: https://dapisandbox.rastreabilidadebrasil.com.br/rest/1/company/search/all/libbs?limit=100?_=1547749321051
    @GET("/rest/1/company/search/all/{companyString}")
    fun getCompanies(
        @Path("companyString") companyString: String,
        @Query("limit") limit : Int,
        @Query("_") timestamp: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Accept") accept: String = "*/*",
        @Header("origin") origin: String = "https://pharmasandbox.rastreabilidadebrasil.com.br",
        @Header("Authorization") header: String? = SingletonApp.getInstance().jwt ?: ""
    ) : Call<MutableList<ResponseCompanies>>

    @POST("rest/1/report")
    fun getCompaniesByUser(
        @Body companiesUserBody: CompaniesUserBody,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Accept-Encoding") accept: String = "gzip, deflate, br",
        @Header("origin") origin: String = "https://pharmasandbox.rastreabilidadebrasil.com.br",
        @Header("Authorization") header: String? = SingletonApp.getInstance().jwt ?: ""
    ): Call<ResponseCompaniesByUser>


    //Aggregation
    @POST("rest/2/event/aggregate")
    fun sendAggregationMessage(
        @Body entryFinishActionBody: AggregationMessageBody,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Accept-Encoding") accept: String = "gzip, deflate, br",
        @Header("origin") origin: String = "https://pharmasandbox.rastreabilidadebrasil.com.br",
        @Header("Company-id") companyId: String? = SingletonApp.getInstance().companyId.toString(),
        @Header("Authorization") header: String? = SingletonApp.getInstance().jwt ?: "",
        @Header("auth-token") authToken: String? = SingletonApp.getInstance().authToken ?: ""
    ): Call<ResponseEntryFinishAction>

}