package exceptions

class BusinessException(message: String = null, cause: Throwable = null) extends RuntimeException(message, cause)
