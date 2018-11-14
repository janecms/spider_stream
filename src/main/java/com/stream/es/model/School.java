package com.stream.es.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
@TableName("base_school")
@Data
public class School implements Serializable {
    private String province;
    private Long provinceId;
    @TableField("is_huayue")
    private Integer isHuaYue;

    private Integer isCooperation;
    private String district;
    private String address;
    private Long districtId;
    private String name;
    private String sectionName;
    private String state;
    private String id;
    @TableField("longitude")
    private String latLon;
    private String city;
    private Long cityId;
    private String isShizhi;
    @TableField(exist = false)
    private  Set<String> tags =new HashSet<>();
    @TableField(exist = false)
    int dupex;

    public String getTagsStr(){
        return StringUtils.join(this.tags,",");
    }
}
