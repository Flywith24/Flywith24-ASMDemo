package cn.flywith24.plugin

import cn.flywith24.plugin.asm.LifecycleClassVisitor
import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.FileOutputStream

/**
 * @author yyz (杨云召)
 * @date   2020/11/18
 * time   10:17
 * description
 */
class LifecycleTransform : Transform() {
    override fun getName(): String = "Flywith24"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> =
        TransformManager.CONTENT_CLASS

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> = TransformManager.PROJECT_ONLY

    override fun isIncremental(): Boolean = false

    override fun transform(transformInvocation: TransformInvocation) {
        val transformInputs = transformInvocation.inputs
        val outputProvider = transformInvocation.outputProvider
        outputProvider?.deleteAll()

        transformInputs.forEach { transformInput ->
            transformInput.jarInputs.forEach { jarInput ->
                val file = jarInput.file
                println("发现 jar: ${file.name} ${jarInput.contentTypes}")
                val dest = outputProvider.getContentLocation(
                    jarInput.name,
                    jarInput.contentTypes,
                    jarInput.scopes,
                    Format.JAR
                )
                FileUtils.copyFile(file, dest)
            }
            transformInput.directoryInputs.forEach { directoryInput ->
                val fileTree = directoryInput.file.walk()
                fileTree
                    .filter { it.isFile }
                    .filter { "class" == it.extension }
                    .forEach { file ->
                        val classReader = ClassReader(file.readBytes())
                        val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                        val classVisitor = LifecycleClassVisitor(classWriter)
                        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                        val bytes = classWriter.toByteArray()
                        val outputStream = FileOutputStream(file.path)
                        outputStream.write(bytes)
                        outputStream.close()
                    }

                val dest = outputProvider.getContentLocation(
                    directoryInput.name,
                    directoryInput.contentTypes,
                    directoryInput.scopes,
                    Format.DIRECTORY
                )
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
        }
    }
}