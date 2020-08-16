package com.github.gitantoinee.deobfuscator.radon.visitors.number

import org.objectweb.asm.*

public class RadonArithmeticMethodVisitor(inner: MethodVisitor? = null) : MethodVisitor(Opcodes.ASM9, inner) {
    private var sum: Int? = null
    private var operand: Int? = null

    private inline var sumOrZero: Int
        get() = sum ?: 0.also { print("0") }
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

            Opcodes.IADD -> sumOrZero += operand ?: error("Operand not defined")
            Opcodes.ISUB -> sumOrZero -= operand ?: error("Operand not defined")
            Opcodes.IMUL -> sumOrZero *= operand ?: error("Operand not defined")
            Opcodes.IDIV -> sumOrZero /= operand ?: error("Operand not defined")
            Opcodes.IREM -> sumOrZero %= operand ?: error("Operand not defined")

            else -> {
                when (sum) {
                    null -> Unit

                    -1 -> super.visitInsn(Opcodes.ICONST_M1)
                    0 -> super.visitInsn(Opcodes.ICONST_0)
                    1 -> super.visitInsn(Opcodes.ICONST_1)
                    2 -> super.visitInsn(Opcodes.ICONST_2)
                    3 -> super.visitInsn(Opcodes.ICONST_3)
                    4 -> super.visitInsn(Opcodes.ICONST_4)
                    5 -> super.visitInsn(Opcodes.ICONST_5)

                    else -> when {
                        sum!! > Byte.MIN_VALUE && sum!! < Byte.MAX_VALUE -> super.visitIntInsn(Opcodes.BIPUSH, sum!!)
                        sum!! > Short.MIN_VALUE && sum!! < Short.MAX_VALUE -> super.visitIntInsn(Opcodes.SIPUSH, sum!!)
                        else -> super.visitLdcInsn(sum!!)
                    }
                }

                sum = null
                super.visitInsn(opcode)
            }
        }
    }

    override fun visitIntInsn(opcode: Int, operand: Int) {
        if (Opcodes.SIPUSH == opcode || Opcodes.BIPUSH == opcode) {
            if (sum == null) {
                sum = operand
            } else {
                this.operand = operand
            }
        }
    }

    override fun visitLdcInsn(value: Any) {
        when (value) {
            is Number -> operand = value.toInt()
            is String, is Type, is Handle, is ConstantDynamic -> {
                sum = null
                super.visitLdcInsn(value)
            }
            else -> error("Load constant operand is invalid")
        }
    }
}
