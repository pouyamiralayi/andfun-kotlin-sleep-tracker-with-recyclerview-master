package com.pouyamiralayi.android.datatracker.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pouyamiralayi.android.datatracker.network.ApiState
import com.pouyamiralayi.android.datatracker.network.StrapiApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModel() {
    val showSplash = MutableLiveData<Boolean>()

    private val apiJob = Job()
    private val coroutineScope = CoroutineScope(apiJob + Dispatchers.Main)

    val navigateToCustomers = MutableLiveData<Boolean>()

    val fetchError = MutableLiveData<String>()

    val state = MutableLiveData<ApiState>()

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val customer_name = MutableLiveData<String>()
    val jwt = MutableLiveData<String>()

    fun onNavigateToCustomersCompleted() {
        navigateToCustomers.value = false
        showSplash.value = true
        state.value  = ApiState.LOADING
    }

    fun testAuth(token: String?){
        if(token == null){
            showSplash.value = false
            state.value = ApiState.DONE
            return
        }
        coroutineScope.launch {
//            Log.e("JWT", "Bearer $token")
            val authDeffered = StrapiApi.retrofitService.auth("Bearer $token")
            try{
                val res = authDeffered.await()
                res.userName.toString()
                jwt.postValue(token)
                username.postValue(res.userName)
                customer_name.postValue(res.email)
                navigateToCustomers.postValue(true)
            }
            catch (e: Exception){
                /*notify*/
                Log.e("Auth",e.message ?: "")
                showSplash.postValue(false)
                state.postValue(ApiState.DONE)
            }
        }
    }

    fun signIn() {
        if (username.value.equals("")) {
            fetchError.postValue("نام کاربری را وارد نمایید")
            return
        } else if (password.value.equals("")) {
            fetchError.postValue("رمز عبور را وارد نمایید")
            return
        } else {
            coroutineScope.launch {
                val loginDeferred = StrapiApi.retrofitService.login(username.value
                        ?: "", password.value ?: "")
                try {
                    state.value = ApiState.LOADING
                    val res = loginDeferred.await()
                    state.value = ApiState.DONE
                    customer_name.value = res.user.email
                    jwt.value = res.jwt
                    navigateToCustomers.value = true
                } catch (t: Throwable) {
                    Log.i("login", t.message)
                    state.value = ApiState.ERROR
                    fetchError.value = "خطا!"
                }
            }
        }
    }

    init {
        showSplash.value = true
        state.value = ApiState.LOADING
        jwt.value = ""
        navigateToCustomers.value = false
        customer_name.value = ""
        username.value = ""
        password.value = ""
    }

    override fun onCleared() {
        super.onCleared()
        apiJob.cancel()
    }
}