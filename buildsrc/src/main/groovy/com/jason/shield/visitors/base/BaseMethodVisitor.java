package com.jason.shield.visitors.base;

import com.jason.shield.PlusPluginConifg;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

public class BaseMethodVisitor extends AdviceAdapter {

    public BaseMethodVisitor(MethodVisitor mv, int access, String name, String descriptor) {
        super(Opcodes.ASM7, mv, access, name, descriptor);
        System.out.println(PlusPluginConifg.BUILD_LOG_TAG + " : >>>>>>>>>>>> plugin method visit start <" + name + ">");
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        System.out.println(PlusPluginConifg.BUILD_LOG_TAG + " : >>>>>>>>>>>> plugin method visit end");
    }
}
