package com.github.gitantoinee.deobfuscator.radon.visitors

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class RadonLocalVariableRenamerMethodVisitor(
    /**
     * If the method is static
     */
    private val isStatic: Boolean,
    inner: MethodVisitor? = null,
) : MethodVisitor(Opcodes.ASM9, inner) {
    override fun visitLocalVariable(
        name: String,
        descriptor: String,
        signature: String?,
        start: Label?,
        end: Label?,
        index: Int,
    ) {
        val newName = if (!isStatic && index == 0) "this" else "var$index"

        super.visitLocalVariable(newName, descriptor, signature, start, end, index)
    }
}
