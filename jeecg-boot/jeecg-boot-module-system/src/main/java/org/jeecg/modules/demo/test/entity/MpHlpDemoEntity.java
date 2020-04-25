package org.jeecg.modules.demo.test.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.jeecg.mphelper.annotation.DeseField;
import org.jeecg.mphelper.annotation.MpEntity;
import org.jeecg.mphelper.format.CommaStrToListFormat;
import org.jeecg.mphelper.web.MyDeserializer;

import java.util.List;

/**
 * @author dafei
 * @version 0.1
 * @date 2020/4/21 22:06
 */
@MpEntity
@Data
public class MpHlpDemoEntity {

    @JsonDeserialize(using = MyDeserializer.class)
    @DeseField(inFormatEl = "['id']?:888")
    private Integer id;
    @DeseField(inFormatEl = "#$?.trim()+'-123456'", outFormatFun = CommaStrToListFormat.class) // 剔除收尾空格
    private String uname;
    @DeseField(inFormatEl = "!#$")
    private Boolean zip;
    @DeseField(inFormatFun = CommaStrToListFormat.class)
    private List<String> friends;

    private String src;

}
