package com.github.gitantoinee.deobfuscator.radon.visitors.flow

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class RadonGotoReplacerMethodVisitor(
    /**
     * The possible predicate fields
     */
    private val possiblePredicateFields: List<String>,
    inner: MethodVisitor? = null,
) : MethodVisitor(Opcodes.ASM9, inner) {
    /**
     * The name and the index of variable used as operand for IFEQ opcodes
     */
    private var predicateVar: Pair<String, Int>? = null
}
