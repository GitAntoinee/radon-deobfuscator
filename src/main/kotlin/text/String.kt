package com.github.gitantoinee.deobfuscator.radon.text

@Suppress("NOTHING_TO_INLINE")
public inline infix fun String.xor(other: Int): String = map {
    it.toInt() xor other
}.joinToString("") { "${it.toChar()}" }
