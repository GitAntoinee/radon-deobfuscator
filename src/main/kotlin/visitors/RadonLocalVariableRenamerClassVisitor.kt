package com.github.gitantoinee.deobfuscator.radon.visitors

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

public class RadonLocalVariableRenamerClassVisitor(inner: ClassVisitor? = null) : ClassVisitor(Opcodes.ASM9, inner) {
    private lateinit var classDescriptor: String

    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?,
    ) {
        classDescriptor = Type.getObjectType(name).descriptor

        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?,
    ): MethodVisitor {
        val inner = super.visitMethod(access, name, descriptor, signature, exceptions)
        val isStatic = access or Opcodes.ACC_STATIC == 0

        return RadonLocalVariableRenamerMethodVisitor(classDescriptor, isStatic, inner)
    }
}
