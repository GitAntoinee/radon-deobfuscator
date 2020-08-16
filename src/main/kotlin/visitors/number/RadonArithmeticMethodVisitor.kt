package com.github.gitantoinee.deobfuscator.radon.visitors.number

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class RadonArithmeticMethodVisitor(inner: MethodVisitor? = null) : MethodVisitor(Opcodes.ASM9, inner) {
    private var sumOrNull: Int? = null
    private var operandOrNull: Int? = null

    private fun pushInt(i: Int) {
        when {
            sumOrNull == null -> sumOrNull = i
            operandOrNull == null -> operandOrNull = i
            else -> {
                // If no operator was present, the default operator is '-'
                sumOrNull = sumOrNull!! - operandOrNull!!
                operandOrNull = i
            }
        }
    }

    override fun visitInsn(opcode: Int) {
        when (opcode) {
            Opcodes.ICONST_M1 -> pushInt(-1)
            Opcodes.ICONST_0 -> pushInt(0)
            Opcodes.ICONST_1 -> pushInt(1)
            Opcodes.ICONST_2 -> pushInt(2)
            Opcodes.ICONST_3 -> pushInt(3)
            Opcodes.ICONST_4 -> pushInt(4)
            Opcodes.ICONST_5 -> pushInt(5)

            // Non-arithmetic opcode
            else -> super.visitInsn(opcode)
        }
    }

    override fun visitIntInsn(opcode: Int, operand: Int) {
        if (Opcodes.BIPUSH == opcode || Opcodes.SIPUSH == opcode) {
            pushInt(operand)
        } else {
            super.visitIntInsn(opcode, operand)
        }
    }

    override fun visitLdcInsn(value: Any?) {
        when (value) {
            is Number -> pushInt(value.toInt())
            else -> super.visitLdcInsn(value)
        }
    }
}
