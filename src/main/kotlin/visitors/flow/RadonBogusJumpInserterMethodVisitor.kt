package com.github.gitantoinee.deobfuscator.radon.visitors.flow

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class RadonBogusJumpInserterMethodVisitor(
    /**
     * The owner, the name and the descriptor of the predicate field
     */
    private val predicateField: Triple<String, String, String>,
    inner: MethodVisitor? = null,
) : MethodVisitor(Opcodes.ASM9, inner) {
    private enum class State {
        /**
         * The predicate local variable is not found
         */
        DISABLED,

        /**
         * Find elements used in IDLE and REMOVING states
         */
        LOADING,

        /**
         * Wait for a bogus jump
         */
        IDLE,

        /**
         * Removing a bogus jump
         */
        REMOVING,
    }

    private var state: State = State.LOADING

    override fun visitLabel(label: Label) {
        // Set the state to IDLE when the label changes because the loading phase is only at the beginning of the method
        // when there is no labels. And the instructions of a bogus jump are only in a label
        state = State.IDLE

        super.visitLabel(label)
    }
}
