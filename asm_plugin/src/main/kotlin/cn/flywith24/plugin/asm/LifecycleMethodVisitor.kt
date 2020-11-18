package cn.flywith24.plugin.asm

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * @author yyz (杨云召)
 * @date   2020/11/18
 * time   10:42
 * description
 */
class LifecycleMethodVisitor(
    methodVisitor: MethodVisitor,
    private val className: String?,
    private val methodName: String
) : MethodVisitor(Opcodes.ASM5, methodVisitor) {
    override fun visitCode() {
        super.visitCode()
        println("LifecycleMethodVisitor visitCode")

        mv.visitLdcInsn("TAG")
        mv.visitLdcInsn("yyz $className ----- $methodName")
        mv.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "android/util/Log",
            "i",
            "(Ljava/lang/String;Ljava/lang/String;)I",
            false
        )
        mv.visitInsn(Opcodes.POP)
    }
}