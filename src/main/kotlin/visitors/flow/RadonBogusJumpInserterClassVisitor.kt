package com.github.gitantoinee.deobfuscator.radon.visitors.flow

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class RadonBogusJumpInserterClassVisitor(inner: ClassVisitor? = null) : ClassVisitor(Opcodes.ASM9, inner) {
    private companion object {
        const val PREDICATE_FIELD_ACCESS: Int = Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC or Opcodes.ACC_FINAL
        const val PREDICATE_FIELD_DESCRIPTOR: String = "Z"
    }

    private val possiblePredicateFields: MutableList<Triple<String, String, String>> = mutableListOf()

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

    override fun visitField(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        value: Any?,
    ): FieldVisitor? {
        if (access == PREDICATE_FIELD_ACCESS && descriptor == PREDICATE_FIELD_DESCRIPTOR) {
            possiblePredicateFields.add(Triple(owner, name, descriptor))
        }

        return super.visitField(access, name, descriptor, signature, value)
    }

    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        exceptions: Array<out String>?,
    ): MethodVisitor? {
        val inner = super.visitMethod(access, name, descriptor, signature, exceptions)

        val predicateField = possiblePredicateFields.firstOrNull()
        return predicateField?.let { RadonBogusJumpInserterMethodVisitor(it, inner) } ?: inner
    }
}
