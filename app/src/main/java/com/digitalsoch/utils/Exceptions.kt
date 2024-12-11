package com.digitalsoch.utils

import java.io.IOException

class Exceptions {
    class ApiException(message: String?) : IOException(message)
    class Exception(message: String?) : java.lang.Exception(message)
}