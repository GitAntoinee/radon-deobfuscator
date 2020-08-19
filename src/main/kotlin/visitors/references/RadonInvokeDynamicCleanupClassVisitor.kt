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
    private lateinit var owner: String

    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?,
    ) {
        owner = name

        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?,
    ): MethodVisitor? {
        return if (bootstrapMethods.all { it.owner != owner && it.name != name && it.desc != descriptor }) {
            super.visitMethod(access, name, descriptor, signature, exceptions)
        } else {
            null
        }
    }
}
