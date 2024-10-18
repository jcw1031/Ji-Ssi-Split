package com.woopaca.jissisplit.kis.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record WebSocketApprovalKeyResponse(@JsonAlias("approval_key") String approvalKey) {
}