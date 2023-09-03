package com.nla.rsvp.controller;

import com.nla.rsvp.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BaseController {

    private ModelMapper initModelMapper(MatchingStrategy strategy) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(strategy);
        return mapper;
    }

    protected User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    protected <D> D convert(Object obj, Class<D> classType) {
        ModelMapper mapper = initModelMapper(MatchingStrategies.LOOSE);
        return mapper.map(obj, classType);
    }

    protected <S, D> List<D> convertToList(List<S> objs, Class<D> classType) {
        if (CollectionUtils.isEmpty(objs)) {
            return new ArrayList<>();
        }

        ModelMapper mapper = initModelMapper(MatchingStrategies.LOOSE);
        return objs.stream()
                .map(obj -> mapper.map(obj, classType))
                .collect(Collectors.toList());
    }

    protected void merge(Object fromObj, Object toObj) {
        ModelMapper mapper = initModelMapper(MatchingStrategies.STANDARD);
        mapper.map(fromObj, toObj);
    }
}