package com.traxis.metroqr

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform