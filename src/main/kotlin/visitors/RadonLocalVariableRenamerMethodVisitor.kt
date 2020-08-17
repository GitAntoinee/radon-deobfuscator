package com.github.gitantoinee.deobfuscator.radon.visitors

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class RadonLocalVariableRenamerMethodVisitor(inner: MethodVisitor? = null) : MethodVisitor(Opcodes.ASM9, inner)
