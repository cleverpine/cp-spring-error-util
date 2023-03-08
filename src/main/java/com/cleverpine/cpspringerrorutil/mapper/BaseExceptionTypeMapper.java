package com.cleverpine.cpspringerrorutil.mapper;

import org.springframework.http.HttpStatus;

public class BaseExceptionTypeMapper implements ExceptionTypeMapper {

    @Override
    public String getType(HttpStatus httpStatus) {
        switch (httpStatus.value()) {
            case 400:
                return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.1";
            case 401:
                return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.2";
            case 403:
                return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.3";
            case 404:
                return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.4";
            case 405:
                return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.5";
            case 408:
                return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.7";
            case 413:
                return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.11";
            case 415:
                return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.15";
            case 500:
                return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.6.1";
            case 501:
                return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.6.2";
            default:
                return null;
        }
    }
}
