package com.ruantang.service.matter.core.service;

public interface CaseStatusService {

    void updateCaseStatus(String caseId, String fromStatus, String toStatus);

}
