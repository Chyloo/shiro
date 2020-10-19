package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private Long id;
    /**
     * 权限类型
     */
    private Integer type;
    private String name;
    private String url;
    private String permission;
}
