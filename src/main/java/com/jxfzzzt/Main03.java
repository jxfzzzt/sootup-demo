package com.jxfzzzt;

import sootup.callgraph.CallGraph;
import sootup.callgraph.CallGraphAlgorithm;
import sootup.callgraph.ClassHierarchyAnalysisAlgorithm;
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.core.signatures.MethodSignature;
import sootup.core.types.ClassType;
import sootup.core.types.PrimitiveType;
import sootup.core.types.VoidType;
import sootup.java.bytecode.inputlocation.JavaClassPathAnalysisInputLocation;
import sootup.java.core.JavaIdentifierFactory;
import sootup.java.core.JavaProject;
import sootup.java.core.JavaSootClass;
import sootup.java.core.JavaSootMethod;
import sootup.java.core.language.JavaLanguage;
import sootup.java.core.views.JavaView;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Main03 {

    static JavaProject getJavaProject() {
        String path = "src/main/resources/test-demo/target/classes"; // 一定要到classes文件夹中
        AnalysisInputLocation<JavaSootClass> inputLocation = new JavaClassPathAnalysisInputLocation(path);
        JavaLanguage javaLanguage = new JavaLanguage(8);
        return JavaProject.builder(javaLanguage)
                .addInputLocation(inputLocation)
                .addInputLocation(new JavaClassPathAnalysisInputLocation(System.getProperty("java.home") + "/lib/rt.jar"))
                .build();
    }

    public static void main(String[] args) {
        JavaProject javaProject = getJavaProject();
        JavaView javaView = javaProject.createFullView();

        ClassType classType = javaProject.getIdentifierFactory().getClassType("example.SootTest");

        JavaSootClass javaSootClass = javaView.getClass(classType).get();
        List<? extends JavaSootMethod> printFizzBuzz = javaSootClass.getMethods().stream().filter(e -> e.getName().equals("sout")).collect(Collectors.toList());
        assert printFizzBuzz.size() > 0;

        MethodSignature signature = printFizzBuzz.get(0).getSignature();
        System.out.println(signature);

//        MethodSignature entryMethodNode = JavaIdentifierFactory.getInstance().getMethodSignature(classType,
//                JavaIdentifierFactory.getInstance().getMethodSubSignature("printFizzBuzz", VoidType.getInstance(), Collections.singletonList(PrimitiveType.IntType.getInstance()))
//        );
//
        CallGraphAlgorithm algorithm = new ClassHierarchyAnalysisAlgorithm(javaView, javaView.getTypeHierarchy());
        CallGraph callGraph = algorithm.initialize(Collections.singletonList(signature));
        System.out.println(callGraph);
        callGraph.callsFrom(signature).forEach(System.out::println);

    }
}
