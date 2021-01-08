package com.yrgv.reddit2.data.network.endpoint

import android.util.Log
import com.yrgv.reddit2.utils.Either
import com.yrgv.reddit2.utils.Either.Companion.value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

/**
 * Parent class for all endpoints.
 * Handles network response and exceptions and return localized response for the app
 */
abstract class BaseEndpoint<R>(val localiseResponse: Either<EndpointError, R>) {

    /**
     * The current call instance
     * */
    private lateinit var currentCall: Call<R>


    /**
     * Returns a new Retrofit Call
     * */
    protected abstract fun getCall(): Call<R>

    /**
     * Invoke to perform the network call.
     *
     * @param cancelOnGoingCall `true`, if there is a call in-progress, attempts to cancel
     * it before the performing the new call. `false`, will not attempt to cancel
     * the previous call in-progress and will perform the new call.
     *
     * @return If network call fails, returns appropriate [EndpointError].
     * If successful, returns Response body <R>
     * */
    suspend fun execute(cancelOnGoingCall: Boolean = true): Either<EndpointError, R> {
        if (cancelOnGoingCall) {
            attemptToCancelCurrentCall()
        }
        currentCall = getCall()
        return attemptNetworkCall()
    }


    private suspend fun attemptNetworkCall(): Either<EndpointError, R> {
        return withContext(Dispatchers.IO) {
            try {
                currentCall.execute().localise()
            } catch (throwable: Throwable) {
                throwable.toLocalError()
            }
        }
    }

    private fun attemptToCancelCurrentCall() {
        if (::currentCall.isInitialized) {
            try {
                currentCall.cancel()
            } catch (exception: IllegalStateException) {
                Log.e("BaseEndpoint", exception.message ?: "exception cancelling currentCall")
            }
        }
    }

    private fun Response<R>.localise(): Either<EndpointError, R> {
        return body()?.let {
            value(it)
        } ?: error(getEndpointError(code(),message()))
    }

}