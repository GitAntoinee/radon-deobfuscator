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
                    -1 -> super.visitInsn(Opcodes.ICONST_M1)
                    0 -> super.visitInsn(Opcodes.ICONST_0)
                    1 -> super.visitInsn(Opcodes.ICONST_1)
                    2 -> super.visitInsn(Opcodes.ICONST_2)
                    3 -> super.visitInsn(Opcodes.ICONST_3)
                    4 -> super.visitInsn(Opcodes.ICONST_4)
                    5 -> super.visitInsn(Opcodes.ICONST_5)

                    else -> when {
                        sum == null -> super.visitInsn(opcode)

                        sum!! >= Byte.MIN_VALUE && sum!! <= Byte.MAX_VALUE -> super.visitIntInsn(Opcodes.BIPUSH, sum!!)
                        sum!! >= Short.MIN_VALUE && sum!! <= Short.MAX_VALUE -> super.visitIntInsn(Opcodes.SIPUSH,
                            sum!!)
                        else -> super.visitLdcInsn(sum!!)
                    }
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

        super.visitLdcInsn(value)
    }
}
