package com.yrgv.reddit2.data.network.endpoint

import junit.framework.TestCase
import org.junit.Assert

class EndpointErrorKtTest : TestCase() {

    fun `test getEndpointError StatusCode_BADREQUEST returns BadRequest`() {
        val message = "ABC"
        val error = getEndpointError(StatusCode.BAD_REQUEST, message)
        Assert.assertTrue(error is EndpointError.ClientError.BadRequest)
        Assert.assertTrue(error.message?.equals(message) == true)
    }

    fun `test getEndpointError StatusCode_UNAUTHORISED returns Unauthorised`() {
        val message = "ABC"
        val error = getEndpointError(StatusCode.UNAUTHORISED, message)
        Assert.assertTrue(error is EndpointError.ClientError.Unauthorised)
        Assert.assertTrue(error.message?.equals(message) == true)
    }

    fun `test getEndpointError StatusCode_FORBIDDEN returns Forbidden`() {
        val message = "ABC"
        val error = getEndpointError(StatusCode.FORBIDDEN, message)
        Assert.assertTrue(error is EndpointError.ClientError.Forbidden)
        Assert.assertTrue(error.message?.equals(message) == true)
    }

    fun `test getEndpointError StatusCode_NOT_FOUND returns NotFound`() {
        val message = "ABC"
        val error = getEndpointError(StatusCode.NOT_FOUND, message)
        Assert.assertTrue(error is EndpointError.ClientError.NotFound)
        Assert.assertTrue(error.message?.equals(message) == true)
    }

    fun `test getEndpointError StatusCode_TIMEOUT returns Timeout`() {
        val message = "ABC"
        val error = getEndpointError(StatusCode.TIMEOUT, message)
        Assert.assertTrue(error is EndpointError.ClientError.Timeout)
        Assert.assertTrue(error.message?.equals(message) == true)
    }

    fun `test getEndpointError StatusCode_I_AM_A_TEAPOT returns IAmATeapot`() {
        val message = "ABC"
        val error = getEndpointError(StatusCode.I_AM_A_TEAPOT, message)
        Assert.assertTrue(error is EndpointError.ClientError.IAmATeapot)
        Assert.assertTrue(error.message?.equals(message) == true)
    }

    fun `test getEndpointError StatusCode_INTERNAL_SERVER_ERROR returns Unauthorised`() {
        val message = "ABC"
        val error = getEndpointError(StatusCode.INTERNAL_SERVER_ERROR, message)
        Assert.assertTrue(error is EndpointError.ServerError.InternalServerError)
        Assert.assertTrue(error.message?.equals(message) == true)
    }

    fun `test getEndpointError StatusCode_SERVICE_UNAVAILABLE returns ServiceUnavailable`() {
        val message = "ABC"
        val error = getEndpointError(StatusCode.SERVICE_UNAVAILABLE, message)
        Assert.assertTrue(error is EndpointError.ServerError.ServiceUnavailable)
        Assert.assertTrue(error.message?.equals(message) == true)
    }

    fun `test getEndpointError undefined StatusCode returns UnhandledError`() {
        val message = "ABC"
        val error1 = getEndpointError(444, message)
        Assert.assertTrue(error1 is EndpointError.UnhandledError && error1.responseCode == 444)
        Assert.assertTrue(error1.message?.equals(message) == true)

        val error2 = getEndpointError(200, null)
        Assert.assertTrue(error2 is EndpointError.UnhandledError&& error2.responseCode == 200)
        Assert.assertTrue(error2.message == null)

    }

}