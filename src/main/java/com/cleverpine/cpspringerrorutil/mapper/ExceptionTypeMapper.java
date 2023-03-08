package com.cleverpine.cpspringerrorutil.mapper;

import org.springframework.http.HttpStatus;

public interface ExceptionTypeMapper {

    String getType(HttpStatus httpStatus);

}
