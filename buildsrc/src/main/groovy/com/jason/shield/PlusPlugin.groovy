package com.jason.shield

import com.android.build.api.transform.*
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import com.jason.shield.visitors.PlusPluginClassVisitor
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

public class PlusPlugin extends Transform implements Plugin<Project> {

    private static igonreClasses = [
            "R.class",
            "BuildConfig.class",
    ]

    void apply(Project project) {
        // gradle task time listener
        project.gradle.addListener(new TimeListener())

        // register transform
        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(this)
    }

    @Override
    String getName() {
        return "PlusPlugin"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        printf "%s : >> plugin visit start\n", PlusPluginConifg.BUILD_LOG_TAG
        def startTime = System.currentTimeMillis()
        Collection<TransformInput> inputs = transformInvocation.inputs
        TransformOutputProvider outputProvider = transformInvocation.outputProvider
        // 删除之前的输出
        if (outputProvider != null)
            outputProvider.deleteAll()
        // 遍历inputs
        inputs.each { TransformInput input ->
            //遍历directoryInputs
            input.directoryInputs.each { DirectoryInput directoryInput ->
                handleDirectoryInput(directoryInput, outputProvider)
            }

            //遍历jarInputs
            input.jarInputs.each { JarInput jarInput ->
                handleJarInputs(jarInput, outputProvider)
            }
        }
        long cost = (System.currentTimeMillis() - startTime)
        printf "%s : >> plugin visit end\n", PlusPluginConifg.BUILD_LOG_TAG
        printf "%s : >> plugin cost %dms\n", PlusPluginConifg.BUILD_LOG_TAG, cost
    }

    /**
     * 处理文件目录下的class文件
     * @param directoryInput
     * @param outputProvider
     */
    static void handleDirectoryInput(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        //是否是目录
        if (directoryInput.file.isDirectory()) {
            ClassReader classReader
            ClassWriter classWriter
            ClassVisitor cv
            byte[] code
            FileOutputStream fos
            //列出目录所有文件（包含子文件夹，子文件夹内文件）
            directoryInput.file.eachFileRecurse { File file ->
                def name = file.name
                if (checkClassFile(name)) {
                    if (PlusPluginClassVisitor.checkClassFile(name)) {
                        printf "%s : >>>> plugin deal \"class\" <%s>\n", PlusPluginConifg.BUILD_LOG_TAG, name
                        classReader = new ClassReader(file.bytes)
                        classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                        cv = new PlusPluginClassVisitor(classWriter)
                        classReader.accept(cv, EXPAND_FRAMES)
                        code = classWriter.toByteArray()
                        fos = new FileOutputStream(
                                file.parentFile.absolutePath + File.separator + name)
                        fos.write(code)
                        fos.close()
                    }
                }
            }
        }

        //处理完输入文件之后，要把输出给下一个任务
        def dest = outputProvider.getContentLocation(directoryInput.name,
                directoryInput.contentTypes, directoryInput.scopes,
                Format.DIRECTORY)
        FileUtils.copyDirectory(directoryInput.file, dest)
    }

    /**
     * 处理Jar中的class文件
     * @param jarInput
     * @param outputProvider
     */
    static void handleJarInputs(JarInput jarInput, TransformOutputProvider outputProvider) {
        if (jarInput.file.getAbsolutePath().endsWith(".jar")) {
            JarEntry jarEntry
            String entryName
            ZipEntry zipEntry
            InputStream inputStream
            ClassReader classReader
            ClassWriter classWriter
            ClassVisitor cv
            byte[] code
            //重名名输出文件,因为可能同名,会覆盖
            def jarName = jarInput.name
            def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length() - 4)
            }
            JarFile jarFile = new JarFile(jarInput.file)
            Enumeration enumeration = jarFile.entries()
            File tmpFile = new File(jarInput.file.getParent() + File.separator + "classes_temp.jar")
            //避免上次的缓存被重复插入
            if (tmpFile.exists()) {
                tmpFile.delete()
            }
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tmpFile))
            //用于保存
            while (enumeration.hasMoreElements()) {
                jarEntry = (JarEntry) enumeration.nextElement()
                entryName = jarEntry.getName()
                zipEntry = new ZipEntry(entryName)
                inputStream = jarFile.getInputStream(jarEntry)
                //插桩class
                if (checkClassFile(entryName) && PlusPluginClassVisitor.checkClassFile(entryName)) {
                    //class文件处理
                    printf "%s : >>>> plugin deal \"jar-class\" <%s>\n", PlusPluginConifg.BUILD_LOG_TAG, entryName
                    jarOutputStream.putNextEntry(zipEntry)
                    classReader = new ClassReader(IOUtils.toByteArray(inputStream))
                    classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    cv = new PlusPluginClassVisitor(classWriter)
                    classReader.accept(cv, ClassReader.EXPAND_FRAMES)
                    code = classWriter.toByteArray()
                    jarOutputStream.write(code)
                } else {
                    jarOutputStream.putNextEntry(zipEntry)
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }
                jarOutputStream.closeEntry()
            }
            //结束
            jarOutputStream.close()
            jarFile.close()
            def dest = outputProvider.getContentLocation(jarName + md5Name,
                    jarInput.contentTypes, jarInput.scopes, Format.JAR)
            FileUtils.copyFile(tmpFile, dest)
            tmpFile.delete()
        }
    }

    /**
     * 检查class文件是否需要处理
     * @param fileName
     * @return
     */
    static boolean checkClassFile(String name) {
        //只处理需要的class文件
        return !name.startsWith("R\$") && !igonreClasses.contains(name) && name.endsWith(".class")
    }
}