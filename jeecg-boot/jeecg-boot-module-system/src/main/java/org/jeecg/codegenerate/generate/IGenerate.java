package org.jeecg.codegenerate.generate;

import java.util.Map;

public interface IGenerate {
    Map<String, Object> getCodeTemplateData() throws Exception;

    void generateCodeFile() throws Exception;

    void generateCodeFile(String var1, String var2) throws Exception;
}
