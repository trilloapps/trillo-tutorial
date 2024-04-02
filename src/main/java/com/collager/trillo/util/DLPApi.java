package com.collager.trillo.util;

import com.collager.trillo.pojo.Result;


public class DLPApi extends BaseApi {
  public static Result inspect(String text, String[] informationTypes) {
    return remoteCallAsResult("DLPApi", "inspect", text, informationTypes);
  }

  public static Result redactImage(byte[] byteImage, ImageType imageType, String[] informationType) {
    return remoteCallAsResult("DLPApi", "redactImage", byteImage, imageType, informationType);
  }

  public static Result redactImage(String base64String, ImageType imageType, String[] informationType) {
    return remoteCallAsResult("DLPApi", "redactImage", base64String, imageType, informationType);
  }

  public static Result redactImageAllText(String base64String, ImageType imageType) {
    return remoteCallAsResult("DLPApi", "redactImageAllText", base64String, imageType);
  }

  public static Result redactImageAllText(byte[] byteImage, ImageType imageType) {
    return remoteCallAsResult("DLPApi", "redactImageAllText", byteImage, imageType);
  }

  public static Result redactPII(String text) {
    return remoteCallAsResult("DLPApi", "redactPII", text);
  }

  public enum ImageType {
    BYTES_TYPE_UNSPECIFIED,
    IMAGE,
    IMAGE_JPEG,
    IMAGE_BMP,
    IMAGE_PNG,
    IMAGE_SVG,
    TEXT_UTF8,
    WORD_DOCUMENT,
    PDF,
    POWERPOINT_DOCUMENT,
    EXCEL_DOCUMENT,
    AVRO,
    CSV,
    TSV
  }
}
