package com.github.gitantoinee.deobfuscator.radon.visitors.string

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class LightStringEncryptionMethodVisitor(inner: MethodVisitor? = null) : MethodVisitor(Opcodes.ASM9, inner) {
    private companion object {
        const val DECRYPTION_METHOD_DESCRIPTOR: String = "(Ljava/lang/Object;I)Ljava/lang/String;"
    }
}
