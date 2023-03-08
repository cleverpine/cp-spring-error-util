package com.cleverpine.cpspringerrorutil.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseModel {

  public final ErrorData error;

  public ErrorResponseModel(int status, String type, String title, String detail) {
    this.error = new ErrorData(status, type, title, detail);
  }

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private static class ErrorData {
    public final int status;

    public final String type;

    public final String title;

    public final String detail;

    public ErrorData(int status, String type, String title, String detail) {
      this.status = status;
      this.type = type;
      this.title = title;
      this.detail = detail;
    }
  }
}
