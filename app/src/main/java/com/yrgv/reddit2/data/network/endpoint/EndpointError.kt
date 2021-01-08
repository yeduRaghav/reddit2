package com.yrgv.reddit2.data.network.endpoint

/**
 * Defines localized error responses and their associated data.
 */
sealed class EndpointError(open val message: String?) {

    data class UnhandledError(val originalException: Throwable?, override val message: String?) : EndpointError(message)

    sealed class ClientError(override val message: String?) : EndpointError(message) {
        data class BadRequest(override val message: String?) : ClientError(message)
        data class Unauthorised(override val message: String?) : ClientError(message)
        data class Forbidden(override val message: String?) : ClientError(message)
        data class NotFound(override val message: String?) : ClientError(message)
        data class Timeout(override val message: String?) : ClientError(message)
        data class IAmATeapot(override val message: String?) : ClientError(message)
    }

    sealed class ServerError(override val message: String?) : EndpointError(message) {
        data class InternalServerError(override val message: String?) : ServerError(message)
        data class ServiceUnavailable(override val message: String?) : ServerError(message)
    }

}