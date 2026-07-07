package com.swp.autocarwash.wash.mapper;

import com.swp.autocarwash.wash.dto.response.WashLaneResponse;
import com.swp.autocarwash.wash.entity.WashLane;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WashLaneMapper {
    WashLaneResponse toResponse(WashLane washLane);

    List<WashLaneResponse> toResponseList(List<WashLane> washLanes);
}
