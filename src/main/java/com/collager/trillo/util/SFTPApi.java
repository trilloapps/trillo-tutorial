package com.collager.trillo.util;

import com.collager.trillo.pojo.Result;

public class SFTPApi extends BaseApi {
  
  public static Result get(String server, String userName, String password, 
      String remoteFile, String localFile) {
    return get(server, 22, userName, password, remoteFile, localFile);
  }
  
  
  public static Result get(String server, int port, String userName, String password, 
      String remoteFile, String localFile) {
    return remoteCallAsResult("SFTPApi", "get", 
        server, port, userName, password, remoteFile, localFile);
  }
  
  public static Result put(String server, String userName, String password, 
      String remoteFile, String localFile) {
    return put(server, 22, userName, password, remoteFile, localFile);
  }
  
  public static Result put(String server, int port, String userName, String password, 
      String remoteFile, String localFile) {
    return remoteCallAsResult("SFTPApi", "put", 
        server, port, userName, password, remoteFile, localFile);
  }
  
  public static Result ls(String server, String userName, String password, 
      String remoteFolder) {
    return ls(server, 22, userName, password, remoteFolder);
  }
  
  public static Result ls(String server, int port, String userName, String password, 
      String remoteFolder) {
    return remoteCallAsResult("SFTPApi", "ls", server, port, userName, password, remoteFolder);
  }
  
  public static Result ls(String server, String userName, String password, 
      String remoteFolder, String extensions) {
    return ls(server, 22, userName, password, remoteFolder, extensions);
  }
  
  public static Result ls(String server, int port, String userName, String password, 
      String remoteFolder, String extensions) {
    return remoteCallAsResult("SFTPApi", "ls", server, port, userName, password, remoteFolder, extensions);
  }
}
