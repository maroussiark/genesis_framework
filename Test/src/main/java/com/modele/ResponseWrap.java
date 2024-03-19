package com.modele;

public class ResponseWrap<T> {

  private T data;
  private String error;

  public static <T> ResponseWrap<T> success(T data) {
    return new ResponseWrap<T>(data, null);
  }

  public static <T> ResponseWrap<T> error(String error) {
    return new ResponseWrap<T>(null, error);
  }

  public ResponseWrap(T data, String error) {
    this.data = data;
    this.error = error;
  }

  public void setData(T data) {
    this.data = data;
  }

  public void setError(String error) {
    this.error = error;
  }

  public T getData() {
    return data;
  }

  public String getError() {
    return error;
  }
}
