package cn.flywith24.plugin.asm

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes


/**
 * @author yyz (杨云召)
 * @date   2020/11/18
 * time   10:42
 * description
 */
class LifecycleClassVisitor(cv: ClassVisitor) : ClassVisitor(Opcodes.ASM5, cv) {
    private var className: String? = null
    var superName: String? = null

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        this.className = name
        this.superName = superName
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        println("ClassVisitor visitMethod name: $name superName：$superName")
        val mv = cv.visitMethod(access, name, descriptor, signature, exceptions)
        if ("androidx/appcompat/app/AppCompatActivity" == superName) {
            if (name?.startsWith("onCreate") == true) {
                return LifecycleMethodVisitor(mv, className, name)
            }
        }
        return mv
    }
}