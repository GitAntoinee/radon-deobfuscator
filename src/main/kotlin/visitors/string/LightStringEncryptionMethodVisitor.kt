package com.github.gitantoinee.deobfuscator.radon.visitors.string

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class LightStringEncryptionMethodVisitor(
        /**
         * The fields who may contains the encrypted strings
         */
        private val possibleEncryptedStringsFields: Map<String, List<String>>,

        inner: MethodVisitor? = null
) : MethodVisitor(Opcodes.ASM9, inner) {
    private companion object {
        const val DECRYPTION_METHOD_DESCRIPTOR: String = "(Ljava/lang/Object;I)Ljava/lang/String;"

        private fun decrypt(encrypted: String, methodOwner: String, methodName: String, key: Long): String {
            val key2 = methodName.hashCode().toLong()
            val key3 = methodOwner.hashCode().toLong()

            return buildString(encrypted.length) {
                encrypted.forEach { encryptedCharacter ->
                    val sum = key3 xor key2 xor key xor encryptedCharacter.toLong()
                    append(sum.toChar())
                }
            }
        }
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

    override fun visitInsn(opcode: Int) {
        if (Opcodes.ICONST_M1 <= opcode && Opcodes.ICONST_5 >= opcode && State.LOADING_ENCRYPTED_STRING_INDEX == state) {
            val index = opcode - 3
            currentEncryptedString = "** encrypted string at $index **" // TODO : Get the encrypted string
            state = State.PATCHING
        } else {
            check(State.LOADING_KEY == state) { "Unexpected instruction" }
            super.visitInsn(opcode)
        }
    }

    override fun visitFieldInsn(opcode: Int, owner: String, name: String, descriptor: String) {
        if (Opcodes.GETSTATIC == opcode && name in possibleEncryptedStringsFields) {
            check(state == State.LOADING_ENCRYPTED_STRING_FIELD) { "Unexpected field instruction" }
            state = State.LOADING_ENCRYPTED_STRING_INDEX
        } else {
            check(state == State.LOADING_KEY) { "Unexpected field instruction" }
            super.visitFieldInsn(opcode, owner, name, descriptor)
        }
    }

    override fun visitLdcInsn(value: Any?) {
        check(state == State.LOADING_KEY) { "Unexpected ldc instruction" }

        if (currentKey == null && value is Number) {
            currentKey = value.toLong()
            state = State.LOADING_ENCRYPTED_STRING_FIELD
        } else {
            super.visitLdcInsn(value)
        }
    }

    override fun visitMethodInsn(opcode: Int, owner: String, name: String, descriptor: String, isInterface: Boolean) {
        if (Opcodes.INVOKESTATIC == opcode && DECRYPTION_METHOD_DESCRIPTOR == descriptor && !isInterface && State.PATCHING == state) {
            super.visitLdcInsn(decrypt(currentEncryptedString!!, owner, name, currentKey!!))
            state = State.LOADING_KEY
        } else {
            check(State.LOADING_KEY == state) { "Unexpected method instruction" }
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
        }
    }
}
