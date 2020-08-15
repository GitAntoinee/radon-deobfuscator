package com.github.gitantoinee.deobfuscator.radon

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@Suppress("NOTHING_TO_INLINE")
public inline fun RadonDeobfuscator.deobfuscate(input: ByteArray): ByteArray {
    return ByteArrayOutputStream().also {
        deobfuscate(ByteArrayInputStream(input), it)
    }.toByteArray()
}
