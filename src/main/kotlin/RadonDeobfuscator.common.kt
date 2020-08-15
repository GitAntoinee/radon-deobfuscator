package com.github.gitantoinee.deobfuscator.radon

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * Deobfuscate the input class [input] and return the deobfuscated class
 */
@Suppress("NOTHING_TO_INLINE")
public inline fun RadonDeobfuscator.deobfuscate(input: ByteArray): ByteArray {
    return ByteArrayOutputStream().also {
        deobfuscate(ByteArrayInputStream(input), it)
    }.toByteArray()
}
