package com.example.myapplication.util

import java.io.IOException

class APIException(message: String) : IOException(message)
class NoInternetException(message: String) : IOException(message)