package com.swp.autocarwash.staff.service;

import com.swp.autocarwash.staff.dto.request.CalculateInvoiceRequest;
import com.swp.autocarwash.staff.dto.request.CreateWalkInRequest;
import com.swp.autocarwash.staff.dto.response.*;

public interface WalkInCheckInService {

    public CheckPhoneResponse checkPhone(String phone);
    public BookingSummaryResponse calculateInvoice(CalculateInvoiceRequest request);
    public CreateWalkInResponse createWalkInOrder(CreateWalkInRequest request);
    public WalkInFormDataResponse getWalkInFormData();
    public CheckInResultResponse collectWalkInPenaltyDeposit(String licensePlate);
}
