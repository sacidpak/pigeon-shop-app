package com.sacidpak.common.dto;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public abstract class BaseEntityDto implements Serializable, Comparable<BaseEntityDto> {

    private Long id;

    private Integer version;

    @Override
    public int compareTo(BaseEntityDto o) {
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

        if(!(obj instanceof BaseEntityDto))
            return false;

        BaseEntityDto that = (BaseEntityDto) obj;
        return null == this.getId() ? false : this.getId().equals(that.getId());
    }
}
