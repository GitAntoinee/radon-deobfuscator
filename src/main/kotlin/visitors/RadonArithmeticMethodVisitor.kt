package com.github.gitantoinee.deobfuscator.radon.visitors

import org.objectweb.asm.*

public class RadonArithmeticMethodVisitor(inner: MethodVisitor? = null) : MethodVisitor(Opcodes.ASM9, inner) {
    private var sum: Int? = null
    private var operand: Number = 0

    private inline var sumOrZero: Int
        get() = sum ?: 0
        set(value) {
            sum = value
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

            Opcodes.IADD -> sumOrZero += operand.toInt()
            Opcodes.ISUB -> sumOrZero -= operand.toInt()
            Opcodes.IMUL -> sumOrZero *= operand.toInt()
            Opcodes.IDIV -> sumOrZero /= operand.toInt()
            Opcodes.IREM -> sumOrZero %= operand.toInt()

            else -> {
                when (sum) {
                    null -> super.visitInsn(opcode)
                }

                sum = null
            }
        }
    }

    override fun visitIntInsn(opcode: Int, operand: Int) {
        this.operand = operand
    }

    override fun visitLdcInsn(value: Any) {
        when (value) {
            is Number -> operand = value
            is String, is Type, is Handle, is ConstantDynamic -> sum = 0
            else -> error("Load constant operand is invalid")
        }
    }
}

