package com.github.gitantoinee.deobfuscator.radon.visitors.flow

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
}
