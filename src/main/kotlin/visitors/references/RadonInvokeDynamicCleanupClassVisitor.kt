package com.github.gitantoinee.deobfuscator.radon.visitors.references

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

/**
 * Remove bootstrap methods created by radon
 */
public class RadonInvokeDynamicCleanupClassVisitor(
    private val bootstrapMethods: Set<String>,
    inner: ClassVisitor? = null,
) : ClassVisitor(Opcodes.ASM9, inner) {
}
