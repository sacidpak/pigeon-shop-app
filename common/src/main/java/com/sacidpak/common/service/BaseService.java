package com.sacidpak.common.service;

import com.sacidpak.common.repository.BaseRepository;
import com.sacidpak.common.domain.BaseEntity;
import com.sacidpak.common.dto.BaseEntityDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;


@Transactional
public abstract class BaseService<T extends BaseEntity, D extends BaseEntityDto, ID  extends Serializable> {

    @Autowired
    protected BaseRepository<T,ID> baseRepository;

    protected Class<T> entityClass;

    protected Class<D> dtoClass;

    public BaseService(){
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        this.dtoClass = (Class<D>) parameterizedType.getActualTypeArguments()[1];
    }

    protected void validateSave(D dto){}

    protected void validateUpdate(D dto){}

    protected void validateDelete(ID id, Integer version){}

    protected T dtoToEntity(D dto){
        T entity;
        if(dto.getId() != null){
            entity = baseRepository.findOne((ID) dto.getId());
            new ModelMapper().map(dto, entity);
        }else{
            entity = new ModelMapper().map(dto, entityClass);
        }

        return entity;
    }


    @Transactional
    public ID save(D dto) {
        validateSave(dto);
        T entity = dtoToEntity(dto);
        return (ID) baseRepository.save(entity).getId();
    }

    public void update(D dto) {
        validateUpdate(dto);
        T entity = dtoToEntity(dto);
        baseRepository.save(entity);
    }

    public void delete(ID id, Integer version) {
        validateDelete(id,version);
        T entity = baseRepository.findOne(id);
        entity.setDeleted(true);
        baseRepository.save(entity);
    }

    public D getById(ID id) {
        T entity = baseRepository.findOne(id);
        if(entity != null)
            return new ModelMapper().map(entity, dtoClass);
        else
            return null;
    }

    public List<D> getAll() {
        List<D> dtoList = new ArrayList<>();
        List<T> entityList = baseRepository.findAll();

        for (T entity : entityList){
            dtoList.add(new ModelMapper().map(entity, dtoClass));
        }

        return dtoList;
    }
}
