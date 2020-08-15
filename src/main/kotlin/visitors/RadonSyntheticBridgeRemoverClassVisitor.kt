package com.github.gitantoinee.deobfuscator.radon.visitors

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class RadonSyntheticBridgeRemoverClassVisitor(inner: ClassVisitor? = null) : ClassVisitor(Opcodes.ASM9, inner) {
    private companion object {
        const val ADDED_ACCESS: Int = (Opcodes.ACC_SYNTHETIC or Opcodes.ACC_BRIDGE).inv()
    }

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        val newAccess = access and ADDED_ACCESS

        super.visit(version, newAccess, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        exceptions: Array<out String>?,
    ): MethodVisitor {
        val newAccess = access and ADDED_ACCESS

        return super.visitMethod(newAccess, name, descriptor, signature, exceptions)
    }
}
