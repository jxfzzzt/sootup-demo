package com.jxfzzzt;

import lombok.extern.slf4j.Slf4j;
import sootup.callgraph.CallGraph;
import sootup.callgraph.CallGraphAlgorithm;
import sootup.callgraph.ClassHierarchyAnalysisAlgorithm;
import sootup.callgraph.RapidTypeAnalysisAlgorithm;
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.core.signatures.MethodSignature;
import sootup.core.types.ClassType;
import sootup.core.types.VoidType;
import sootup.java.bytecode.inputlocation.JavaClassPathAnalysisInputLocation;
import sootup.java.core.JavaIdentifierFactory;
import sootup.java.core.JavaProject;
import sootup.java.core.JavaSootClass;
import sootup.java.core.language.JavaLanguage;
import sootup.java.core.views.JavaView;

import java.util.Collections;
import java.util.Set;

@Slf4j
public class Main04 {
    static JavaProject getJavaProject() {
        String path = "src/main/resources/test-demo/target/classes"; // 一定要到classes文件夹中
        AnalysisInputLocation<JavaSootClass> inputLocation = new JavaClassPathAnalysisInputLocation(path);

        JavaLanguage javaLanguage = new JavaLanguage(8);

        return JavaProject.builder(javaLanguage)
                .addInputLocation(inputLocation)
//                .addInputLocation(new JavaClassPathAnalysisInputLocation(System.getProperty("java.home") + "/lib/rt.jar")) // 加了这行堆会爆炸
                .build();
    }

    public static void main(String[] args) {
        JavaProject javaProject = getJavaProject();
        JavaView fullView = javaProject.createFullView();

        ClassType classTypeA = javaProject.getIdentifierFactory().getClassType("example.hierarchy.A");
        ClassType classTypeB = javaProject.getIdentifierFactory().getClassType("example.hierarchy.B");

        MethodSignature methodSignature = JavaIdentifierFactory.getInstance().getMethodSignature(classTypeB,
                JavaIdentifierFactory.getInstance().getMethodSubSignature("calc", VoidType.getInstance(), Collections.singletonList(classTypeA)));


        CallGraphAlgorithm algorithm = new ClassHierarchyAnalysisAlgorithm(fullView, fullView.getTypeHierarchy());
        CallGraph callGraph = algorithm.initialize(Collections.singletonList(methodSignature));

        Set<MethodSignature> methodSignatures = callGraph.callsFrom(methodSignature);

        // 调用复杂的方法
        System.out.println("calc(A a) 调用了以下方法: ");
        for (MethodSignature signature : methodSignatures) {
            System.out.println(signature);
        }
        System.out.println("=================================");
    }
}
