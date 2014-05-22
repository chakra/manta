package com.espendwise.manta.util;


import com.espendwise.manta.service.IllegalDataStateException;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;

public class DatabaseError {


    public static IllegalDataStateException multipleEntityCatalogConfiguration(Long entityId, String assocType) {
        return new IllegalDataStateException(
                new ApplicationExceptionCode<ExceptionReason.IllegalDataStateReason>(
                        ExceptionReason.IllegalDataStateReason.MULTIPLE_ACCOUNT_CATALOGS, Args.typed(entityId, assocType)
                )
        );
    }


    public static IllegalDataStateException mutipleAccountCatalogs(Long accountId) {
        return new IllegalDataStateException(
                new ApplicationExceptionCode<ExceptionReason.IllegalDataStateReason>(
                        ExceptionReason.IllegalDataStateReason.MULTIPLE_ACCOUNT_CATALOGS, Args.typed(accountId)
                )
        );
    }

    public static IllegalDataStateException fiscalCalendarNotFound(Long accountId, Integer year) {
        return new IllegalDataStateException(
                new ApplicationExceptionCode<ExceptionReason.IllegalDataStateReason>(
                        ExceptionReason.IllegalDataStateReason.FISCAL_CALENDAR_NOT_FOUND, Args.typed(accountId, year)
                )
        );
    }
}
