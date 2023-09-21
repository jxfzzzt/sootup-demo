package com.jxfzzzt;

import lombok.extern.slf4j.Slf4j;
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.core.typehierarchy.ViewTypeHierarchy;
import sootup.core.types.ClassType;
import sootup.java.bytecode.inputlocation.JavaClassPathAnalysisInputLocation;
import sootup.java.core.JavaProject;
import sootup.java.core.JavaSootClass;
import sootup.java.core.language.JavaLanguage;
import sootup.java.core.views.JavaView;

import java.util.List;
import java.util.Set;

@Slf4j
public class Main02 {

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
        System.out.println(javaView);

        ClassType classTypeA = javaProject.getIdentifierFactory().getClassType("example.hierarchy.A");
        ClassType classTypeD = javaProject.getIdentifierFactory().getClassType("example.hierarchy.D");
        // 求解类之间的继承关系
        ViewTypeHierarchy typeHierarchy = new ViewTypeHierarchy(javaView);
        Set<ClassType> subclasses = typeHierarchy.subclassesOf(classTypeA);
        System.out.println("Class A's Sub Classes: " + subclasses);

        Set<ClassType> directSubClasses = typeHierarchy.directSubtypesOf(classTypeA);
        System.out.println("Class A's Direct Sub Classes: " + directSubClasses);

        List<ClassType> superClasses = typeHierarchy.superClassesOf(classTypeD);
        System.out.println("Class D's Super Classes: " + superClasses);

        ClassType directSuperClass = typeHierarchy.superClassOf(classTypeD);
        System.out.println("Class D's Direct Super Class: " + directSuperClass);


        // 求解接口之间的实现关系
        ClassType classTypeInterface1 = javaProject.getIdentifierFactory().getClassType("example.hierarchy.Interface1");
        System.out.println("Interface interface1 has been implemented by: " + typeHierarchy.implementersOf(classTypeInterface1));

        ClassType classTypeM = javaProject.getIdentifierFactory().getClassType("example.hierarchy.M");
        System.out.println("Class M's Implements: " + typeHierarchy.implementedInterfacesOf(classTypeM));
    }
}
