package com.jason.shield.visitors;

import com.jason.shield.visitors.base.BaseClassVisitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.List;

public class PlusPluginClassVisitor extends BaseClassVisitor {

    private static List<String> classes;

    static {
        classes = new ArrayList<>();
        classes.add("com/jason/avengers/common/base/BaseActivity.class");
        classes.add("com/jason/avengers/common/base/BaseFragment.class");
    }

    public PlusPluginClassVisitor(ClassVisitor cv) {
        super(cv);
    }

    public static boolean checkClassFile(String className) {
        return classes.contains(className);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
        return new PlusPluginMethodVisitor(mv, access, name, descriptor);
    }
}
