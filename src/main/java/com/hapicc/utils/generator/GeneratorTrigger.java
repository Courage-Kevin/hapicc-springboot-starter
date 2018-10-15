package com.hapicc.utils.generator;

import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;

public class GeneratorTrigger {

    public static void main(String[] args) {
        try {
            GeneratorTrigger trigger = new GeneratorTrigger();
            trigger.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute() throws Exception {

        boolean overwrite = true;
        List<String> warnings = new ArrayList<>();

        // MyBatis Generator 配置文件
        File configFile = new File("src/main/resources/conf/generator/generatorConfig.xml");

        ConfigurationParser parser = new ConfigurationParser(warnings);
        Configuration configuration = parser.parseConfiguration(configFile);
        DefaultShellCallback shellCallback = new DefaultShellCallback(overwrite);
        MyBatisGenerator generator = new MyBatisGenerator(configuration, shellCallback, warnings);
        generator.generate(null);
    }
}
