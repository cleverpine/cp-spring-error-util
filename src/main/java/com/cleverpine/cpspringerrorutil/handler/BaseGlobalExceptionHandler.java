package com.cleverpine.cpspringerrorutil.handler;

import com.cleverpine.cpspringerrorutil.exception.AuthorisationException;
import com.cleverpine.cpspringerrorutil.exception.BadRequestException;
import com.cleverpine.cpspringerrorutil.exception.DuplicateEntityException;
import com.cleverpine.cpspringerrorutil.logger.Logger;
import com.cleverpine.cpspringerrorutil.mapper.ExceptionTypeMapper;
import com.cleverpine.cpspringerrorutil.model.ErrorResponseModel;
import jakarta.validation.ConstraintViolation;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.stream.Collectors;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

public class BaseGlobalExceptionHandler {

    private final ExceptionTypeMapper exceptionTypeMapper;

    private final Logger log;

    private final boolean includeStackTrace;

    public BaseGlobalExceptionHandler(ExceptionTypeMapper exceptionTypeMapper, Logger log, boolean includeStackTrace) {
        this.exceptionTypeMapper = exceptionTypeMapper;
        this.log = log;
        this.includeStackTrace = includeStackTrace;
    }

    @ExceptionHandler(AuthorisationException.class)
    public ResponseEntity<ErrorResponseModel> handleAccessDeniedException(AuthorisationException ex) {
        log.error(ex.getMessage(), ex);
        return createErrorResponse(ex, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({BadRequestException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponseModel> handleBadRequestException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ErrorResponseModel> handleTypeMismatchException(TypeMismatchException ex) {
        log.error(ex.getMessage(), ex);
        return createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseModel> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);
        var message = ex.getBindingResult().getAllErrors()
                .stream()
                .map(err -> err.unwrap(ConstraintViolation.class))
                .map(err -> String.format("'%s' %s", err.getPropertyPath(), err.getMessage()))
                .collect(Collectors.joining(", "));
        return createErrorResponse(ex, HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorResponseModel> handleSqlException(SQLException ex) {
        log.error(ex.getMessage(), ex);
        return createErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<ErrorResponseModel> handleDuplicateEntityException(DuplicateEntityException ex) {
        log.error(ex.getMessage(), ex);
        return createErrorResponse(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponseModel> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        log.error(ex.getMessage(), ex);
        return createErrorResponse(ex, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseModel> handleRuntimeException(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return createErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected ResponseEntity<ErrorResponseModel> createErrorResponse(Throwable cause, HttpStatus httpStatus) {
        return createErrorResponse(cause, httpStatus, null);
    }

    protected ResponseEntity<ErrorResponseModel> createErrorResponse(Throwable cause, HttpStatus httpStatus, String message) {
        var error = new ErrorResponseModel(
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                exceptionTypeMapper.getType(httpStatus),
                getDetail(cause, message));
        return new ResponseEntity<>(error, httpStatus);
    }

    private String getDetail(Throwable cause, String message) {
        if (cause == null && message == null) {
            return null;
        }
        if (message == null) {
            message = cause.getMessage();
        }
        String stackTrace = "";
        if (includeStackTrace && cause != null) {
            try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
                cause.printStackTrace(pw);
                stackTrace = sw.toString();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                stackTrace = "";
            }
        }
        return String.format("%s\n%s", message, stackTrace);
    }

}
