package com.sacidpak.common.controller;

import com.sacidpak.common.dto.BaseEntityDto;
import com.sacidpak.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

public abstract class BaseController<S extends BaseService, D extends BaseEntityDto> {

    @Autowired
    protected S entityService;

    @GetMapping("/{id}")
    public ResponseEntity<D> getById(@PathVariable("id") Long id){
        return ResponseEntity.ok((D)entityService.getById(id));
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<D>> getListAll (){
        return ResponseEntity.ok(entityService.getAll());
    }

    @PostMapping
    public ResponseEntity<Long> save (@RequestBody D dto){
        return ResponseEntity.ok((Long) entityService.save(dto));
    }

    @PutMapping
    public void update (@RequestBody D dto){
        entityService.update(dto);
    }

    @DeleteMapping
    public void delete (@RequestParam("id") Long id, @RequestParam("version") Integer version){
        entityService.delete(id,version);
    }

}
