package com.hedvig.api.error.decoder

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.hedvig.api.error.model.ApiErrorResponse
import feign.FeignException
import feign.Response
import feign.codec.ErrorDecoder
import org.slf4j.LoggerFactory
import java.io.IOException

class InternalApiErrorDecoder(private val mapper: ObjectMapper) : ErrorDecoder {
    override fun decode(methodKey: String, response: Response): Exception {
        try {
            return mapper.readValue(response.body().asInputStream(), ApiErrorResponse::class.java)
        } catch (ex: IllegalArgumentException) {
            log.error(String.format("Could not read ApiError: %s", ex.message), ex)
        } catch (ex: JsonParseException) {
            log.error(String.format("Could not read ApiError: %s", ex.message), ex)
        } catch (ex: JsonMappingException) {
            log.error(String.format("Could not read ApiError: %s", ex.message), ex)
        } catch (ex: IOException) {
            log.error(String.format("IO error when decoding error:", ex.message), ex)
        }
        return FeignException.errorStatus(methodKey, response)
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}
