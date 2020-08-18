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
        IDLE {
            override val nextState: State
                get() = PATCHING_JUMP
        },

        /**
         * Replacing a jump instruction to a goto instruction
         */
        PATCHING_JUMP {
            override val nextState: State
                get() = PATCHING_EXCEPTION
        },

        /**
         * Removing an `ACONST_NULL` instruction
         */
        PATCHING_EXCEPTION {
            override val nextState: State
                get() = PATCHING_THROW
        },

        /**
         * Removing an `ATHROW` instruction
         */
        PATCHING_THROW {
            override val nextState: State
                get() = IDLE
        };

        abstract val nextState: State
    }

    private var state: State = State.IDLE

    override fun visitInsn(opcode: Int) {
        if (State.PATCHING_EXCEPTION == state) {
            check(Opcodes.ACONST_NULL == opcode) { "Patching exception but invalid opcode" }
            state = state.nextState
        } else if (State.PATCHING_THROW == state) {
            check(Opcodes.ATHROW == opcode) { "Patching throw but invalid opcode" }
            state = state.nextState
        } else {
            super.visitInsn(opcode)
        }
    }

    override fun visitJumpInsn(opcode: Int, label: Label) {
        if (State.PATCHING_JUMP == state) {
            check(Opcodes.IFEQ == opcode) { "Invalid jump instruction while patching a replaced goto ($opcode)" }
            super.visitJumpInsn(Opcodes.GOTO, label)
            state = state.nextState
        } else {
            super.visitJumpInsn(opcode, label)
        }
    }

    override fun visitFieldInsn(opcode: Int, owner: String, name: String, descriptor: String) {
        check(state == State.IDLE) { "Field instruction visited while replacing a goto (state: $state)" }

        if (Opcodes.GETSTATIC == opcode && owner == predicateField.first && name == predicateField.second && descriptor == predicateField.third) {
            state = state.nextState
        } else {
            super.visitFieldInsn(opcode, owner, name, descriptor)
        }
    }
}
