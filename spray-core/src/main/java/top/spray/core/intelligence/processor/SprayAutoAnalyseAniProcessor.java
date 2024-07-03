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
import top.spray.core.intelligence.annotation.SprayAutoAnalyse;
import top.spray.core.intelligence.handler.SprayAutoAnalyserResolveHandler;
import top.spray.core.util.SprayServiceUtil;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.*;

import static jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle.block;
import static jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle.parameters;

/**
 * TODO learn something about compiling annotation processor
 */
@SupportedAnnotationTypes("top.spray.core.intelligence.annotation.SprayAutoAnalyse")
public class SprayAutoAnalyseAniProcessor extends AbstractProcessor {
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
        // 拿到被注解标注的所有的类
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(SprayAutoAnalyse.class);
        elementsAnnotatedWith.forEach(element -> {
            Iterator<SprayAutoAnalyserResolveHandler> iterator = ServiceLoader.load(SprayAutoAnalyserResolveHandler.class).iterator();
            while (iterator.hasNext()) {
                SprayAutoAnalyserResolveHandler sprayAutoAnalyserResolveHandler = iterator.next();
                if (sprayAutoAnalyserResolveHandler.canHandle(element)){
                    sprayAutoAnalyserResolveHandler.handle(element, roundEnv, messager, javacTrees, treeMaker, names);
                    break;
                }
            }

            // 得到类的抽象树结构
            JCTree tree = javacTrees.getTree(element);
            // 遍历类，对类进行修改
            tree.accept(new TreeTranslator() {
                            @Override
                            public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                                List<JCTree.JCVariableDecl> jcVariableDeclList = List.nil();
                                // 在抽象树中找出所有的变量
                                for (JCTree jcTree : jcClassDecl.defs) {
                                    if (jcTree.getKind().equals(Tree.Kind.VARIABLE)) {
                                        JCTree.JCVariableDecl jcVariableDecl = (JCTree.JCVariableDecl) jcTree;
                                        jcVariableDeclList = jcVariableDeclList.append(jcVariableDecl);
                                    }
                                }

                                // 对于变量进行生成方法的操作
                                for (JCTree.JCVariableDecl jcVariableDecl : jcVariableDeclList) {
                                    messager.printMessage(Diagnostic.Kind.NOTE, jcVariableDecl.getName() + " has been processed");
                                    jcClassDecl.defs = jcClassDecl.defs.prepend(makeSetterMethodDecl(jcVariableDecl));

                                    jcClassDecl.defs = jcClassDecl.defs.prepend(makeGetterMethodDecl(jcVariableDecl));
                                }
                            }
                        });
        // 生成返回对象
        JCTree.JCExpression methodType = treeMaker.Type(new Type.JCVoidType());

        return treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC), getNewSetterMethodName(jcVariableDecl.getName()), methodType, List.nil(), parameters, List.nil(), block, null);
    }
    /**
     * 生成 getter 方法
     * @param jcVariableDecl
     * @return
     */
    private JCTree.JCMethodDecl makeGetterMethodDecl(JCTree.JCVariableDecl jcVariableDecl){
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        // 生成表达式
        JCTree.JCReturn aReturn = treeMaker.Return(treeMaker.Ident(jcVariableDecl.getName()));
        statements.append(aReturn);
        JCTree.JCBlock block = treeMaker.Block(0, statements.toList());
        // 无入参
        // 生成返回对象
        JCTree.JCExpression returnType = treeMaker.Type(jcVariableDecl.getType().type);
        return treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC), getNewGetterMethodName(jcVariableDecl.getName()), returnType, List.nil(), List.nil(), List.nil(), block, null);
    }
    /**
     * 拼装Setter方法名称字符串
     * @param name
     * @return
     */
    private Name getNewSetterMethodName(Name name) {
        String s = name.toString();
        return names.fromString("set" + s.substring(0,1).toUpperCase() + s.substring(1, name.length()));
    }
    /**
     * 拼装 Getter 方法名称的字符串
     * @param name
     * @return
     */
    private Name getNewGetterMethodName(Name name) {
        String s = name.toString();
        return names.fromString("get" + s.substring(0,1).toUpperCase() + s.substring(1, name.length()));
    }
    /**
     * 生成表达式
     * @param lhs
     * @param rhs
     * @return
     */
    private JCTree.JCExpressionStatement makeAssignment(JCTree.JCExpression lhs, JCTree.JCExpression rhs) {
        return treeMaker.Exec(
                treeMaker.Assign(lhs, rhs)
        );
    }
}