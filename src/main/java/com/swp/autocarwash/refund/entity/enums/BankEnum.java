package com.swp.autocarwash.refund.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/**
 * Chức năng: Danh mục ngân hàng Việt Nam dùng cho luồng hoàn tiền.
 *
 * <p>Mỗi hằng gồm {@code bin} (mã napas BIN, dùng gọi VietQR Lookup và build QR),
 * {@code displayName} (tên hiển thị cho khách chọn) và {@code shortCode}
 * (mã ngắn kiểu SePay/VietQR, phòng khi cần build QR sau này).</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum BankEnum {

    VIETCOMBANK("970436", "Vietcombank", "VCB"),
    VIETINBANK("970415", "VietinBank", "ICB"),
    BIDV("970418", "BIDV", "BIDV"),
    AGRIBANK("970405", "Agribank", "VBA"),
    TPBANK("970423", "TPBank", "TPB"),
    MBBANK("970422", "MB Bank", "MB"),
    TECHCOMBANK("970407", "Techcombank", "TCB"),
    ACB("970416", "ACB", "ACB"),
    VPBANK("970432", "VPBank", "VPB"),
    SACOMBANK("970403", "Sacombank", "STB"),
    VIB("970441", "VIB", "VIB"),
    SHB("970443", "SHB", "SHB"),
    HDBANK("970437", "HDBank", "HDB"),
    OCB("970448", "OCB", "OCB"),
    MSB("970426", "MSB", "MSB"),
    SEABANK("970440", "SeABank", "SEAB"),
    NAMABANK("970428", "Nam A Bank", "NAB"),
    BVBANK("970454", "BVBank", "BVB");

    private final String bin;
    private final String displayName;
    private final String shortCode;

    /**
     * Tra ngân hàng theo BIN.
     *
     * @param bin mã napas BIN
     * @return Optional chứa BankEnum tương ứng, rỗng nếu không khớp
     */
    public static Optional<BankEnum> fromBin(String bin) {
        if (bin == null) {
            return Optional.empty();
        }
        return Arrays.stream(values())
                .filter(b -> b.bin.equals(bin.trim()))
                .findFirst();
    }
}
