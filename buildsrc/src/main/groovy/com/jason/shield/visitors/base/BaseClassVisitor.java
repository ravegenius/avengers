package com.jason.shield.visitors.base;

import com.jason.shield.PlusPluginConifg;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class BaseClassVisitor extends ClassVisitor implements Opcodes {

    public BaseClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM7, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        System.out.println(PlusPluginConifg.BUILD_LOG_TAG + " : >>>>>>>> plugin class visit start <" + name + ">");
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        System.out.println(PlusPluginConifg.BUILD_LOG_TAG + " : >>>>>>>> plugin class visit end");
    }
}
