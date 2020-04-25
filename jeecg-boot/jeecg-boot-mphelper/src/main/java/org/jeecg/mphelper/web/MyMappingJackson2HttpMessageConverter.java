package org.jeecg.mphelper.web;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.databind.JavaType;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.mphelper.annotation.DeseField;
import org.jeecg.mphelper.annotation.MpEntity;
import org.jeecg.mphelper.format.InFormat;
import org.jeecg.mphelper.format.OutFormat;
import org.jeecg.mphelper.util.FieldUtil;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author dafei
 * @version 0.1
 * @date 2020/4/21 22:12
 */
@Slf4j
public class MyMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    private static ExpressionParser parser;

    {
        // 自动初始化空元素和扩容
        SpelParserConfiguration config = new SpelParserConfiguration(true, true);
        parser = new SpelExpressionParser(config);
    }


    // getSupportedMediaTypes()
    // super(objectMapper, MediaType.APPLICATION_JSON, new MediaType("application", "*+json"))
    // MappingJackson2HttpMessageConverter 构造时已经定义好了支持的 media type
    @Override
    protected boolean canRead(MediaType mediaType) {
        return super.canRead(mediaType);
    }

    @Override
    public boolean canRead(Type type, @Nullable Class<?> contextClass, @Nullable MediaType mediaType) {
        return super.canRead(type, contextClass, mediaType);
    }

    @Override
    public Object read(Type type, @Nullable Class<?> contextClass, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {

        JavaType javaType = getJavaType(type, contextClass);

        // Object o = this.objectMapper.readValue(inputMessage.getBody(), javaType);

        Object o = null;
        Class clz = (Class) type;
        if (clz.getAnnotation(MpEntity.class) != null) {
            Map<String, Field> fieldMap = FieldUtil.getAllFieldMap(clz);

            Map map = this.objectMapper.readValue(inputMessage.getBody(), Map.class);
            Map newMap = new LinkedHashMap(map.size());
            StandardEvaluationContext context = new StandardEvaluationContext();
            context.setRootObject(map);
            // 遍历每个字段, 根据字段序列化配置信息, 修改值.
            fieldMap.forEach((k, field) -> {
                Object value = map.get(k);

                DeseField df = field.getAnnotation(DeseField.class);

                if (df != null) {
                    Object nVal = null;
                    Class<? extends Function> inFmtFun = df.inFormatFun();
                    String inFmtEl = df.inFormatEl();
                    // 优先使用 fun
                    if (inFmtFun != null && inFmtFun != InFormat.None.class) {
                        Function function = null;
                        try {
                            function = inFmtFun.newInstance();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        nVal = function.apply(value);
                    } else if (!"".equals(inFmtEl)) {
                        // 解析spel修改值
                        // #$ 取当前字段的值; 或者, 字段名取(实质是#root['字段名'])
                        context.setVariable("$", value);
                        Expression expression = parser.parseExpression(inFmtEl);
                        nVal = expression.getValue(context);
                        log.debug("Spel parse input expression {} -> {} from {}", k, nVal, value);
                    } else {
                        nVal = value;
                    }

                    newMap.put(k, nVal);
                } else {
                    //  保持原样
                    newMap.put(k, value);
                }
            });

            // 再次调用json序列化, 可让json的配置生效 ??

            o = BeanUtil.mapToBean(newMap, clz, true);

            return o;
        } else {
            return super.read(type, contextClass, inputMessage);
        }
    }

    // todo 不支持嵌套在字段里, 暂无法解析
    @Override
    protected void writeInternal(Object o, @Nullable Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        Class<?> clz = o.getClass();
        if (clz.getAnnotation(MpEntity.class) != null) {
            // 转换字段后放入新map中
            Map map = BeanUtil.beanToMap(o); // 也是 LinkedHashMap
            Map newMap = new LinkedHashMap(map.size());
            Map<String, Field> fieldMap = FieldUtil.getAllFieldMap(clz);
            StandardEvaluationContext context = new StandardEvaluationContext();
            context.setRootObject(map);
            fieldMap.forEach((k, field) -> {
                Object value = map.get(k);
                DeseField df = field.getAnnotation(DeseField.class);
                if (df != null) {
                    Object nVal = null;
                    Class<? extends Function> outFmtFun = df.outFormatFun();
                    String outFmtEl = df.outFormatEl();
                    if (outFmtFun != null && outFmtFun != OutFormat.None.class) {
                        Function function = null;
                        try {
                            function = outFmtFun.newInstance();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        nVal = function.apply(value);
                    } else if (!"".equals(outFmtEl)) {
                        // 解析spel修改值
                        // #$ 取当前字段的值; 或者, 字段名取(实质是#root['字段名'])
                        context.setVariable("$", value);
                        Expression expression = parser.parseExpression(outFmtEl);
                        nVal = expression.getValue(context);
                        log.debug("Spel parse output expression {} -> {} from {}", k, nVal, value);
                    } else {
                        nVal = value;
                    }

                    newMap.put(k, nVal);
                } else {
                    newMap.put(k, value);
                }
            });

            super.writeInternal(newMap, type, outputMessage);
        } else {
            super.writeInternal(o, type, outputMessage);
        }

    }
}
