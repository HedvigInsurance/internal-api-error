package com.hedvig.api.error.model

class ApiErrorResponse(
  val errors: List<ApiError>?
): Exception() {
  constructor(
    apiError: ApiError
  ): this(listOf(apiError))
}
