package top.yeonon.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Topic {
    private Integer id;

    private String name;

    private String description;

    private Integer status;

    private Date createTime;

    private Date updateTime;

}