package com.cleverpine.cpspringerrorutil.util;

import java.util.LinkedList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

public class ListResponseEntityUtil<R, D> extends AbstractResponseEntityUtil {

    private final Class<R> responseClass;
    private final Class<D> responseDataClass;

    public ListResponseEntityUtil(@NonNull Class<R> responseClass, @NonNull Class<D> responseDataClass) {
        this.responseClass = responseClass;
        this.responseDataClass = responseDataClass;
        try {
            var testList = new LinkedList<D>();
            var testObj = getResponseObj(testList);
        } catch (RuntimeException e) {
            throw new AssertionError(String.format(
                    "Attempted to create ResponseEntityUtil for response object [%s], "
                            + "that doesn't have accessible field named [%s] of type List of [%s]!",
                    responseClass,
                    DATA_FIELD_NAME,
                    responseDataClass
            ), e);
        }
    }

    public ResponseEntity<R> created(List<D> responseDataList) {
        R response = getResponseObj(responseDataList);
        return new ResponseEntity<R>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<R> ok(List<D> responseDataList) {
        R response = getResponseObj(responseDataList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private R getResponseObj(List<D> responseDataList) {
        return getResponseObj(responseDataList, responseDataClass, responseClass);
    }

}
