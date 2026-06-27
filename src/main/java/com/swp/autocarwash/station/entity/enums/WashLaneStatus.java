package com.swp.autocarwash.station.entity.enums;

/**
 * Chức năng: Trạng thái của làn rửa (WashLane).
 *
 * <ul>
 *     <li>{@code AVAILABLE} — làn trống, có thể nhận xe</li>
 *     <li>{@code OCCUPIED}  — làn đang có xe rửa</li>
 * </ul>
 *
 * @author KimNgan
 * @version 1.0
 */
public enum WashLaneStatus {
    AVAILABLE,
    OCCUPIED;

    public static final String AVAILABLE_VALUE = "AVAILABLE";
    public static final String OCCUPIED_VALUE = "OCCUPIED";
}
