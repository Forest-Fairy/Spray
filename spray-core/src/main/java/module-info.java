module top.spray.core {
    // 导入需要引入的包
//    requires java.base;

    // interval
    requires java.compiler;
    requires jdk.unsupported;
    requires org.jetbrains.annotations;
    requires java.sql;

    // project
    requires top.spray.common;

    // third parties
    requires org.slf4j;
    requires cn.hutool;
    requires com.alibaba.fastjson2;
    requires org.apache.commons.lang3;
    requires com.google.common;
    requires testcontainers;

}