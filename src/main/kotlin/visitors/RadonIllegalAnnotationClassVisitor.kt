package com.github.gitantoinee.deobfuscator.radon.visitors

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

public class RadonIllegalAnnotationClassVisitor(inner: ClassVisitor? = null) : ClassVisitor(Opcodes.ASM9, inner) {
    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
        return if ("@" != descriptor && "" != descriptor) super.visitAnnotation(descriptor, visible) else null
    }
}
