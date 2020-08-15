package com.github.gitantoinee.deobfuscator.radon

import java.io.File
import java.util.jar.JarEntry
import java.util.jar.JarInputStream
import java.util.jar.JarOutputStream

public fun main(args: Array<String>) {
    val input = File(args[0]).also {
        require(it.isFile) { "Input is not a file" }
        require(it.canRead()) { "Input is not readable" }
    }
    val output = File(args[1]).also {
        if (it.delete()) {
            println("Overwriting output file")
        }
    }

    val deobfuscator = RadonDeobfuscator()

    JarInputStream(input.inputStream()).use { inputStream ->
        JarOutputStream(output.outputStream()).use { outputStream ->
            var inputEntry = inputStream.nextJarEntry

            while (inputEntry != null) {
                outputStream.putNextEntry(JarEntry(inputEntry.name))

                if (!inputEntry.isDirectory && inputEntry.name.endsWith(".class")) {
                    deobfuscator.deobfuscate(inputStream, outputStream)
                } else {
                    inputStream.copyTo(outputStream)
                }

                inputEntry = inputStream.nextJarEntry
            }
        }
    }
}