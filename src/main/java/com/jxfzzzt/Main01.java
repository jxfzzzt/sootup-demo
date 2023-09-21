package com.jxfzzzt;

import lombok.extern.slf4j.Slf4j;
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.model.SootMethod;
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
import java.util.List;

@Slf4j
public class Main01 {
    public static void main(String[] args) {
        String path = "src/main/resources/test-demo/target/classes"; // 一定要到classes文件夹中
        // 添加classpath路径，已进行后续sootup的分析
        AnalysisInputLocation<JavaSootClass> inputLocation = new JavaClassPathAnalysisInputLocation(path);
        JavaLanguage javaLanguage = new JavaLanguage(8);
        JavaProject javaProject = JavaProject.builder(javaLanguage).addInputLocation(inputLocation).build();
        // 创建一个全局的视图
        JavaView fullView = javaProject.createFullView();
        log.info("fullView: {}", fullView);

        // 获得class的类型签名
        ClassType classType = javaProject.getIdentifierFactory().getClassType("example.test.Test2");

        //根据class签名取出类对象
        JavaSootClass javaSootClass = fullView.getClass(classType).get();
        System.out.println(javaSootClass);

        // 获得方法签名
        MethodSignature methodSignature = javaProject.getIdentifierFactory().getMethodSignature(
                classType,
                JavaIdentifierFactory.getInstance().getMethodSubSignature("test2", VoidType.getInstance(), Collections.EMPTY_LIST));
        // 从class中取出对应的方法
        SootMethod sootMethod = fullView.getMethod(methodSignature).get();

        // 打印方法
        System.out.println(sootMethod);

        // 打印方法的Statements
        List<Stmt> stmts = sootMethod.getBody().getStmts();
        System.out.println(stmts);

    }
}

