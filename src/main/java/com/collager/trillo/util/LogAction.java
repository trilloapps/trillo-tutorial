package com.collager.trillo.util;

public class LogAction {
  
  /* This class define constants used as 'action' parameter in the Logging.
   * This is used by the platform code. For TrilloFunciton, we have gather a few examples before
   * we can standardize on the constants.
   */

  public final static String Login = "Login";
  public final static String Logout = "Logout";
  public final static String ChangePassword = "ChangePassword";
  public final static String ResetPassword = "ResetPassword";
  public final static String ForgotPassword = "ForgotPassword";
  public final static String AddUser = "AddUser";
  public final static String SignupUser = "SignupUser";
  public final static String EditUser = "EditUser";
  public final static String DeleteUser = "DeleteUser";
  public final static String SuspendUser = "SuspendUser";
  public final static String ResumeUser = "ResumeUser";
  public final static String ToggleUser = "ToggleUser";
  public final static String SwitchingUser = "SwitchingUser";
  public final static String OAuth2Loging = "OAuth2Loging";
  public final static String CreateExternalUser = "CreateExternalUser";
  public final static String SaveRole = "SaveRole";
  public final static String DeleteRole = "DeleteRole";
  public final static String Unknown = "Unknown";
  public final static String CreateTenant = "CreateTenant";
  public final static String ServiceAccountPassword = "ServiceAccountPassword";
  public final static String VerifyEmail = "VerifyEmail";
  public final static String AddContact = "AddContact";
  public final static String ClientCredential = "ClientCredential";
  public final static String UploadingUsers = "UploadingUsers";
  
  public final static String FolderCreate = "FolderCreate";
  public final static String FolderUpdate = "FolderUpdate";
  public final static String FolderRename = "FolderRename";
  public final static String FolderMove = "FolderMove";
  public final static String FolderDelete = "FolderDelete";
  public final static String FolderGetCreate = "FolderGetCreate";
  public final static String SaveFolderTask = "SaveFolderTask";
  public final static String ExecuteFolderTask = "ExecuteFolderTask";
  public final static String FolderAyncOp = "FolderAyncOp";
  
  public final static String SaveFileInDb = "SaveFileInDb";
  public final static String FileCreate = "FileCreate";
  public final static String FileUpdate = "FileUpdate";
  public final static String FileRename = "FileRename";
  public final static String FileMove = "FileMove";
  public final static String FileCopy = "FileCopy";
  public final static String FileDelete = "FileDelete";
  public final static String FileRestore = "FileRestore";
  public final static String FileUpload = "FileUpload";
  public final static String FileDownload = "FileDownload";
  public final static String FilePostCreate = "FilePostCreate";
  public final static String SendFileEmail = "SendFileEmail";
  public final static String SignedURL = "SignedURL";
  public final static String ShareWithTenants = "ShareWithTenants";
  public final static String BucketSync = "BucketSync";
  public final static String ShareFile = "ShareFile";
  public final static String FileToUserRelation = "FileToUserRelation";
  public final static String FileToFileRelation = "FileToFileRelation";

  
  public final static String FolderCreateEvent = "FolderCreateEvent";
  public final static String FileCreateEvent = "FileCreateEvent";
  public final static String FolderDeleteEvent = "FolderDeleteEvent";
  public final static String FileDeleteEvent = "FileDeleteEvent";
  
  public final static String BucketOp = "BucketOp";
  public final static String DbOp = "DbOp";
  public final static String BqOp = "BqOp";
  public final static String RestOp = "RestOp";
  public static final String ParallelOp = "ParallelOp";

  
  public final static String PubSubSubscriber = "PubSubSubscriber";

  
  public final static String WriteMetaData = "WriteMetaData";
  
  public final static String Function = "Function";
  
  public final static String GroupCreate = "GroupCreate";
  public final static String GroupUpdate = "GroupUpdate";
  public final static String GroupDelete = "GroupDelete";
  public final static String RoleUpdate = "RoleUpdate";
  public final static String RoleDelete = "RoleDelete";
  
  public final static String Save = "Save";
  public final static String SaveIgnoreError = "SaveIgnoreError";
  public final static String SaveBlob = "SaveBlob";
  public final static String Delete = "Delete";
  public final static String UpdateAttr = "UpdateAttr";
  public final static String GetByMetadataDataSource = "GetByMetadataDataSource";
  public final static String UploadDataFromFiles = "UploadDataFromFiles";
  public final static String UpdateLogo = "UpdateLogo";
  public final static String ExecutePreparedStatement = "ExecutePreparedStatement";
  public final static String ExecuteWriteStatement = "ExecuteWriteStatement";
  public final static String ExecuteSql = "ExecuteSql";
  public final static String BulkOp = "BulkOp";
  public final static String ParseWhereClause = "ParseWhereClause";
  
  public final static String MDSvcCreate = "MDSvcCreate";
  public final static String MDSvcSave = "MDSvcSave";
  public final static String MDSvcDelete = "MDSvcDelete";
  public final static String MDSvcCustomizeClass = "MDSvcCustomizeClass";
  public final static String MDSvcCreateDB = "MDSvcCreateDB";
  public final static String MDSvcCreateSchema = "MDSvcCreateSchema";
  public final static String MDSvcDeploy = "MDSvcDeploy";
  public final static String MDSvcOperation = "MDSvcOperation";
  public final static String MDSvcRename = "MDSvcRename";
  public final static String MDSvcSyncBQ = "MDSvcSyncBQ";
  public final static String MDSvcGitPush = "MDSvcGitPush";
  public final static String MDSvcGitPull = "MDSvcGitPull";
  public final static String MDSvcRestoreVersion = "MDSvcRestoreVersion";
  public final static String MDSvcUpdateTable = "MDSvcUpdateTable";
  public final static String MDSvcUpdateSchema = "MDSvcUpdateSchema";
  public final static String MDSvcCritical = "MDSvcCritical";
  
  public final static String GithubApi = "GithubApi";
  public final static String GitCommand = "GitCommand";
  public final static String GitPush = "GitPush";
  public final static String GitPull = "GitPull";
  public final static String ExportMetadata = "ExportMetadata";
  public final static String ImportMetadata = "ImportMetadata";
  
  public final static String DSModelDBCreate = "DSModelDBCreate";
  public final static String DSModelTableCreate = "DSModelTableCreate";
  public final static String DSModelDeployFunction = "DSModelDeployFunction";
  public final static String DSModelSchemaCreate = "DSModelSchemaCreate";
  public final static String DSModelCredentialCreate = "DSModelCredentialCreate";
  public final static String DSModelCredentialUpdate = "DSModelCredentialUpdate";
  public final static String DSModelCredentialDelete = "DSModelCredentialDelete";
  public final static String DSModelCachingViews = "DSModelCachingViews";
  public final static String DSModelServiceUserCredentials = "DSModelServiceUserCredentials";
  
  public final static String RunOSCommand = "RunOSCommand";
}
