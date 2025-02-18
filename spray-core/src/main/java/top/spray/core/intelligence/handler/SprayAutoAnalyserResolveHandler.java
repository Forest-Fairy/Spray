package top.spray.core.intelligence.handler;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public interface SprayAutoAnalyserResolveHandler {
    boolean canHandle(Element typeElement);

    void handle(Element typeElement, RoundEnvironment roundEnvironment, Messager messager,
                JavacTrees javacTrees, TreeMaker treeMaker, Names names);
}
