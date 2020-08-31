package com.github.gitantoinee.deobfuscator.radon.visitors.string

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class LightStringEncryptionClassVisitor(inner: ClassVisitor? = null) : ClassVisitor(Opcodes.ASM9, inner) {
    private companion object {
        private const val ENCRYPTED_STRINGS_FIELD_DESCRIPTOR: String = "[Ljava/lang/String;"
        private const val ENCRYPTED_STRINGS_FIELD_ACCESS: Int = Opcodes.ACC_PRIVATE and Opcodes.ACC_STATIC
    }

    private val possibleEncryptedStringsFields: MutableMap<String, MutableList<String>> = mutableMapOf()

    override fun visitField(access: Int, name: String, descriptor: String, signature: String?, value: Any?): FieldVisitor {
        if (ENCRYPTED_STRINGS_FIELD_ACCESS == access && ENCRYPTED_STRINGS_FIELD_DESCRIPTOR == descriptor) {
            assert(name !in possibleEncryptedStringsFields) { "Duplicate field" }
            possibleEncryptedStringsFields[name] = mutableListOf()
        }

        return super.visitField(access, name, descriptor, signature, value)
    }

    override fun visitMethod(access: Int, name: String?, descriptor: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor? {
        val inner = super.visitMethod(access, name, descriptor, signature, exceptions)

        return EncryptedFieldReaderMethodVisitor(if (possibleEncryptedStringsFields.isNotEmpty()) LightStringEncryptionMethodVisitor(possibleEncryptedStringsFields, inner) else inner)
    }

    private inner class EncryptedFieldReaderMethodVisitor(inner: MethodVisitor? = null) : MethodVisitor(Opcodes.ASM9, inner) {
        private lateinit var fieldName: String
        private var expectedSize: Int? = null
        private val content: MutableList<String> = mutableListOf()

        override fun visitEnd() {
            if (::fieldName.isInitialized) {
                check(fieldName in possibleEncryptedStringsFields) { "Undefined field" }
                check(possibleEncryptedStringsFields.getValue(fieldName).isEmpty()) { "Duplicate initialization function" }
                check(expectedSize != null && possibleEncryptedStringsFields.size == expectedSize) { "Invalid size" }
                possibleEncryptedStringsFields[fieldName] = content
            }

            super.visitEnd()
        }
    }
}
