package com.cleverpine.cpspringerrorutil.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

public class ResponseEntityUtil<R, D> extends AbstractResponseEntityUtil {

    private final Class<R> responseClass;
    private final Class<D> responseDataClass;

    public ResponseEntityUtil(@NonNull Class<R> responseClass, @NonNull Class<D> responseDataClass) {
        this.responseClass = responseClass;
        this.responseDataClass = responseDataClass;
        try {
            var testObj = getResponseObj(null);
        } catch (RuntimeException e) {
            throw new AssertionError(String.format(
                    "Attempted to create ResponseEntityUtil for response object [%s], "
                            + "that doesn't have accessible field named [%s] of type [%s]!",
                    responseClass,
                    DATA_FIELD_NAME,
                    responseDataClass
            ), e);
        }
    }

    public ResponseEntity<R> created(D responseData) {
        R response = getResponseObj(responseData);
        return new ResponseEntity<R>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<R> ok(D responseData) {
        R response = getResponseObj(responseData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private R getResponseObj(D responseData) {
        return getResponseObj(responseData, responseDataClass, responseClass);
    }

}
