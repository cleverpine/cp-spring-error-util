package com.cleverpine.cpspringerrorutil.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AbstractResponseEntityUtil {

    protected static final String DATA_FIELD_NAME = "data";

    public ResponseEntity<Void> ok() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Void> created() {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<Void> noContent() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    protected final <R, D> R getResponseObj(List<D> responseDataList, Class<D> responseDataClass, Class<R> responseClass) {
        return getResponseObjInt(responseDataList, responseDataClass, responseClass);
    }

    protected final <R, D> R getResponseObj(D responseData, Class<D> responseDataClass, Class<R> responseClass) {
        return getResponseObjInt(responseData, responseDataClass, responseClass);
    }

    private <R, D> R getResponseObjInt(Object responseData, Class<D> responseDataClass, Class<R> responseClass) {
        try {
            Constructor<R> constructor = responseClass.getConstructor();
            var instance =  constructor.newInstance();
            var dataField = responseClass.getDeclaredField(DATA_FIELD_NAME);
            dataField.setAccessible(true);
            dataField.set(instance, responseData);
            return instance;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            throw new IllegalStateException();
        }
    }

}