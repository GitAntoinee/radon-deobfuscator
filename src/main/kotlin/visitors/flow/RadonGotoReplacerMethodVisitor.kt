package com.github.gitantoinee.deobfuscator.radon.visitors.flow

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class RadonGotoReplacerMethodVisitor(
    /**
     * The possible predicate fields
     */
    private val possiblePredicateFields: List<String> = emptyList(),
    inner: MethodVisitor? = null,
) : MethodVisitor(Opcodes.ASM9, inner) {
    private enum class State {
        /**
         * Predicate field and/or predicate field not found
         */
        DISABLED,

        /**
         * Initial state, find predicate variable and predicate field
         */
        FINDING_PREDICATE,

        /**
         * Waiting
         */
        IDLE,

        /**
         * Remove or replace the created instructions
         *
         * Remove ILOAD, IFEQ, ACONST_NULL and ATHROW
         */
        REMOVING
    }

    /**
     * The index of variable used as operand for IFEQ opcodes
     */
    private var predicateVar: Int? = null

    /**
     * The predicate field
     */
    private var predicateField: String? = null

    private var state: State = State.FINDING_PREDICATE

    /**
     * The current label
     */
    private var currentLabel: Label? = null

    override fun visitLabel(label: Label) {
        if (State.FINDING_PREDICATE == state) {
            state = State.DISABLED
        }

        currentLabel = label
        super.visitLabel(label)
    }

    override fun visitVarInsn(opcode: Int, `var`: Int) {
        if (State.DISABLED == state) {
            return super.visitVarInsn(opcode, `var`)
        }

        if (Opcodes.ILOAD == opcode && predicateField != null && predicateVar == `var`) {
            state = State.REMOVING
            return
        } else {
            state = State.IDLE
        }

        if (Opcodes.ISTORE == opcode && predicateVar == null && predicateField != null && currentLabel == null) {
            predicateVar = `var`
        }

        super.visitVarInsn(opcode, `var`)
    }

    override fun visitJumpInsn(opcode: Int, label: Label) {
        if (State.REMOVING != state) {
            super.visitJumpInsn(opcode, label)
            return
        }

        when (opcode) {
            Opcodes.IFEQ -> super.visitJumpInsn(Opcodes.GOTO, label)
            else -> {
                super.visitVarInsn(Opcodes.ILOAD, predicateVar!!)
                super.visitJumpInsn(opcode, label)
            }
        }
    }

    override fun visitFieldInsn(opcode: Int, owner: String, name: String, descriptor: String) {
        if (Opcodes.GETSTATIC == opcode
            && (possiblePredicateFields.isEmpty() || name in possiblePredicateFields)
            && descriptor == "Z"
            && predicateVar == null && predicateField == null && currentLabel == null
        ) {
            predicateField = name
        }

        super.visitFieldInsn(opcode, owner, name, descriptor)
    }
}
