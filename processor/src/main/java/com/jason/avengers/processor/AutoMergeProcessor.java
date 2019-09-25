package com.jason.avengers.processor;

import com.google.auto.service.AutoService;
import com.jason.avengers.annotations.AutoMerge;
import com.jason.avengers.infos.VariableInfo;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.jason.avengers.annotations.AutoMerge"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class AutoMergeProcessor extends AbstractProcessor {

    private Map<String, List<VariableInfo>> classElements = new HashMap<>();

    private Filer filer;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnv.getFiler();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // 收集信息
        collectInfo(roundEnvironment);
        // 生成新的Java文件
        writeJavaFile();
        return true;
    }

    private void collectInfo(RoundEnvironment roundEnvironment) {
        classElements.clear();

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(AutoMerge.class);
        for (Element element : elements) {
            // 获取 AutoMerge 注解的值
            String autoMergeClassFullName = element.getAnnotation(AutoMerge.class).value();
            // 被注解的元素 Class
            TypeElement typeElement = (TypeElement) element;
            // 被注解的元素 Class 的完整路径
            String classFullName = typeElement.getQualifiedName().toString();
            // 收集Class中所有被注解的元素
            List<VariableInfo> variableList = classElements.get(classFullName);
            if (variableList == null) {
                variableList = new ArrayList<>();
                classElements.put(classFullName, variableList);
            }
            VariableInfo variableInfo = new VariableInfo();
            variableInfo.setAutoMergeClassFullName(autoMergeClassFullName);
            variableInfo.setTypeElement(typeElement);
            variableList.add(variableInfo);
        }
    }

    private void writeJavaFile() {
        try {
            for (String classFullName : classElements.keySet()) {
                int lastIndexOfDot = classFullName.lastIndexOf(".");
                // 构建Class
                TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(classFullName.substring(lastIndexOfDot + 1))
                        .addModifiers(Modifier.PUBLIC);

                List<VariableInfo> variableList = classElements.get(classFullName);
                for (VariableInfo variableInfo : variableList) {
                    TypeElement typeElement = variableInfo.getTypeElement();
                    List<ExecutableElement> elementMethods = getElementMethods(typeElement);

                    for (ExecutableElement executableElement : elementMethods) {
                        String methodName = executableElement.getSimpleName().toString();
                        Set<Modifier> modifiers = executableElement.getModifiers();
                        List<? extends VariableElement> parameters = executableElement.getParameters();

                        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder(methodName)
                                .addModifiers(modifiers);

                        ParameterSpec parameterSpec;
                        for (VariableElement variableElement : parameters) {
                            ClassName className = ClassName.get((TypeElement) variableElement.getEnclosingElement());

                            int size = variableElement.getEnclosingElement().getModifiers().size();
                            Modifier[] modifierArray = new Modifier[size];
                            modifierArray = variableElement.getEnclosingElement().getModifiers().toArray(modifierArray);

                            ParameterSpec.Builder parameterSpecBuilder
                                    = ParameterSpec.builder(className, variableElement.getSimpleName().toString());
                            parameterSpec = parameterSpecBuilder.addModifiers(modifierArray)
                                    .build();

                            methodSpecBuilder.addParameter(parameterSpec);
                        }

                        MethodSpec methodSpec = methodSpecBuilder.build();
                        typeSpecBuilder.addMethod(methodSpec);
                    }
                }

                TypeSpec typeSpec = typeSpecBuilder.build();
                JavaFile javaFile = JavaFile.builder(classFullName.substring(0, lastIndexOfDot), typeSpec).build();
                // 生成class文件
                javaFile.writeTo(filer);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<ExecutableElement> getElementMethods(TypeElement typeElement) {
        List<? extends Element> elementMembers = elementUtils.getAllMembers(typeElement);
        List<ExecutableElement> elementMethods = ElementFilter.methodsIn(elementMembers);
        return elementMethods;
    }
}
