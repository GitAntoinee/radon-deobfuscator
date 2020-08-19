package com.github.gitantoinee.deobfuscator.radon.visitors.references

import com.github.gitantoinee.deobfuscator.radon.text.xor
import org.objectweb.asm.Handle
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class RadonInvokeDynamicMethodVisitor(
    private val bootstrapMethods: MutableSet<Handle>,
    inner: MethodVisitor? = null,
) : MethodVisitor(Opcodes.ASM9, inner) {
    private companion object {
        /**
         * Number used to define a static method by radon
         */
        const val STATIC_METHOD_TYPE: Int = 0

        /**
         * Number used to define a virtual method by radon
         */
        const val VIRTUAL_METHOD_TYPE: Int = 1
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
            bootstrapMethods.add(bootstrapMethodHandle)

            // TODO : Find xor encryption

            val originalOwner = (bootstrapMethodArguments[1].toString() xor 2893).replace('.', '/')
            val originalName = bootstrapMethodArguments[2].toString() xor 2993
            val originalDescriptor = bootstrapMethodArguments[3].toString() xor 8372

            val originalOpcode = when ((bootstrapMethodArguments[0] as Int) shl 256 and 255) {
                STATIC_METHOD_TYPE -> Opcodes.INVOKESTATIC
                VIRTUAL_METHOD_TYPE -> Opcodes.INVOKEVIRTUAL
                else -> error("Unknown obfuscated type")
            }

            super.visitMethodInsn(originalOpcode, originalOwner, originalName, originalDescriptor, false)
        } else {
            super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, *bootstrapMethodArguments)
        }
    }
}
