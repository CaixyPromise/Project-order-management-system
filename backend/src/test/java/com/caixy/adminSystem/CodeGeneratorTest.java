package com.caixy.adminSystem;

import cn.hutool.core.io.FileUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Collectors;


/**
 * 代码生成器
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.CodeGeneratorTest
 * @since 2024-06-04 21:26
 **/
public class CodeGeneratorTest
{
    @Test
    void testString()
    {
        String packageName = "com.caixy.adminSystem";
        String projectPath = System.getProperty("user.dir");

        String outputRootPath = String.format("%s/src/main/java/%s", projectPath,
                packageName.replaceAll("\\.", Matcher.quoteReplacement(File.separator)));
        System.out.println(
                outputRootPath
        );
    }

    /**
     * 用法：修改生成参数和生成路径，注释掉不需要的生成逻辑，然后运行即可
     *
     * @throws TemplateException
     * @throws IOException
     */
    @Test
    void start() throws TemplateException, IOException
    {
        // 指定生成参数
        String packageName = "com.caixy.adminSystem";
        String modelPackageName = "category";   // 实体类软件包名称
        String modelDesc = "订单分类";
        String lowerModelName = "orderCategory";
        String modelName = "OrderCategory";
        String outputPathName = "codeGenerator";

        // 封装生成参数
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("modelPackageName", modelPackageName);
        dataModel.put("packageName", packageName);
        dataModel.put("dataName", modelDesc);
        dataModel.put("dataKey", lowerModelName);
        dataModel.put("upperDataKey", modelName);

        // 生成路径默认值
        String projectPath = System.getProperty("user.dir");
        // 参考路径，可以自己调整下面的 outputPath
        String inputPath;
        String outputPath;
        String outputRootPath = String.format("%s/%s", projectPath,outputPathName
                );
        String inputRootPath = projectPath + File.separator;

        // 1、生成 Controller
        // 指定生成路径
        inputPath = inputRootPath + "src/main/resources/templates/TemplateController.java.ftl";
        outputPath = String.format("%s/controller/%sController.java", outputRootPath,
                modelName);
        // 生成
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Controller 成功，文件路径：" + outputPath);

        // 2、生成 Service 接口和实现类
        // 生成 Service 接口
        inputPath = inputRootPath + "src/main/resources/templates/TemplateService.java.ftl";
        outputPath = String.format("%s/service/%sService.java", outputRootPath, modelName);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Service 接口成功，文件路径：" + outputPath);
        // 生成 Service 实现类
        inputPath = inputRootPath + "src/main/resources/templates/TemplateServiceImpl.java.ftl";
        outputPath = String.format("%s/service/impl/%sServiceImpl.java", outputRootPath, modelName);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Service 实现类成功，文件路径：" + outputPath);

        // 3、生成数据模型封装类（包括 DTO 和 VO）
        // 生成 DTO
        inputPath = inputRootPath + "src/main/resources/templates/model/TemplateAddRequest.java.ftl";
        outputPath = String.format("%s/model/dto/%s/%sAddRequest.java", outputRootPath,modelPackageName,
                modelName);
        doGenerate(inputPath, outputPath, dataModel);
        inputPath = inputRootPath + "src/main/resources/templates/model/TemplateQueryRequest.java.ftl";
        outputPath = String.format("%s/model/dto/%s/%sQueryRequest.java", outputRootPath, modelPackageName,
                modelName);
        doGenerate(inputPath, outputPath, dataModel);
        inputPath = inputRootPath + "src/main/resources/templates/model/TemplateEditRequest.java.ftl";
        outputPath = String.format("%s/model/dto/%s/%sEditRequest.java", outputRootPath, modelPackageName,
                modelName);
        doGenerate(inputPath, outputPath, dataModel);
        inputPath = inputRootPath + "src/main/resources/templates/model/TemplateUpdateRequest.java.ftl";
        outputPath = String.format("%s/model/dto/%s/%sUpdateRequest.java", outputRootPath,modelPackageName,
                modelName);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 DTO 成功，文件路径：" + outputPath);
        // 生成 VO
        inputPath = inputRootPath + "src/main/resources/templates/model/TemplateVO.java.ftl";
        outputPath = String.format("%s/model/vo/%s/%sVO.java", outputRootPath,modelPackageName, modelName);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 VO 成功，文件路径：" + outputPath);
    }

    /**
     * 生成文件
     *
     * @param inputPath  模板文件输入路径
     * @param outputPath 输出路径
     * @param model      数据模型
     * @throws IOException
     * @throws TemplateException
     */
    public void doGenerate(String inputPath, String outputPath, Object model) throws IOException, TemplateException
    {
        // new 出 Configuration 对象，参数为 FreeMarker 版本号
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);

        // 指定模板文件所在的路径
        File templateDir = new File(inputPath).getParentFile();
        configuration.setDirectoryForTemplateLoading(templateDir);

        // 设置模板文件使用的字符集
        configuration.setDefaultEncoding("utf-8");

        // 创建模板对象，加载指定模板
        String templateName = new File(inputPath).getName();
        Template template = configuration.getTemplate(templateName);

        // 文件不存在则创建文件和父目录
        if (!FileUtil.exist(outputPath))
        {
            FileUtil.touch(outputPath);
        }

        // 生成
        Writer out = new FileWriter(outputPath);
        template.process(model, out);

        // 生成文件后别忘了关闭哦
        out.close();
    }
}
