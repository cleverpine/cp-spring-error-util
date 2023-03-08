package com.cleverpine.cpspringerrorutil.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GenericResponseEntityUtil extends AbstractResponseEntityUtil {

    public <R, D> ResponseEntity<R> created(D responseData, Class<D> responseDataClass, Class<R> responseClass) {
        R response = getResponseObj(responseData, responseDataClass, responseClass);
        return new ResponseEntity<R>(response, HttpStatus.CREATED);
    }

    public <R, D> ResponseEntity<R> ok(D responseData, Class<D> responseDataClass, Class<R> responseClass) {
        R response = getResponseObj(responseData, responseDataClass, responseClass);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
