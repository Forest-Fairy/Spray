package top.spray.core.util;

import cn.hutool.core.io.FileUtil;
import top.spray.core.i18n.SprayUtf8s;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * a class for getting info from class code
 *  - className     0
 *  - classPackage  1
 *  - classContent  2
 */
public class SprayClassInfoUtil {
    public static final int CLASS_NAME = 0;
    public static final int CLASS_PACKAGE = 1;
    public static final int CLASS_CONTENT = 2;

    private static final Set<String> TOKENS = new HashSet<String>(){
        {
            add("class");
            add("interface");
            add("@interface");
            add("enum");
            add("record");
        }
    };
    public static String[] getClassInfo(String code) throws ClassNotFoundException {
        String[] lines = code.split("\n");
        String[] infos = new String[3];
        for (String line : lines) {
            if (infos[CLASS_PACKAGE] == null && line.startsWith("package ")) {
                // 包名
                infos[CLASS_PACKAGE] = line.substring("package ".length(), line.length() - 1).trim(); // 去除末尾分号和空白
                continue;
            }
            if (infos[CLASS_PACKAGE] != null &&
                    TOKENS.stream().anyMatch(token -> line.contains(String.format(" %s ", token)))) {
                // 类名
                String[] words = line.split(" ");
                for (int i = 0; i < words.length; i++) {
                    if (TOKENS.contains(words[i].trim())) {
                        infos[CLASS_NAME] = words[i + 1].trim();
                        infos[CLASS_CONTENT] = code;
                        return infos;
                    }
                }
                // 不管是否解析成功都打断
                break;
            }
        }
        throw new ClassNotFoundException("resolve failed, can't find the className");
    }

    public static File writeAsJavaFile(String baseDir, String fullClassName, String code) {
        if (baseDir == null) {
            baseDir = "";
        } else {
            if (! baseDir.endsWith("/")) {
                baseDir += "/";
            }
        }
        return FileUtil.writeBytes(code.getBytes(SprayUtf8s.Charset),
                new File(baseDir + fullClassName.replace(".", "/") + ".java"));
    }


}
