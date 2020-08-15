package com.github.gitantoinee.deobfuscator.radon.visitors

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class RadonArithmeticMethodVisitor(inner: MethodVisitor? = null) : MethodVisitor(Opcodes.ASM9, inner) {
    private var sum: Int = 0
    private var operand: Number = 0

    override fun visitInsn(opcode: Int) {
        when (opcode) {
            Opcodes.ICONST_M1 -> operand = -1
            Opcodes.ICONST_0 -> operand = 0
            Opcodes.ICONST_1 -> operand = 1
            Opcodes.ICONST_2 -> operand = 2
            Opcodes.ICONST_3 -> operand = 3
            Opcodes.ICONST_4 -> operand = 4
            Opcodes.ICONST_5 -> operand = 5

            Opcodes.IADD -> sum += operand.toInt()
            Opcodes.ISUB -> sum -= operand.toInt()
            Opcodes.IMUL -> sum *= operand.toInt()
            Opcodes.IDIV -> sum /= operand.toInt()
            Opcodes.IREM -> sum %= operand.toInt()

            else -> {
                sum = 0

                super.visitInsn(opcode)
            }
        }
    }

    override fun visitIntInsn(opcode: Int, operand: Int) {
        this.operand = operand
    }

    override fun visitLdcInsn(value: Any) {
        when (value) {
            is Number -> operand = value
            else -> error("Load constant operand is not a number")
        }
    }
}

