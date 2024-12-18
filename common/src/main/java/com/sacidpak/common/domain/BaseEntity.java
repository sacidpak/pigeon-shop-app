package com.sacidpak.common.domain;

import com.sacidpak.common.dto.BaseEntityDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;


import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity<T extends BaseEntity, D extends BaseEntityDto> implements Serializable, Comparable<BaseEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(insertable = false, updatable = false)
    private Long id;

    @Column(name = "is_deleted")
    private Boolean deleted = Boolean.FALSE;

    @Version
    @Column(name = "version")
    private  Integer version = 0;

    @Basic
    @CreatedDate
    @Column(name = "create_date")
    private Date createDate;

    @Basic
    @Column(name = "update_date")
    @LastModifiedDate
    private Date updateDate;

    @Basic
    @CreatedBy
    @Column(name = "create_user")
    private Long createUser;

    @Basic
    @LastModifiedBy
    @Column(name = "update_user")
    private Long updateUser;

    @Override
    public int compareTo(BaseEntity o) {
        if(o == null)
            return 1;

        if(this.getId() != null)
            return this.getId().compareTo(o.getId());

        return -1;
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += null == getId() ? 0 : getId().hashCode();
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if(null == obj)
            return false;

        if(this == obj)
            return true;

        if(!(obj instanceof BaseEntity))
            return false;

        BaseEntity that = (BaseEntity) obj;
        return null == this.getId() ? false : this.getId().equals(that.getId());
    }
}
