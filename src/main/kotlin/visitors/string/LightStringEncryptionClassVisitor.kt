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

    private val possibleEncryptedStringsFields: MutableList<String> = mutableListOf()

    override fun visitField(access: Int, name: String, descriptor: String, signature: String?, value: Any?): FieldVisitor {
        if (ENCRYPTED_STRINGS_FIELD_ACCESS == access && ENCRYPTED_STRINGS_FIELD_DESCRIPTOR == descriptor) {
            possibleEncryptedStringsFields.add(name)
        }

        return super.visitField(access, name, descriptor, signature, value)
    }

    override fun visitMethod(access: Int, name: String?, descriptor: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor? {
        val inner = super.visitMethod(access, name, descriptor, signature, exceptions)

        return if(possibleEncryptedStringsFields.isNotEmpty()) LightStringEncryptionMethodVisitor(inner) else inner
    }
}
