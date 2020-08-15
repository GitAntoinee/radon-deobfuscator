package com.github.gitantoinee.deobfuscator.radon.visitors

import org.objectweb.asm.Handle
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class RadonInvokeDynamicMethodVisitor(inner: MethodVisitor? = null) : MethodVisitor(Opcodes.ASM9, inner) {
    override fun visitInvokeDynamicInsn(
        name: String,
        descriptor: String,
        bootstrapMethodHandle: Handle,
        vararg bootstrapMethodArguments: Any,
    ) {
        println("INDY ($bootstrapMethodHandle) : `$name` `$descriptor` - arguments (${bootstrapMethodArguments.size}) : " +
                bootstrapMethodArguments.joinToString(" ", "`", "`"))

        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, *bootstrapMethodArguments)
    }
}
