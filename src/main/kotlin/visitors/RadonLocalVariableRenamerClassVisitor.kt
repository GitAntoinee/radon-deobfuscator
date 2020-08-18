package com.github.gitantoinee.deobfuscator.radon.visitors

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class RadonLocalVariableRenamerClassVisitor(inner: ClassVisitor? = null) : ClassVisitor(Opcodes.ASM9, inner) {
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?,
    ): MethodVisitor {
        val inner = super.visitMethod(access, name, descriptor, signature, exceptions)
        val isStatic = access or Opcodes.ACC_STATIC == 0

        return RadonLocalVariableRenamerMethodVisitor(isStatic, inner)
    }
}
