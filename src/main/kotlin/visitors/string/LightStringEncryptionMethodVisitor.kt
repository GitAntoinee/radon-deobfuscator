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

    private enum class State {
        /**
         * Waiting for a `ldc` instruction with a number
         */
        LOADING_KEY,

        /**
         * Waiting for the `getstatic` instruction
         */
        LOADING_ENCRYPTED_STRING_FIELD,

        /**
         * Waiting for the index of the encrypted string
         */
        LOADING_ENCRYPTED_STRING_INDEX,

        /**
         * Waiting for the decryption method invocation and replacing it to a ldc instruction with the decrypted string
         */
        PATCHING,
    }

    private var state: State = State.LOADING_KEY

    private var currentKey: Long? = null
    private var currentEncryptedString: String? = null

    override fun visitLdcInsn(value: Any?) {
        check(state == State.LOADING_KEY) { "Unexpected ldc instruction" }

        if (currentKey == null && value is Number) {
            currentKey = value.toLong()
            state = State.LOADING_ENCRYPTED_STRING_FIELD
        } else {
            super.visitLdcInsn(value)
        }
    }
}
