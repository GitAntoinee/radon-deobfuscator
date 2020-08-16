package com.github.gitantoinee.deobfuscator.radon.visitors.number

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class RadonArithmeticMethodVisitor(inner: MethodVisitor? = null) : MethodVisitor(Opcodes.ASM9, inner) {
    private var sumOrNull: Int? = null
    private var operandOrNull: Int? = null
        set(value) {
            println("Operator : $value")
            field = value
        }

    private var sum: Int
        get() = sumOrNull ?: error("Sum is null")
        set(value) {
            sumOrNull = value
        }

    private var operand: Int
        get() = operandOrNull ?: error("Operand is null")
        set(value) {
            operandOrNull = value
        }

    override fun visitInsn(opcode: Int) {
        when (opcode) {
            Opcodes.ICONST_M1 -> operand = -1
            Opcodes.ICONST_0 -> operand = 0
            Opcodes.ICONST_1 -> operand = 1
            Opcodes.ICONST_2 -> operand = 2
            Opcodes.ICONST_3 -> operand = 3
            Opcodes.ICONST_4 -> operand = 4
            Opcodes.ICONST_5 -> operand = 5

            Opcodes.IADD -> sum += operand
            Opcodes.ISUB -> sum -= operand
            Opcodes.IMUL -> sum *= operand
            Opcodes.IDIV -> sum /= operand
            Opcodes.IREM -> sum %= operand

            else -> super.visitInsn(opcode)
        }
    }

    override fun visitIntInsn(opcode: Int, operand: Int) {
        if (Opcodes.BIPUSH == opcode || Opcodes.SIPUSH == opcode) {
            this.operand = operand
        } else {
            super.visitIntInsn(opcode, operand)
        }
    }

    override fun visitLdcInsn(value: Any?) {
        if (value is Number) {
            operand = value.toInt()
        } else {
            super.visitLdcInsn(value)
        }
    }
}
