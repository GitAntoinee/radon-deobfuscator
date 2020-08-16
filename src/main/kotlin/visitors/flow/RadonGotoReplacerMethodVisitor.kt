package com.github.gitantoinee.deobfuscator.radon.visitors.flow

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class RadonGotoReplacerMethodVisitor(
    /**
     * The owner, the name and the descriptor of the predicate field
     */
    private val predicateField: Triple<String, String, String>,
    inner: MethodVisitor? = null,
) : MethodVisitor(Opcodes.ASM9, inner) {
    private enum class State {
        /**
         * Wait for a replaced goto
         */
        IDLE,

        /**
         * Replacing a goto
         */
        PATCHING,
    }

    private var state: State = State.IDLE

    override fun visitInsn(opcode: Int) {
        if (State.PATCHING == state) {
            if (Opcodes.ACONST_NULL == opcode) Unit // The next instruction will be ATHROW
            else if (Opcodes.ATHROW == opcode) state = State.IDLE // Patched
        } else {
            super.visitInsn(opcode)
        }
    }

    override fun visitJumpInsn(opcode: Int, label: Label) {
        if (State.PATCHING == state) {
            check(Opcodes.IFEQ == opcode) { "Invalid jump instruction while patching a replaced goto ($opcode)" }
            super.visitJumpInsn(Opcodes.GOTO, label)
        } else {
            super.visitJumpInsn(opcode, label)
        }
    }

    override fun visitFieldInsn(opcode: Int, owner: String, name: String, descriptor: String) {
        check(state == State.IDLE) { "Field instruction visited while replacing a goto" }

        if (Opcodes.GETSTATIC == opcode && owner == predicateField.first && name == predicateField.second && descriptor == predicateField.third) {
            state = State.PATCHING
        }

        super.visitFieldInsn(opcode, owner, name, descriptor)
    }
}
