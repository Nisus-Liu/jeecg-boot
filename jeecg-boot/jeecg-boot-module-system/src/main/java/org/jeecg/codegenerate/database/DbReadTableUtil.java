package org.jeecg.codegenerate.database;

import org.apache.commons.lang.StringUtils;
import org.jeecg.codegenerate.config.AppConfig;
import org.jeecg.codegenerate.generate.pojo.ColumnVo;
import org.jeecg.codegenerate.generate.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class DbReadTableUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbReadTableUtil.class);
    private static Connection CONNECTION;
    private static Statement STATEMENT;

    public DbReadTableUtil() {
    }

    // public static void main(String[] args) throws SQLException {
    //     try {
    //         List var1 = getColumns("demo");
    //         Iterator var2 = var1.iterator();
    //
    //         while(var2.hasNext()) {
    //             ColumnVo var3 = (ColumnVo)var2.next();
    //             System.out.println(var3.getFieldName());
    //         }
    //     } catch (Exception var4) {
    //         var4.printStackTrace();
    //     }
    //
    //     PrintStream var10000 = System.out;
    //     new DbReadTableUtil();
    //     var10000.println(ArrayUtils.toString(getTableNames()));
    // }

    public static Statement getStatement() {
        if (STATEMENT==null) {
            try {
                Class.forName(AppConfig.DRIVER_NAME);
                CONNECTION = DriverManager.getConnection(AppConfig.URL, AppConfig.USERNAME, AppConfig.PASSWORD);
                STATEMENT = CONNECTION.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return STATEMENT;
    }

    public static List<String> getTableNames() throws SQLException {
        String var1 = null;
        ArrayList var2 = new ArrayList(0);

        try {
            Class.forName(AppConfig.DRIVER_NAME);
            CONNECTION = DriverManager.getConnection(AppConfig.URL, AppConfig.USERNAME, AppConfig.PASSWORD);
            STATEMENT = CONNECTION.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (AppConfig.DB_TYPE.equals("mysql")) {
                var1 = MessageFormat.format("select distinct table_name from information_schema.columns where table_schema = {0}", StrUtil.wrapWithSingleQuote(AppConfig.DATABASE_NAME));
            }

            if (AppConfig.DB_TYPE.equals("oracle")) {
                var1 = " select distinct colstable.table_name as  table_name from user_tab_cols colstable order by colstable.table_name";
            }

            if (AppConfig.DB_TYPE.equals("postgresql")) {
                var1 = "SELECT distinct c.relname AS  table_name FROM pg_class c";
            }

            if (AppConfig.DB_TYPE.equals("sqlserver")) {
                var1 = "select distinct c.name as  table_name from sys.objects c where c.type = 'U' ";
            }

            ResultSet var0 = STATEMENT.executeQuery(var1);

            while(var0.next()) {
                String var3 = var0.getString(1);
                var2.add(var3);
            }
        } catch (Exception var12) {
            var12.printStackTrace();
        } finally {
            try {
                if (STATEMENT != null) {
                    STATEMENT.close();
                    STATEMENT = null;
                    System.gc();
                }

                if (CONNECTION != null) {
                    CONNECTION.close();
                    CONNECTION = null;
                    System.gc();
                }
            } catch (SQLException var11) {
                throw var11;
            }

        }

        return var2;
    }

    public static List<ColumnVo> getColumns(String tableName) throws Exception {
        String var2 = null;
        ArrayList<ColumnVo> var3 = new ArrayList();

        ColumnVo columnVo;
        try {
            Class.forName(AppConfig.DRIVER_NAME);
            CONNECTION = DriverManager.getConnection(AppConfig.URL, AppConfig.USERNAME, AppConfig.PASSWORD);
            STATEMENT = CONNECTION.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (AppConfig.DB_TYPE.equals("mysql")) {
                var2 = MessageFormat.format("select column_name,data_type,column_comment,numeric_precision,numeric_scale,character_maximum_length,is_nullable nullable from information_schema.columns where table_name = {0} and table_schema = {1}", StrUtil.wrapWithSingleQuote(tableName.toUpperCase()), StrUtil.wrapWithSingleQuote(AppConfig.DATABASE_NAME));
            }

            if (AppConfig.DB_TYPE.equals("oracle")) {
                var2 = MessageFormat.format(" select colstable.column_name column_name, colstable.data_type data_type, commentstable.comments column_comment, colstable.Data_Precision column_precision, colstable.Data_Scale column_scale,colstable.Char_Length,colstable.nullable from user_tab_cols colstable  inner join user_col_comments commentstable  on colstable.column_name = commentstable.column_name  where colstable.table_name = commentstable.table_name  and colstable.table_name = {0}", StrUtil.wrapWithSingleQuote(tableName.toUpperCase()));
            }

            if (AppConfig.DB_TYPE.equals("postgresql")) {
                var2 = MessageFormat.format("SELECT a.attname AS  field,t.typname AS type,col_description(a.attrelid,a.attnum) as comment,null as column_precision,null as column_scale,null as Char_Length,a.attnotnull  FROM pg_class c,pg_attribute  a,pg_type t  WHERE c.relname = {0} and a.attnum > 0  and a.attrelid = c.oid and a.atttypid = t.oid  ORDER BY a.attnum ", StrUtil.wrapWithSingleQuote(tableName.toLowerCase()));
            }

            if (AppConfig.DB_TYPE.equals("sqlserver")) {
                var2 = MessageFormat.format("select distinct cast(a.name as varchar(50)) column_name,  cast(b.name as varchar(50)) data_type,  cast(e.value as varchar(200)) comment,  cast(ColumnProperty(a.object_id,a.Name,'''Precision''') as int) num_precision,  cast(ColumnProperty(a.object_id,a.Name,'''Scale''') as int) num_scale,  a.max_length,  (case when a.is_nullable=1 then '''y''' else '''n''' end) nullable,column_id   from sys.columns a left join sys.types b on a.user_type_id=b.user_type_id left join (select top 1 * from sys.objects where type = '''U''' and name ={0}  order by name) c on a.object_id=c.object_id left join sys.extended_properties e on e.major_id=c.object_id and e.minor_id=a.column_id and e.class=1 where c.name={0} order by a.column_id", StrUtil.wrapWithSingleQuote(tableName.toLowerCase()));
            }

            ResultSet var1 = STATEMENT.executeQuery(var2);
            var1.last();
            int var4 = var1.getRow();
            if (var4 <= 0) {
                throw new Exception("表`"+tableName+"`不存在或者表中没有字段");
            }

            columnVo = new ColumnVo();
            if (AppConfig.DB_FIELD_CONVERT) {
                columnVo.setFieldName(convertDbFieldName(var1.getString(1).toLowerCase()));
            } else {
                columnVo.setFieldName(var1.getString(1).toLowerCase());
            }

            columnVo.setFieldDbName(var1.getString(1).toUpperCase());
            columnVo.setFieldType(convertDbFieldName(var1.getString(2).toLowerCase()));
            columnVo.setFieldDbType(convertDbFieldName(var1.getString(2).toLowerCase()));
            columnVo.setPrecision(var1.getString(4));
            columnVo.setScale(var1.getString(5));
            columnVo.setCharmaxLength(var1.getString(6));
            columnVo.setNullable(StrUtil.normalizeBoolFlag(var1.getString(7)));
            setClassTypeAndOptionType(columnVo);
            columnVo.setFiledComment(StringUtils.isBlank(var1.getString(3)) ? columnVo.getFieldName() : var1.getString(3));
            LOGGER.debug("column.getFieldName() -------------" + columnVo.getFieldName());
            String[] var7 = new String[0];
            if (AppConfig.p != null) {
                var7 = AppConfig.p.toLowerCase().split(",");
            }

            if (!AppConfig.m.equals(columnVo.getFieldName()) && !org.jeecg.codegenerate.database.util.a.a(columnVo.getFieldDbName().toLowerCase(), var7)) {
                var3.add(columnVo);
            }

            while(var1.previous()) {
                ColumnVo var8 = new ColumnVo();
                if (AppConfig.DB_FIELD_CONVERT) {
                    var8.setFieldName(convertDbFieldName(var1.getString(1).toLowerCase()));
                } else {
                    var8.setFieldName(var1.getString(1).toLowerCase());
                }

                var8.setFieldDbName(var1.getString(1).toUpperCase());
                LOGGER.debug("columnt.getFieldName() -------------" + var8.getFieldName());
                if (!AppConfig.m.equals(var8.getFieldName()) && !org.jeecg.codegenerate.database.util.a.a(var8.getFieldDbName().toLowerCase(), var7)) {
                    var8.setFieldType(convertDbFieldName(var1.getString(2).toLowerCase()));
                    var8.setFieldDbType(convertDbFieldName(var1.getString(2).toLowerCase()));
                    LOGGER.debug("-----po.setFieldType------------" + var8.getFieldType());
                    var8.setPrecision(var1.getString(4));
                    var8.setScale(var1.getString(5));
                    var8.setCharmaxLength(var1.getString(6));
                    var8.setNullable(StrUtil.normalizeBoolFlag(var1.getString(7)));
                    setClassTypeAndOptionType(var8);
                    var8.setFiledComment(StringUtils.isBlank(var1.getString(3)) ? var8.getFieldName() : var1.getString(3));
                    var3.add(var8);
                }
            }

            LOGGER.debug("读取表成功");
        } catch (ClassNotFoundException var17) {
            throw var17;
        } catch (SQLException var18) {
            throw var18;
        } finally {
            try {
                if (STATEMENT != null) {
                    STATEMENT.close();
                    STATEMENT = null;
                    System.gc();
                }

                if (CONNECTION != null) {
                    CONNECTION.close();
                    CONNECTION = null;
                    System.gc();
                }
            } catch (SQLException var16) {
                throw var16;
            }

        }

        ArrayList var20 = new ArrayList();

        for(int var5 = var3.size() - 1; var5 >= 0; --var5) {
            columnVo = (ColumnVo)var3.get(var5);
            var20.add(columnVo);
        }

        return var20;
    }

    public static List<ColumnVo> getOriginalColumns(String tableName) throws Exception {
        ResultSet var1 = null;
        String var2 = null;
        ArrayList<ColumnVo> var3 = new ArrayList();

        ColumnVo columnVo;
        try {
            Class.forName(AppConfig.DRIVER_NAME);
            CONNECTION = DriverManager.getConnection(AppConfig.URL, AppConfig.USERNAME, AppConfig.PASSWORD);
            STATEMENT = CONNECTION.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (AppConfig.DB_TYPE.equals("mysql")) {
                var2 = MessageFormat.format("select column_name,data_type,column_comment,numeric_precision,numeric_scale,character_maximum_length,is_nullable nullable from information_schema.columns where table_name = {0} and table_schema = {1}", StrUtil.wrapWithSingleQuote(tableName.toUpperCase()), StrUtil.wrapWithSingleQuote(AppConfig.DATABASE_NAME));
            }

            if (AppConfig.DB_TYPE.equals("oracle")) {
                var2 = MessageFormat.format(" select colstable.column_name column_name, colstable.data_type data_type, commentstable.comments column_comment, colstable.Data_Precision column_precision, colstable.Data_Scale column_scale,colstable.Char_Length,colstable.nullable from user_tab_cols colstable  inner join user_col_comments commentstable  on colstable.column_name = commentstable.column_name  where colstable.table_name = commentstable.table_name  and colstable.table_name = {0}", StrUtil.wrapWithSingleQuote(tableName.toUpperCase()));
            }

            if (AppConfig.DB_TYPE.equals("postgresql")) {
                var2 = MessageFormat.format("SELECT a.attname AS  field,t.typname AS type,col_description(a.attrelid,a.attnum) as comment,null as column_precision,null as column_scale,null as Char_Length,a.attnotnull  FROM pg_class c,pg_attribute  a,pg_type t  WHERE c.relname = {0} and a.attnum > 0  and a.attrelid = c.oid and a.atttypid = t.oid  ORDER BY a.attnum ", StrUtil.wrapWithSingleQuote(tableName.toLowerCase()));
            }

            if (AppConfig.DB_TYPE.equals("sqlserver")) {
                var2 = MessageFormat.format("select distinct cast(a.name as varchar(50)) column_name,  cast(b.name as varchar(50)) data_type,  cast(e.value as varchar(200)) comment,  cast(ColumnProperty(a.object_id,a.Name,'''Precision''') as int) num_precision,  cast(ColumnProperty(a.object_id,a.Name,'''Scale''') as int) num_scale,  a.max_length,  (case when a.is_nullable=1 then '''y''' else '''n''' end) nullable,column_id   from sys.columns a left join sys.types b on a.user_type_id=b.user_type_id left join (select top 1 * from sys.objects where type = '''U''' and name ={0}  order by name) c on a.object_id=c.object_id left join sys.extended_properties e on e.major_id=c.object_id and e.minor_id=a.column_id and e.class=1 where c.name={0} order by a.column_id", StrUtil.wrapWithSingleQuote(tableName.toLowerCase()));
            }

            var1 = STATEMENT.executeQuery(var2);
            var1.last();
            int var4 = var1.getRow();
            if (var4 <= 0) {
                throw new Exception("该表不存在或者表中没有字段");
            }

            columnVo = new ColumnVo();
            if (AppConfig.DB_FIELD_CONVERT) {
                columnVo.setFieldName(convertDbFieldName(var1.getString(1).toLowerCase()));
            } else {
                columnVo.setFieldName(var1.getString(1).toLowerCase());
            }

            columnVo.setFieldDbName(var1.getString(1).toUpperCase());
            columnVo.setPrecision(StrUtil.normalizeBlankStr(var1.getString(4)));
            columnVo.setScale(StrUtil.normalizeBlankStr(var1.getString(5)));
            columnVo.setCharmaxLength(StrUtil.normalizeBlankStr(var1.getString(6)));
            columnVo.setNullable(StrUtil.normalizeBoolFlag(var1.getString(7)));
            columnVo.setFieldType(jdbcTypeToJavaType(var1.getString(2).toLowerCase(), columnVo.getPrecision(), columnVo.getScale()));
            columnVo.setFieldDbType(convertDbFieldName(var1.getString(2).toLowerCase()));
            setClassTypeAndOptionType(columnVo);

            // add by df 2020-9-13  判断是否和DB关键字冲突
            columnVo.setKeyword(DbKeywords.isKeyword(columnVo.getFieldName()));

            columnVo.setFiledComment(StringUtils.isBlank(var1.getString(3)) ? columnVo.getFieldName() : var1.getString(3));
            LOGGER.debug("columnt.getFieldName() -------------" + columnVo.getFieldName());
            var3.add(columnVo);

            while(true) {
                if (!var1.previous()) {
                    LOGGER.debug("读取表成功");
                    break;
                }

                ColumnVo var7 = new ColumnVo();
                if (AppConfig.DB_FIELD_CONVERT) {
                    var7.setFieldName(convertDbFieldName(var1.getString(1).toLowerCase()));
                } else {
                    var7.setFieldName(var1.getString(1).toLowerCase());
                }

                var7.setFieldDbName(var1.getString(1).toUpperCase());
                var7.setPrecision(StrUtil.normalizeBlankStr(var1.getString(4)));
                var7.setScale(StrUtil.normalizeBlankStr(var1.getString(5)));
                var7.setCharmaxLength(StrUtil.normalizeBlankStr(var1.getString(6)));
                var7.setNullable(StrUtil.normalizeBoolFlag(var1.getString(7)));
                var7.setFieldType(jdbcTypeToJavaType(var1.getString(2).toLowerCase(), var7.getPrecision(), var7.getScale()));
                var7.setFieldDbType(convertDbFieldName(var1.getString(2).toLowerCase()));
                setClassTypeAndOptionType(var7);
                // add by df 2020-9-13  判断是否和DB关键字冲突
                var7.setKeyword(DbKeywords.isKeyword(var7.getFieldName()));
                var7.setFiledComment(StringUtils.isBlank(var1.getString(3)) ? var7.getFieldName() : var1.getString(3));
                var3.add(var7);
            }
        } catch (ClassNotFoundException var16) {
            throw var16;
        } catch (SQLException var17) {
            throw var17;
        } finally {
            try {
                if (STATEMENT != null) {
                    STATEMENT.close();
                    STATEMENT = null;
                    System.gc();
                }

                if (CONNECTION != null) {
                    CONNECTION.close();
                    CONNECTION = null;
                    System.gc();
                }
            } catch (SQLException var15) {
                throw var15;
            }

        }

        ArrayList var19 = new ArrayList();

        for(int var5 = var3.size() - 1; var5 >= 0; --var5) {
            columnVo = (ColumnVo)var3.get(var5);
            var19.add(columnVo);
        }

        return var19;
    }

    public static boolean isExist(String var0) {
        String var2 = null;

        try {
            LOGGER.debug("数据库驱动: " + AppConfig.DRIVER_NAME);
            Class.forName(AppConfig.DRIVER_NAME);
            CONNECTION = DriverManager.getConnection(AppConfig.URL, AppConfig.USERNAME, AppConfig.PASSWORD);
            STATEMENT = CONNECTION.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (AppConfig.DB_TYPE.equals("mysql")) {
                var2 = "select column_name,data_type,column_comment,0,0 from information_schema.columns where table_name = '" + var0.toUpperCase() + "' and table_schema = '" + AppConfig.DATABASE_NAME + "'";
            }

            if (AppConfig.DB_TYPE.equals("oracle")) {
                var2 = "select colstable.column_name column_name, colstable.data_type data_type, commentstable.comments column_comment from user_tab_cols colstable  inner join user_col_comments commentstable  on colstable.column_name = commentstable.column_name  where colstable.table_name = commentstable.table_name  and colstable.table_name = '" + var0.toUpperCase() + "'";
            }

            if (AppConfig.DB_TYPE.equals("postgresql")) {
                var2 = MessageFormat.format("SELECT a.attname AS  field,t.typname AS type,col_description(a.attrelid,a.attnum) as comment,null as column_precision,null as column_scale,null as Char_Length,a.attnotnull  FROM pg_class c,pg_attribute  a,pg_type t  WHERE c.relname = {0} and a.attnum > 0  and a.attrelid = c.oid and a.atttypid = t.oid  ORDER BY a.attnum ", StrUtil.wrapWithSingleQuote(var0.toLowerCase()));
            }

            if (AppConfig.DB_TYPE.equals("sqlserver")) {
                var2 = MessageFormat.format("select distinct cast(a.name as varchar(50)) column_name,  cast(b.name as varchar(50)) data_type,  cast(e.value as varchar(200)) comment,  cast(ColumnProperty(a.object_id,a.Name,'''Precision''') as int) num_precision,  cast(ColumnProperty(a.object_id,a.Name,'''Scale''') as int) num_scale,  a.max_length,  (case when a.is_nullable=1 then '''y''' else '''n''' end) nullable,column_id   from sys.columns a left join sys.types b on a.user_type_id=b.user_type_id left join (select top 1 * from sys.objects where type = '''U''' and name ={0}  order by name) c on a.object_id=c.object_id left join sys.extended_properties e on e.major_id=c.object_id and e.minor_id=a.column_id and e.class=1 where c.name={0} order by a.column_id", StrUtil.wrapWithSingleQuote(var0.toLowerCase()));
            }

            ResultSet var1 = STATEMENT.executeQuery(var2);
            var1.last();
            int var3 = var1.getRow();
            return var3 > 0;
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }

    private static String convertDbFieldName(String var0) {
        String[] var1 = var0.split("_");
        var0 = "";
        int var2 = 0;

        for(int var3 = var1.length; var2 < var3; ++var2) {
            if (var2 > 0) {
                String var4 = var1[var2].toLowerCase();
                var4 = var4.substring(0, 1).toUpperCase() + var4.substring(1, var4.length());
                var0 = var0 + var4;
            } else {
                var0 = var0 + var1[var2].toLowerCase();
            }
        }

        return var0;
    }

    public static String d(String var0) {
        String[] var1 = var0.split("_");
        var0 = "";
        int var2 = 0;

        for(int var3 = var1.length; var2 < var3; ++var2) {
            if (var2 > 0) {
                String var4 = var1[var2].toLowerCase();
                var4 = var4.substring(0, 1).toUpperCase() + var4.substring(1, var4.length());
                var0 = var0 + var4;
            } else {
                var0 = var0 + var1[var2].toLowerCase();
            }
        }

        var0 = var0.substring(0, 1).toUpperCase() + var0.substring(1);
        return var0;
    }

    private static void setClassTypeAndOptionType(ColumnVo columnVo) {
        String fieldType = columnVo.getFieldType();
        String scale = columnVo.getScale();
        columnVo.setClassType("inputxt");
        if ("N".equals(columnVo.getNullable())) {
            columnVo.setOptionType("*");
        }

        if (!"datetime".equals(fieldType) && !fieldType.contains("time")) {
            if ("date".equals(fieldType)) {
                columnVo.setClassType("easyui-datebox");
            } else if (fieldType.contains("int")) {
                columnVo.setOptionType("n");
            } else if ("number".equals(fieldType)) {
                if (StringUtils.isNotBlank(scale) && Integer.parseInt(scale) > 0) {
                    columnVo.setOptionType("d");
                }
            } else if (!"float".equals(fieldType) && !"double".equals(fieldType) && !"decimal".equals(fieldType)) {
                if ("numeric".equals(fieldType)) {
                    columnVo.setOptionType("d");
                }
            } else {
                columnVo.setOptionType("d");
            }
        } else {
            columnVo.setClassType("easyui-datetimebox");
        }

    }

    private static String jdbcTypeToJavaType(String jdbcType, String precision, String scale) {
        if (jdbcType.contains("char") || jdbcType.contains("text")) {
            jdbcType = "java.lang.String";
        } else if(jdbcType.contains(JDBCType.TINYINT.getName().toLowerCase())) {
            if (AppConfig.TINYINT_TO_BOOLEAN) {
                jdbcType = "java.lang.Boolean";
            }else {
                jdbcType = "java.lang.Integer";
            }
        } else if(jdbcType.contains(JDBCType.BIGINT.getName().toLowerCase())) {
            jdbcType = "java.lang.Long";
        } else if (jdbcType.contains("int")) {
            jdbcType = "java.lang.Integer";
        } else if (jdbcType.contains("float")) {
            jdbcType = "java.lang.Float";
        } else if (jdbcType.contains("double")) {
            jdbcType = "java.lang.Double";
        } else if (jdbcType.contains("number")) {
            if (StringUtils.isNotBlank(scale) && Integer.parseInt(scale) > 0) {
                jdbcType = "java.math.BigDecimal";
            } else if (StringUtils.isNotBlank(precision) && Integer.parseInt(precision) > 10) {
                jdbcType = "java.lang.Long";
            } else {
                jdbcType = "java.lang.Integer";
            }
        } else if (jdbcType.contains("decimal")) {
            jdbcType = "java.math.BigDecimal";
        } else if (jdbcType.contains("date")) {
            jdbcType = "java.util.Date";
        } else if (jdbcType.contains("time")) {
            jdbcType = "java.util.Date";
        } else if (jdbcType.contains("blob")) {
            jdbcType = "byte[]";
        } else if (jdbcType.contains("clob")) {
            jdbcType = "java.sql.Clob";
        } else if (jdbcType.contains("numeric")) {
            jdbcType = "java.math.BigDecimal";
        } else {
            jdbcType = "java.lang.Object";
        }

        return jdbcType;
    }
}
