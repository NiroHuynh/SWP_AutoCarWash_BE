package com.swp.autocarwash.auth.mapper;

import com.swp.autocarwash.auth.dto.response.RegisterResponse;
import com.swp.autocarwash.auth.entity.User;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;



}
