package com.github.gitantoinee.deobfuscator.radon.visitors.number

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class RadonArithmeticClassVisitor(inner: ClassVisitor? = null) : ClassVisitor(Opcodes.ASM9, inner) {
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?,
    ): MethodVisitor {
        val inner = super.visitMethod(access, name, descriptor, signature, exceptions)

        return RadonArithmeticMethodVisitor(inner)
    }
}
