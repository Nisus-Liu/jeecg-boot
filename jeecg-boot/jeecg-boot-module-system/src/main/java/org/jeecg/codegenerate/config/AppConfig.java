package org.jeecg.codegenerate.config;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ResourceBundle;

@Component
public class AppConfig {

    @Autowired
    private Environment env;

    private static final ResourceBundle JEECG_DATABASE_BUNDLE = ResourceBundle.getBundle("jeecg/jeecg_database");
    private static final ResourceBundle JEECG_CONFIG_BUNDLE = ResourceBundle.getBundle("jeecg/jeecg_config");
    public static String DB_TYPE = "mysql"; // df: 数据库类型
    public static String DRIVER_NAME = "com.mysql.jdbc.Driver";
    public static String URL = "jdbc:mysql://localhost:3306/jeecg-boot?useUnicode=true&characterEncoding=UTF-8";
    public static String USERNAME = "root";
    public static String PASSWORD = "root";
    public static String DATABASE_NAME = "polaris"; // df: 库名
    public static String g = "c:/workspace/jeecg";
    public static String h = "org.jeecg";
    public static String i = "src";
    public static String j = "WebRoot";
    public static String k = "template/code-template/";
    public static boolean l = true;
    public static String m;
    public static String n = "4";
    public static String o = "3";
    public static String p;
    public static String q = "1";
    public static boolean TINYINT_TO_BOOLEAN = true;

    public AppConfig() {
    }

    private void n() {
    }

    @PostConstruct
    public void readConfig() {  // df: 这是为了在Spring环境下, 自动复用项目依赖的数据源. 而gui或工具方法执行, 用的则是 jeecg/jeecg_database.properties 里的配置
        DRIVER_NAME = env.getProperty("spring.datasource.dynamic.datasource.master.driver-class-name");
        URL = env.getProperty("spring.datasource.dynamic.datasource.master.url");
        USERNAME = env.getProperty("spring.datasource.dynamic.datasource.master.username");
        PASSWORD = env.getProperty("spring.datasource.dynamic.datasource.master.password");
    }


    public static final String getDriverName() {
        return JEECG_DATABASE_BUNDLE.getString("diver_name");
    }

    public static final String getUrl() {
        return JEECG_DATABASE_BUNDLE.getString("url");
    }

    public static final String getUsername() {
        return JEECG_DATABASE_BUNDLE.getString("username");
    }

    public static final String getPassword() {
        return JEECG_DATABASE_BUNDLE.getString("password");
    }

    public static final String getDatabaseName() {
        return JEECG_DATABASE_BUNDLE.getString("database_name");
    }

    public static final boolean f() {
        String var0 = JEECG_CONFIG_BUNDLE.getString("db_filed_convert");
        return !var0.toString().equals("false");
    }

    private static String o() {
        return JEECG_CONFIG_BUNDLE.getString("bussi_package");
    }

    private static String p() {
        return JEECG_CONFIG_BUNDLE.getString("templatepath");
    }

    public static final String g() {
        return JEECG_CONFIG_BUNDLE.getString("source_root_package");
    }

    public static final String h() {
        return JEECG_CONFIG_BUNDLE.getString("webroot_package");
    }

    public static final String i() {
        return JEECG_CONFIG_BUNDLE.getString("db_table_id");
    }

    public static final String j() {
        return JEECG_CONFIG_BUNDLE.getString("page_filter_fields");
    }

    public static final String k() {
        return JEECG_CONFIG_BUNDLE.getString("page_search_filed_num");
    }

    public static final String l() {
        return JEECG_CONFIG_BUNDLE.getString("page_field_required_num");
    }

    public static String m() {
        String var0 = JEECG_CONFIG_BUNDLE.getString("project_path");
        if (var0 != null && !"".equals(var0)) {
            g = var0;
        }

        return g;
    }

    public static void getDriverName(String var0) {
        g = var0;
    }

    public static void getUrl(String var0) {
        k = var0;
    }

    // Add by df
    public static boolean getBool(String key) {
        String val = JEECG_CONFIG_BUNDLE.getString(key);

        // 空值认为是 false , 'false' == false, 其他都是 true
        if (StrUtil.isBlank(val)) {
            return false;
        }

        return !"false".equals(val); // 除了 false , 其他非空字符都是认为true
    }

    static {
        DRIVER_NAME = getDriverName();
        URL = getUrl();
        USERNAME = getUsername();
        PASSWORD = getPassword();
        DATABASE_NAME = getDatabaseName();
        i = g();
        j = h();
        h = o();
        k = p();
        g = m();
        m = i();
        l = f();
        p = j();
        o = k();
        if (URL.indexOf("mysql") < 0 && URL.indexOf("MYSQL") < 0) {
            if (URL.indexOf("oracle") < 0 && URL.indexOf("ORACLE") < 0) {
                if (URL.indexOf("postgresql") < 0 && URL.indexOf("POSTGRESQL") < 0) {
                    if (URL.indexOf("sqlserver") >= 0 || URL.indexOf("sqlserver") >= 0) {
                        DB_TYPE = "sqlserver";
                    }
                } else {
                    DB_TYPE = "postgresql";
                }
            } else {
                DB_TYPE = "oracle";
            }
        } else {
            DB_TYPE = "mysql";
        }

        i = i.replace(".", "/");
        j = j.replace(".", "/");

        // df 加的配置
        TINYINT_TO_BOOLEAN = getBool("tinyint_to_boolean");
    }
}
