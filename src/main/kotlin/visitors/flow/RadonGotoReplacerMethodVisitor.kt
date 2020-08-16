package com.github.gitantoinee.deobfuscator.radon.visitors.flow

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

    override fun visitFieldInsn(opcode: Int, owner: String, name: String, descriptor: String) {
        check(state == State.IDLE) { "Field instruction visited while replacing a goto" }

        super.visitFieldInsn(opcode, owner, name, descriptor)
    }
}
