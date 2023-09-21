package com.jxfzzzt;

import sootup.callgraph.CallGraph;
import sootup.callgraph.CallGraphAlgorithm;
import sootup.callgraph.RapidTypeAnalysisAlgorithm;
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.core.signatures.MethodSignature;
import sootup.core.types.ClassType;
import sootup.core.types.PrimitiveType;
import sootup.core.types.VoidType;
import sootup.java.bytecode.inputlocation.JavaClassPathAnalysisInputLocation;
import sootup.java.core.JavaIdentifierFactory;
import sootup.java.core.JavaProject;
import sootup.java.core.JavaSootClass;
import sootup.java.core.language.JavaLanguage;
import sootup.java.core.views.JavaView;

import java.util.Collections;
import java.util.Set;

public class Main03 {

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
        JavaView javaView = javaProject.createFullView();

        ClassType classType = javaProject.getIdentifierFactory().getClassType("example.SootTest");

        MethodSignature entryMethodNode = JavaIdentifierFactory.getInstance().getMethodSignature(classType,
                JavaIdentifierFactory.getInstance().getMethodSubSignature("printFizzBuzz", VoidType.getInstance(), Collections.singletonList(PrimitiveType.IntType.getInstance()))
        );

        CallGraphAlgorithm algorithm = new RapidTypeAnalysisAlgorithm(javaView, javaView.getTypeHierarchy());
        CallGraph callGraph = algorithm.initialize(Collections.singletonList(entryMethodNode));

        Set<MethodSignature> methodSignatures = callGraph.callsFrom(entryMethodNode);

        System.out.println("printFizzBuzz(int k) 调用了以下方法: ");
        for (MethodSignature signature1 : methodSignatures) {
            System.out.println(signature1.getSubSignature());
        }
        System.out.println("=================================");


        show(callGraph, entryMethodNode, 0);
    }


    // 递归打印展示
    static void show(CallGraph callGraph, MethodSignature signature, Integer depth) {
        // 打印
        for(int i = 0 ; i < depth; i ++ ) System.out.print("\t");
        System.out.println(signature.getSubSignature());

        for (MethodSignature methodSignature : callGraph.callsFrom(signature)) {
            show(callGraph, methodSignature, depth + 1);
        }
    }

}
