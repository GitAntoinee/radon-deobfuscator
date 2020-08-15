package com.github.gitantoinee.deobfuscator.radon

import com.github.gitantoinee.deobfuscator.radon.visitors.RadonIllegalAnnotationClassVisitor
import com.github.gitantoinee.deobfuscator.radon.visitors.RadonInvokeDynamicClassVisitor
import com.github.gitantoinee.deobfuscator.radon.visitors.RadonSyntheticBridgeRemoverClassVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.InputStream
import java.io.OutputStream

public class RadonDeobfuscator {
    public fun deobfuscate(inputStream: InputStream, outputStream: OutputStream) {
        val reader = ClassReader(inputStream.readBytes())
        val writer = ClassWriter(reader, 0).also {
            reader.accept(
                RadonSyntheticBridgeRemoverClassVisitor(
                    RadonIllegalAnnotationClassVisitor(
                        RadonInvokeDynamicClassVisitor(it))),
                0)
        }
        outputStream.write(writer.toByteArray())
    }
}
