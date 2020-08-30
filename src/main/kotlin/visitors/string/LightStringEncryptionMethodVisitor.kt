package com.github.gitantoinee.deobfuscator.radon.visitors.string

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class LightStringEncryptionMethodVisitor(
        /**
         * The fields who may contains the encrypted strings
         */
        private val possibleEncryptedStringsFields: List<String>,

        inner: MethodVisitor? = null
) : MethodVisitor(Opcodes.ASM9, inner) {
    private companion object {
        const val DECRYPTION_METHOD_DESCRIPTOR: String = "(Ljava/lang/Object;I)Ljava/lang/String;"
        const val DECRYPTION_METHOD_ACCESS: Int = Opcodes.ACC_PUBLIC and Opcodes.ACC_STATIC
    }

    private var currentKey: Long? = null
    private var currentEncryptedString: String? = null

    override fun visitLdcInsn(value: Any?) {
        if (currentKey == null && value is Number) {
            currentKey = value.toLong()
        } else {
            check(currentKey == null && currentEncryptedString == null) { "Unexpected ldc instruction" }
            super.visitLdcInsn(value)
        }
    }
}
