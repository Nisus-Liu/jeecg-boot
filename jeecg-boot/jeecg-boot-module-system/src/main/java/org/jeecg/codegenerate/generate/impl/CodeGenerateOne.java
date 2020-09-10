package org.jeecg.codegenerate.generate.impl;

import org.jeecg.codegenerate.database.DbReadTableUtil;
import org.jeecg.codegenerate.generate.IGenerate;
import org.jeecg.codegenerate.generate.impl.a.a;
import org.jeecg.codegenerate.generate.pojo.ColumnVo;
import org.jeecg.codegenerate.generate.pojo.TableVo;
import org.jeecg.codegenerate.generate.util.NonceUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CodeGenerateOne extends a implements IGenerate {
    private static final Logger a = LoggerFactory.getLogger(CodeGenerateOne.class);
    private TableVo tableVo;
    private List<ColumnVo> columns;
    private List<ColumnVo> originalColumns;

    public CodeGenerateOne(TableVo tableVo) {
        this.tableVo = tableVo;
    }

    public CodeGenerateOne(TableVo tableVo, List<ColumnVo> columns, List<ColumnVo> originalColumns) {
        this.tableVo = tableVo;
        this.columns = columns;
        this.originalColumns = originalColumns;
    }

    public Map<String, Object> getCodeTemplateData() throws Exception {
        HashMap var1 = new HashMap();
        var1.put("bussiPackage", org.jeecg.codegenerate.config.AppConfig.h);
        var1.put("entityPackage", this.tableVo.getEntityPackage());
        var1.put("entityName", this.tableVo.getEntityName());
        var1.put("tableName", this.tableVo.getTableName());
        var1.put("primaryKeyField", org.jeecg.codegenerate.config.AppConfig.m);
        if (this.tableVo.getFieldRequiredNum() == null) {
            this.tableVo.setFieldRequiredNum(StringUtils.isNotEmpty(org.jeecg.codegenerate.config.AppConfig.n) ? Integer.parseInt(org.jeecg.codegenerate.config.AppConfig.n) : -1);
        }

        if (this.tableVo.getSearchFieldNum() == null) {
            this.tableVo.setSearchFieldNum(StringUtils.isNotEmpty(org.jeecg.codegenerate.config.AppConfig.o) ? Integer.parseInt(org.jeecg.codegenerate.config.AppConfig.o) : -1);
        }

        if (this.tableVo.getFieldRowNum() == null) {
            this.tableVo.setFieldRowNum(Integer.parseInt(org.jeecg.codegenerate.config.AppConfig.q));
        }

        var1.put("tableVo", this.tableVo);

        try {
            if (this.columns == null || this.columns.size() == 0) {
                this.columns = DbReadTableUtil.getColumns(this.tableVo.getTableName());
            }

            var1.put("columns", this.columns);
            if (this.originalColumns == null || this.originalColumns.size() == 0) {
                this.originalColumns = DbReadTableUtil.getOriginalColumns(this.tableVo.getTableName());
            }

            var1.put("originalColumns", this.originalColumns);
            Iterator var2 = this.originalColumns.iterator();

            while(var2.hasNext()) {
                ColumnVo var3 = (ColumnVo)var2.next();
                if (var3.getFieldName().toLowerCase().equals(org.jeecg.codegenerate.config.AppConfig.m.toLowerCase())) {
                    var1.put("primaryKeyPolicy", var3.getFieldType());
                }
            }
        } catch (Exception var4) {
            throw var4;
        }

        long var5 = NonceUtils.c() + NonceUtils.g();
        var1.put("serialVersionUID", String.valueOf(var5));
        a.info("code template data: " + var1.toString());
        return var1;
    }

    public void generateCodeFile() throws Exception {
        a.info("----jeecg---Code----Generation----[单表模型:" + this.tableVo.getTableName() + "]------- 生成中。。。");
        String var1 = org.jeecg.codegenerate.config.AppConfig.g;
        Map var2 = this.getCodeTemplateData(); // df: 获取表字段等元信息
        String var3 = org.jeecg.codegenerate.config.AppConfig.k; // df: 模板
        if (a(var3, "/").equals("jeecg/code-template")) {
            var3 = "/" + a(var3, "/") + "/one";
        }

        org.jeecg.codegenerate.generate.a.a var4 = new org.jeecg.codegenerate.generate.a.a(var3);
        this.a(var4, var1, var2); // df: 生成代码逻辑
        a.info("----jeecg----Code----Generation-----[单表模型：" + this.tableVo.getTableName() + "]------ 生成完成。。。");
    }

    public void generateCodeFile(String projectPath, String templatePath) throws Exception {
        if (projectPath != null && !"".equals(projectPath)) {
            org.jeecg.codegenerate.config.AppConfig.getDriverName(projectPath);
        }

        if (templatePath != null && !"".equals(templatePath)) {
            org.jeecg.codegenerate.config.AppConfig.getUrl(templatePath);
        }

        this.generateCodeFile();
    }

    public static void main(String[] args) {
        System.out.println("----jeecg--------- Code------------- Generation -----[单表模型]------- 生成中。。。");
        TableVo var1 = new TableVo();
        var1.setTableName("demo");
        var1.setPrimaryKeyPolicy("uuid");
        var1.setEntityPackage("test");
        var1.setEntityName("JeecgDemo");
        var1.setFtlDescription("jeecg 测试demo");

        try {
            (new CodeGenerateOne(var1)).generateCodeFile();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        System.out.println("----jeecg--------- Code------------- Generation -----[单表模型]------- 生成完成。。。");
    }
}
