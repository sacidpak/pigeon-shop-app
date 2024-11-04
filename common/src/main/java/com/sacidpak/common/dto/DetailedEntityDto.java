package com.sacidpak.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DetailedEntityDto extends BaseEntityDto {

    private Date createDate;

    private Date updateDate;

    private Long createUser;

    private Long updateUser;
}
