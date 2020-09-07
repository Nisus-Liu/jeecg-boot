package org.jeecg.codegenerate.generate.util;

import org.apache.commons.lang.StringUtils;

public class StrUtil {
    public StrUtil() {
    }

    public static String normalizeBoolFlag(String var0) {
        if (!"YES".equals(var0) && !"yes".equals(var0) && !"y".equals(var0) && !"Y".equals(var0) && !"f".equals(var0)) {
            return !"NO".equals(var0) && !"N".equals(var0) && !"no".equals(var0) && !"n".equals(var0) && !"t".equals(var0) ? null : "N";
        } else {
            return "Y";
        }
    }

    public static String normalizeBlankStr(String var0) {
        return StringUtils.isBlank(var0) ? "" : var0;
    }

    public static String wrapWithSingleQuote(String var0) {
        return "'" + var0 + "'";
    }
}
