package com.swp.autocarwash.auth.mapper;

import com.swp.autocarwash.auth.dto.response.RegisterResponse;
import com.swp.autocarwash.auth.entity.User;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Component;

/**
 *
 * UserMapper dùng để chuyển đổi giữa các đối tượng User và RegisterResponse.
 *
 * @author Phong
 * @version 1.0
 */

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;



}
