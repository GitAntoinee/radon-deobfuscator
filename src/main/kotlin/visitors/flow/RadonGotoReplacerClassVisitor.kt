package com.github.gitantoinee.deobfuscator.radon.visitors.flow

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class RadonGotoReplacerClassVisitor(inner: ClassVisitor? = null) : ClassVisitor(Opcodes.ASM9, inner) {
    private val possiblePredicateFields: MutableList<String> = mutableListOf()

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?,
    ): MethodVisitor {
        val inner = super.visitMethod(access, name, descriptor, signature, exceptions)

        return RadonGotoReplacerMethodVisitor(possiblePredicateFields, inner)
    }
}
