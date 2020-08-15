package com.github.gitantoinee.deobfuscator.radon.visitors.flow

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

public class RadonGotoReplacerClassVisitor(inner: ClassVisitor? = null) : ClassVisitor(Opcodes.ASM9, inner) {
}
