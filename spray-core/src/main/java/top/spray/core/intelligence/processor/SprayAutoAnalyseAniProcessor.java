package top.spray.core.intelligence.processor;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;
import top.spray.core.intelligence.annotation.SprayClassInfoAutoAnalyse;
import top.spray.core.intelligence.handler.SprayAutoAnalyserResolveHandler;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.*;

/**
 * TODO learn something about compiling annotation processor
 */
@SupportedAnnotationTypes("top.spray.core.intelligence.annotation.SprayClassInfoAutoAnalyse")
public class SprayAutoAnalyseAniProcessor extends AbstractProcessor {
    private static int round; // 用于标识注解处理的round
    // 主要是输出信息
    private Messager messager;
    private JavacTrees javacTrees;

    private TreeMaker treeMaker;
    private Names names;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.javacTrees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment)processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE, SprayAutoAnalyseAniProcessor.class.getSimpleName() + " round " + (++round));

        for (TypeElement annotation : annotations) {
            // 通过lambda表达式，处理被注解标记的每个元素
            roundEnv.getElementsAnnotatedWith(annotation).forEach(element -> {
                // 获取value的值
                SprayClassInfoAutoAnalyse autoAnalyse = element.getAnnotation(SprayClassInfoAutoAnalyse.class);
                String description = autoAnalyse.description();
                String version = autoAnalyse.version();

                // 修改语法树节点：直接修改init，为字段赋默认初始值
                JCTree.JCVariableDecl jcVariableDecl = (JCTree.JCVariableDecl) javacTrees.getTree(element);
                if (!jcVariableDecl.getModifiers().getFlags().contains(Modifier.FINAL) && jcVariableDecl.vartype.toString().equals("String")) {
                    messager.printMessage(Diagnostic.Kind.NOTE, "原始的字段信息: " + jcVariableDecl.toString());
                    jcVariableDecl.init = treeMaker.Literal(value);
                    messager.printMessage(Diagnostic.Kind.NOTE, "修改后的字段信息: " + jcVariableDecl.toString());
                } else {
                    messager.printMessage(Diagnostic.Kind.ERROR, "当前字段: " + jcVariableDecl.toString() + "\n@Value注解只能作用于非final的String字段!");
                }
            });
        }
        return roundEnv.processingOver();
    }

//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        // 拿到被注解标注的所有的类
//        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(SprayClassInfoAutoAnalyse.class);
//        elementsAnnotatedWith.forEach(element -> {
//            Iterator<SprayAutoAnalyserResolveHandler> iterator = ServiceLoader.load(SprayAutoAnalyserResolveHandler.class).iterator();
//            while (iterator.hasNext()) {
//                SprayAutoAnalyserResolveHandler sprayAutoAnalyserResolveHandler = iterator.next();
//                if (sprayAutoAnalyserResolveHandler.canHandle(element)){
//                    sprayAutoAnalyserResolveHandler.handle(element, roundEnv, messager, javacTrees, treeMaker, names);
//                    break;
//                }
//            }
//
//            // 得到类的抽象树结构
//            JCTree tree = javacTrees.getTree(element);
//            // 遍历类，对类进行修改
//            tree.accept(new TreeTranslator() {
//                            @Override
//                            public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
//                                List<JCTree.JCVariableDecl> jcVariableDeclList = List.nil();
//                                // 在抽象树中找出所有的变量
//                                for (JCTree jcTree : jcClassDecl.defs) {
//                                    if (jcTree.getKind().equals(Tree.Kind.VARIABLE)) {
//                                        JCTree.JCVariableDecl jcVariableDecl = (JCTree.JCVariableDecl) jcTree;
//                                        jcVariableDeclList = jcVariableDeclList.append(jcVariableDecl);
//                                    }
//                                }
//
//                                // 对于变量进行生成方法的操作
//                                for (JCTree.JCVariableDecl jcVariableDecl : jcVariableDeclList) {
//                                    messager.printMessage(Diagnostic.Kind.NOTE, jcVariableDecl.getName() + " has been processed");
//                                    jcClassDecl.defs = jcClassDecl.defs.prepend(makeSetterMethodDecl(jcVariableDecl));
//
//                                    jcClassDecl.defs = jcClassDecl.defs.prepend(makeGetterMethodDecl(jcVariableDecl));
//                                }
//                            }
//                        });
//        // 生成返回对象
//        JCTree.JCExpression methodType = treeMaker.Type(new Type.JCVoidType());
//
//        return treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC), getNewSetterMethodName(jcVariableDecl.getName()), methodType, List.nil(), parameters, List.nil(), block, null);
//    }
//    /**
//     * 生成 getter 方法
//     * @param jcVariableDecl
//     * @return
//     */
//    private JCTree.JCMethodDecl makeGetterMethodDecl(JCTree.JCVariableDecl jcVariableDecl){
//        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
//        // 生成表达式
//        JCTree.JCReturn aReturn = treeMaker.Return(treeMaker.Ident(jcVariableDecl.getName()));
//        statements.append(aReturn);
//        JCTree.JCBlock block = treeMaker.Block(0, statements.toList());
//        // 无入参
//        // 生成返回对象
//        JCTree.JCExpression returnType = treeMaker.Type(jcVariableDecl.getType().type);
//        return treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC), getNewGetterMethodName(jcVariableDecl.getName()), returnType, List.nil(), List.nil(), List.nil(), block, null);
//    }
//    /**
//     * 拼装Setter方法名称字符串
//     * @param name
//     * @return
//     */
//    private Name getNewSetterMethodName(Name name) {
//        String s = name.toString();
//        return names.fromString("set" + s.substring(0,1).toUpperCase() + s.substring(1, name.length()));
//    }
//    /**
//     * 拼装 Getter 方法名称的字符串
//     * @param name
//     * @return
//     */
//    private Name getNewGetterMethodName(Name name) {
//        String s = name.toString();
//        return names.fromString("get" + s.substring(0,1).toUpperCase() + s.substring(1, name.length()));
//    }
//    /**
//     * 生成表达式
//     * @param lhs
//     * @param rhs
//     * @return
//     */
//    private JCTree.JCExpressionStatement makeAssignment(JCTree.JCExpression lhs, JCTree.JCExpression rhs) {
//        return treeMaker.Exec(
//                treeMaker.Assign(lhs, rhs)
//        );
//    }
}