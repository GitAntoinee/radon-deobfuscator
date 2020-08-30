package com.github.gitantoinee.deobfuscator.radon.visitors.string

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class LightStringEncryptionClassVisitor(inner: ClassVisitor? = null) : ClassVisitor(Opcodes.ASM9, inner) {
    private companion object {
        private const val ENCRYPTED_STRINGS_FIELD_DESCRIPTOR: String = "[Ljava/lang/String;"
    }

    override fun visitMethod(access: Int, name: String?, descriptor: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor {
        val inner = super.visitMethod(access, name, descriptor, signature, exceptions)

        return LightStringEncryptionMethodVisitor(inner)
    }
}
