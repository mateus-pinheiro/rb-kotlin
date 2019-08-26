package br.com.packapps.rbcoletorandroid.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.FragmentTransaction
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.core.SingletonApp
import br.com.packapps.rbcoletorandroid.model.body.CompanyIdBody
import br.com.packapps.rbcoletorandroid.model.body.LoginBody
import br.com.packapps.rbcoletorandroid.model.body.PrivilegesBody
import br.com.packapps.rbcoletorandroid.model.response.*
import br.com.packapps.rbcoletorandroid.ui.adapter.CompaniesIdLoginAdapter
import br.com.packapps.rbcoletorandroid.ui.fragment.FailFragment
import br.com.packapps.rbcoletorandroid.ui.fragment.SearchCompanyIdLoginFragment
import br.com.packapps.rbcoletorandroid.util.Const
import br.com.packapps.rbcoletorandroid.viewmodel.CompanyIdViewModel
import br.com.packapps.rbcoletorandroid.viewmodel.LoginViewModel
import br.com.packapps.rbcoletorandroid.viewmodel.PrivilegesViewModel

import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_login.*
import kotlinx.android.synthetic.main.fragment_company_list.*

class LoginActivity : BaseActivity(), FailFragment.OnFailFragmentListener,
    CompaniesIdLoginAdapter.OnCompaniesIdFromLoginListener {


    val TAG = LoginActivity::class.java.simpleName

    lateinit var loginViewModel: LoginViewModel
    lateinit var companyIdViewModel: CompanyIdViewModel
    lateinit var privilegesViewModel: PrivilegesViewModel
    lateinit var username: String
    lateinit var password: String
    var privilegeMock: String = "AGGREGATION"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        observerLogin()



        login_btn.setOnClickListener { view ->


            showProgress(true, "Logando")


            username = username_et.text.toString()
            password = password_et.text.toString()

            loginViewModel.login(LoginBody(username, password))


        }

        username_et.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                username_et.error = if (s!!.length >= 3) null else "Mínimo 3 caracteres, por favor."
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })

        password_et.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                password_et.error = if (s!!.length >= 3) null else "Mínimo 3 caracteres, por favor."
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })

    }


    private fun observerLogin() {

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        //Success
        loginViewModel.responseLogin.observe(this,
            Observer<ResponseLogin> { response ->
                if (response != null) {
                    Log.i("TAG", response.toString())

                    //### Get companyId
                    if (response.jwt != "") {
                        observerCompanyId(CompanyIdBody(username, privilegeMock))
                        //                        val activityOptions =
                        //                            ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity, ivLogo, "logo")
                        //                        val intent = Intent(this@LoginActivity, MenuActivity::class.java)
                        //                        startActivity(intent, activityOptions.toBundle())

                    }
                }
            })

        //ErrorApp
        loginViewModel.error.observe(this,
            Observer<Error> { error ->
                showProgress(false)
                if (error != null) {
                    Log.e("TAG", "Failure:$error")
                    if (error.code == 401) {
                        val dialog = FailFragment.newInstance(
                            "Login Falhou",
                            "Por favor verifique seus dados e tente novamente."
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager, "")
                    } else {
                        val dialog = FailFragment.newInstance(
                            "Login Falhou",
                            "Tentativa falha, tente novamente! Se persistir tente reiniciar o aparelho. ${error.messages}"
                        )
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager, "")
                    }
                }
            })
    }

    private fun observerCompanyId(body: CompanyIdBody?) {
        companyIdViewModel = ViewModelProviders.of(this@LoginActivity).get(CompanyIdViewModel::class.java)
        //Success
        companyIdViewModel.response.observe(this@LoginActivity,
            Observer<ResponseCompanyId> {
                Log.i(TAG, it.toString())
                showProgress(false, "Logando")
                openFragmentSearchView("")
//                observerPrivileges(it)
            })
        //Failure
        companyIdViewModel.error.observe(this@LoginActivity,
            Observer<Error> {
                Log.e(TAG, it.toString())
                showProgress(false)

                val dialog = FailFragment.newInstance(
                    "Busca de empresa falhou",
                    "Por favor verifique seus dados e tente novamente. ${it!!.messages}"
                )
                dialog.isCancelable = false
                dialog.show(supportFragmentManager, "")
            })

        //call
        companyIdViewModel.getCompanyIdFromLogin(body)
    }


    private fun observerPrivileges(companyId: ResponseCompaniesByUser.Company) {
        //### Get Privileges
        privilegesViewModel = ViewModelProviders.of(this@LoginActivity).get(PrivilegesViewModel::class.java)
        //Success
        privilegesViewModel.response.observe(this@LoginActivity, Observer {
            Log.i(TAG, it.toString())
            showProgress(false)
            if (it != null) {
                //Call next View
                var bundle = Bundle()
                bundle.putParcelable(Const.PRIVILEGES, it)

                ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity, ivLogo, "logo")
                var intent = Intent(this@LoginActivity, MenuActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
                finish()
            }
            //### Call Main Activity
//            startActivity(Intent(this@LoginActivity, MenuActivity::class.java))

        })
        //Error
        privilegesViewModel.error.observe(this@LoginActivity, Observer {
            Log.e(TAG, it.toString())
            showProgress(false)
            val dialog = FailFragment.newInstance(
                "Busca de privilégios falhou",
                "Por favor verifique seus dados e tente novamente.${it!!.messages}"
            )
            dialog.isCancelable = false
            dialog.show(supportFragmentManager, "")
        })

        //call
        if (companyId == null) {
            //TODO show Error
            return
        }
        if (companyId == null) {
            //TODO show error
            return
        }

        privilegesViewModel.getPrivileges(
            PrivilegesBody(
                username,
                companyId.id!!.toLong()
            )
        )
    }


    private var transaction: FragmentTransaction? = null
    private fun openFragmentSearchView(text: CharSequence) {
        var fragemtSearchCompany = SearchCompanyIdLoginFragment.newInstance(text.toString())
        transaction = supportFragmentManager.beginTransaction()
        transaction?.replace(R.id.container_search_exit, fragemtSearchCompany)
        transaction?.addToBackStack(null)
        transaction?.commit()

//        etSearchCompany.visibility = View.GONE
    }

    override fun onBackPressed() {
        if (transaction != null) {
            if (supportFragmentManager.fragments.size > 0) {
                supportFragmentManager.popBackStackImmediate()

                return
            }
        }

        super.onBackPressed()
    }


    override fun onActionFromFailFragment() {
        //Do nothing here this case
    }


    override fun onClickCompanyItemFromSearchFragment(companyName: ResponseCompaniesByUser.Company) {
        Log.i(TAG, "click item adapter companies: ${companyName.toString()}")

//        Toast.makeText(this, "company: $companyName", Toast.LENGTH_LONG).show()
        SingletonApp.getInstance().companyId = companyName.id!!.toLong()
        onBackPressed()
        showProgress(true)
        observerPrivileges(companyName)
    }

    override fun onErrorGetListCompanies(error: Error?) {
        Toast.makeText(this, "error: " + error?.toString(), Toast.LENGTH_LONG).show()
        onBackPressed()
    }

}

