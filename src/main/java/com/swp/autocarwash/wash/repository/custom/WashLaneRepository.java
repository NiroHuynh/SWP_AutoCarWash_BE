package com.swp.autocarwash.wash.repository.custom;

import com.swp.autocarwash.wash.entity.WashLane;
import com.swp.autocarwash.wash.entity.enums.WashLaneStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WashLaneRepository extends JpaRepository<WashLane, Integer> {
    //check xem hệ thống có bất kỳ làn rửa nào ở trạng thái đc truyền vào hay ko
    Boolean existsByStatus(String status);

    //loại trừ những thằng đã bị softdeleted ra
    Boolean existsByStatusAndIsDeletedFalse(String status);
}
