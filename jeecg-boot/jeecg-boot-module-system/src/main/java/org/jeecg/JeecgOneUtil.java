package org.jeecg;

import com.google.common.base.CaseFormat;
import org.jeecg.codegenerate.config.AppConfig;
import org.jeecg.codegenerate.database.DbReadTableUtil;
import org.jeecg.codegenerate.generate.impl.CodeGenerateOne;
import org.jeecg.codegenerate.generate.pojo.TableVo;
import org.jeecg.codegenerate.generate.util.StrUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

/**
 * @author dafei
 * @version 0.1
 * @date 2020/9/7 23:24
 */
public class JeecgOneUtil {

    /*
     * 查询表注释
     * SELECT TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES  WHERE TABLE_NAME = '表名' AND TABLE_SCHEMA = '库名'
     * */

    public static String getTableComment(String tableName) {
        Statement statement = DbReadTableUtil.getStatement();
        String sql = MessageFormat.format("SELECT TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES  WHERE TABLE_NAME = {0} AND TABLE_SCHEMA = {1}", StrUtil.wrapWithSingleQuote(tableName), StrUtil.wrapWithSingleQuote(AppConfig.DATABASE_NAME));
        ResultSet rs = null;
        String desc = "";
        try {
            rs = statement.executeQuery(sql);

            if (rs.next()) {
                desc = rs.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return desc;
    }


    public static void main(String[] args) {
        // 批量生成多个表格diamante
        String tableNames[] = {"feed_store", "feed_share", "feed_reply", "feed_like", "feed_favorite"};
        String moduleName = "feed"; // 模块名



        for (String tn : tableNames) {
            genOneCode(tn, moduleName);
        }
    }

    private static void genOneCode(String tableName, String moduleName) {
        // 默认表名下划线转成大写开头的驼峰作为类名前缀
        String entityName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName);

        String desc = getTableComment(tableName);

        System.out.println("----jeecg--------- Code------------- Generation -----[单表模型]------- 生成中。。。");
        TableVo var1 = new TableVo();
        var1.setTableName(tableName);
        // var1.setPrimaryKeyPolicy("uuid");
        var1.setEntityPackage(moduleName);  // 子模块名, 基础包名在配置文件jeecg_config.properties#bussi_package配置
        var1.setEntityName(entityName);
        var1.setFtlDescription(desc);

        try {
            (new CodeGenerateOne(var1)).generateCodeFile();
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new RuntimeException(var3);
        }

        System.out.println("----jeecg--------- Code------------- Generation -----[单表模型]------- 生成完成。。。");
    }

}
