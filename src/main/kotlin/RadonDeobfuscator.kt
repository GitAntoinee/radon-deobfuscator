package com.github.gitantoinee.deobfuscator.radon

import com.github.gitantoinee.deobfuscator.radon.visitors.RadonBadAnnotationClassVisitor
import com.github.gitantoinee.deobfuscator.radon.visitors.RadonHideCodeClassVisitor
import com.github.gitantoinee.deobfuscator.radon.visitors.RadonLocalVariableRenamerClassVisitor
import com.github.gitantoinee.deobfuscator.radon.visitors.flow.RadonBogusJumpInserterClassVisitor
import com.github.gitantoinee.deobfuscator.radon.visitors.flow.RadonGotoReplacerClassVisitor
import com.github.gitantoinee.deobfuscator.radon.visitors.references.RadonInvokeDynamicClassVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import java.io.InputStream
import java.io.OutputStream

public class RadonDeobfuscator {
    public fun deobfuscate(inputStream: InputStream, outputStream: OutputStream) {
        val reader = ClassReader(inputStream.readBytes())

        val classNode = ClassNode().also {
            reader.accept(
                RadonHideCodeClassVisitor(
                    RadonBogusJumpInserterClassVisitor(
                        RadonGotoReplacerClassVisitor(
                            it
                        )
                    )
                ),
                0
            )
        }

        val writer = ClassWriter(reader, 0).also {
            reader.accept(
                RadonHideCodeClassVisitor(
                    RadonBogusJumpInserterClassVisitor(
                        RadonGotoReplacerClassVisitor(
                            RadonBadAnnotationClassVisitor(
                                RadonLocalVariableRenamerClassVisitor(
                                    RadonInvokeDynamicClassVisitor(
                                        it
                                    )
                                )
                            )
                        )
                    )
                ),
                0
            )
        }

        outputStream.write(writer.toByteArray())
    }
}
