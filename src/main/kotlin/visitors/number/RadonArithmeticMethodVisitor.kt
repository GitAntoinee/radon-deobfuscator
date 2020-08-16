package com.github.gitantoinee.deobfuscator.radon.visitors.number

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class RadonArithmeticMethodVisitor(inner: MethodVisitor? = null) : MethodVisitor(Opcodes.ASM9, inner) {
    private var sum: Int? = null
    private var operand: Int? = null

    private fun pushInt(i: Int) {
        when {
            sum == null -> sum = i
            operand == null -> operand = i
            else -> {
                // If no operator was present, the default operator is '-'
                sum = sum!! - operand!!
                operand = i
            }
        }
    }
}
