package com.nla.rsvp.controller;

import com.nla.rsvp.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BaseController {

    protected User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    protected <D> D convert(Object obj, Class<D> classType) {
        ModelMapper mapper = new ModelMapper();

        return mapper.map(obj, classType);
    }

    protected <S, D> List<D> convertToList(List<S> objs, Class<D> classType) {
        if(CollectionUtils.isEmpty(objs)) {
            return new ArrayList<>();
        }

        ModelMapper mapper = new ModelMapper();

        return objs.stream()
                .map(obj -> mapper.map(obj, classType))
                .collect(Collectors.toList());
    }


    protected void merge(Object fromObj, Object toObj) {
        ModelMapper mapper = new ModelMapper();
        mapper.map(fromObj, toObj);
    }
}
