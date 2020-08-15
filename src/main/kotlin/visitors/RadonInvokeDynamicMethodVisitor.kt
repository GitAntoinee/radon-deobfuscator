package com.github.gitantoinee.deobfuscator.radon.visitors

import org.objectweb.asm.Handle
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class RadonInvokeDynamicMethodVisitor(inner: MethodVisitor? = null) : MethodVisitor(Opcodes.ASM9, inner) {
    private companion object {
        /**
         * Depend on the obfuscation, if it use fast invoke dynamic or normal invoke dynamic
         */
        const val FAST: Boolean = true
    }

    override fun visitInvokeDynamicInsn(
        name: String,
        descriptor: String,
        bootstrapMethodHandle: Handle,
        vararg bootstrapMethodArguments: Any,
    ) {
        val isObfuscated = bootstrapMethodArguments.first() is Int
                && bootstrapMethodArguments.drop(1).all { it is String }

        if (isObfuscated) {
            // Deobfuscation will be here
        } else {
            super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, *bootstrapMethodArguments)
        }
    }
}
