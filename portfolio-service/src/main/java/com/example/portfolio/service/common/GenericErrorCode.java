package com.example.portfolio.service.common;

import java.util.HashMap;
import java.util.Map;

public enum GenericErrorCode implements ErrorCode {
    GEN_001("GEN-001", "An unexpected error occurred. Please contact System Administrator"),
    SGUR_001("SGR-001", "Try any other Username, this Username has already been taken"),
    SGUR_002("SGR-002", "This user has already been registered, try with any other emailId"),
    ATH_001("ATH-001", "This username does not exist"),
    ATH_002("ATH-002", "Password failed"),
    ATHR_001_COMMON("ATHR-001", "User has not signed in"),
    ATHR_002_COMMON("ATHR-002", "User is signed out.Sign in first to get user details"),
    USR_001_COMMON("USR-001", "User with entered uuid does not exist"),
    SGOR_001("SGR-001", "User is not Signed in"),
    DATE_001("DAT-001", "Date entered is not in the correct format"),
    SCHL_001("SCHL-001", "Entered schoolId is not present"),
    COL_001("COL_001", "Entered collegeId is not present"),
    POS_001("POS_001", "Entered postId is not present"),
    ATHR_003_COMMON("USR-002", "User does not have the permission to alter the object");
    private static final Map<String, GenericErrorCode> LOOKUP = new HashMap<String, GenericErrorCode>();

    static {
        for (final GenericErrorCode enumeration : GenericErrorCode.values()) {
            LOOKUP.put(enumeration.getCode(), enumeration);
        }
    }

    private final String code;

    private final String defaultMessage;

    private GenericErrorCode(final String code, final String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }
}
