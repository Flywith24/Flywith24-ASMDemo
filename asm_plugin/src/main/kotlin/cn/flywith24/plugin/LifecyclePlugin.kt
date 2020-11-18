package cn.flywith24.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class LifecyclePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("loading.....")
        val android = project.extensions.getByType(AppExtension::class.java)
        println("registerTransform.....")
        android.registerTransform(LifecycleTransform())
    }
}