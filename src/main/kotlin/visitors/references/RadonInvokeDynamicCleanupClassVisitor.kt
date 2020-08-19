package com.github.gitantoinee.deobfuscator.radon.visitors.references

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Handle
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * Remove bootstrap methods created by radon
 */
public class RadonInvokeDynamicCleanupClassVisitor(
    private val bootstrapMethods: Set<Handle>,
    inner: ClassVisitor? = null,
) : ClassVisitor(Opcodes.ASM9, inner) {
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?,
    ): MethodVisitor {
        return super.visitMethod(access, name, descriptor, signature, exceptions)
    }
}
