package com.github.gitantoinee.deobfuscator.radon.visitors

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

public class RadonSyntheticBridgeRemoverClassVisitor(inner: ClassVisitor? = null) : ClassVisitor(Opcodes.ASM9, inner) {
}
