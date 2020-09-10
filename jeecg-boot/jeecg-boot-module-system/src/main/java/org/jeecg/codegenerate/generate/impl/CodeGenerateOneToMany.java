package org.jeecg.codegenerate.generate.impl;

import org.jeecg.codegenerate.database.DbReadTableUtil;
import org.jeecg.codegenerate.generate.IGenerate;
import org.jeecg.codegenerate.generate.impl.a.a;
import org.jeecg.codegenerate.generate.pojo.ColumnVo;
import org.jeecg.codegenerate.generate.pojo.onetomany.MainTableVo;
import org.jeecg.codegenerate.generate.pojo.onetomany.SubTableVo;
import org.jeecg.codegenerate.generate.util.NonceUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CodeGenerateOneToMany extends a implements IGenerate {
    private static final Logger d = LoggerFactory.getLogger(CodeGenerateOneToMany.class);
    private static String e;
    public static String a = "A";
    public static String b = "B";
    private MainTableVo f;
    private List<ColumnVo> g;
    private List<ColumnVo> h;
    private List<SubTableVo> i;
    private static DbReadTableUtil j = new DbReadTableUtil();

    public CodeGenerateOneToMany(MainTableVo mainTableVo, List<SubTableVo> subTables) {
        this.i = subTables;
        this.f = mainTableVo;
    }

    public CodeGenerateOneToMany(MainTableVo mainTableVo, List<ColumnVo> mainColums, List<ColumnVo> originalMainColumns, List<SubTableVo> subTables) {
        this.f = mainTableVo;
        this.g = mainColums;
        this.h = originalMainColumns;
        this.i = subTables;
    }

    public Map<String, Object> getCodeTemplateData() throws Exception {
        HashMap var1 = new HashMap();
        var1.put("bussiPackage", org.jeecg.codegenerate.config.AppConfig.h);
        var1.put("entityPackage", this.f.getEntityPackage());
        var1.put("entityName", this.f.getEntityName());
        var1.put("tableName", this.f.getTableName());
        var1.put("ftl_description", this.f.getFtlDescription());
        var1.put("primaryKeyField", org.jeecg.codegenerate.config.AppConfig.m);
        if (this.f.getFieldRequiredNum() == null) {
            this.f.setFieldRequiredNum(StringUtils.isNotEmpty(org.jeecg.codegenerate.config.AppConfig.n) ? Integer.parseInt(org.jeecg.codegenerate.config.AppConfig.n) : -1);
        }

        if (this.f.getSearchFieldNum() == null) {
            this.f.setSearchFieldNum(StringUtils.isNotEmpty(org.jeecg.codegenerate.config.AppConfig.o) ? Integer.parseInt(org.jeecg.codegenerate.config.AppConfig.o) : -1);
        }

        if (this.f.getFieldRowNum() == null) {
            this.f.setFieldRowNum(Integer.parseInt(org.jeecg.codegenerate.config.AppConfig.q));
        }

        var1.put("tableVo", this.f);

        try {
            DbReadTableUtil var10001;
            if (this.g == null || this.g.size() == 0) {
                var10001 = j;
                this.g = DbReadTableUtil.getColumns(this.f.getTableName());
            }

            if (this.h == null || this.h.size() == 0) {
                var10001 = j;
                this.h = DbReadTableUtil.getOriginalColumns(this.f.getTableName());
            }

            var1.put("columns", this.g);
            var1.put("originalColumns", this.h);
            Iterator var2 = this.h.iterator();

            while(var2.hasNext()) {
                ColumnVo var3 = (ColumnVo)var2.next();
                if (var3.getFieldName().toLowerCase().equals(org.jeecg.codegenerate.config.AppConfig.m.toLowerCase())) {
                    var1.put("primaryKeyPolicy", var3.getFieldType());
                }
            }

            var2 = this.i.iterator();

            while(var2.hasNext()) {
                SubTableVo var12 = (SubTableVo)var2.next();
                List var4;
                DbReadTableUtil var10000;
                if (var12.getColums() == null || var12.getColums().size() == 0) {
                    var10000 = j;
                    var4 = DbReadTableUtil.getColumns(var12.getTableName());
                    var12.setColums(var4);
                }

                if (var12.getOriginalColumns() == null || var12.getOriginalColumns().size() == 0) {
                    var10000 = j;
                    var4 = DbReadTableUtil.getOriginalColumns(var12.getTableName());
                    var12.setOriginalColumns(var4);
                }

                String[] var13 = var12.getForeignKeys();
                ArrayList var5 = new ArrayList();
                String[] var6 = var13;
                int var7 = var13.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    String var9 = var6[var8];
                    var10001 = j;
                    var5.add(DbReadTableUtil.d(var9));
                }

                var12.setForeignKeys((String[])var5.toArray(new String[0]));
                var12.setOriginalForeignKeys(var13);
            }

            var1.put("subTables", this.i);
        } catch (Exception var10) {
            throw var10;
        }

        long var11 = NonceUtils.c() + NonceUtils.g();
        var1.put("serialVersionUID", String.valueOf(var11));
        d.info("code template data: " + var1.toString());
        return var1;
    }

    public void generateCodeFile() throws Exception {
        d.info("----jeecg---Code----Generation----[一对多模型:" + this.f.getTableName() + "]------- 生成中。。。");
        String var1 = org.jeecg.codegenerate.config.AppConfig.g;
        Map var2 = this.getCodeTemplateData();
        String var3 = org.jeecg.codegenerate.config.AppConfig.k;
        if (a(var3, "/").equals("jeecg/code-template")) {
            var3 = "/" + a(var3, "/") + "/onetomany";
        }

        org.jeecg.codegenerate.generate.a.a var4 = new org.jeecg.codegenerate.generate.a.a(var3);
        this.a(var4, var1, var2);
        d.info("----jeecg----Code----Generation-----[一对多模型：" + this.f.getTableName() + "]------ 生成完成。。。");
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
}
