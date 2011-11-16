package com.orange.evabricks.continuaconnector.hl7process.impl.integration.credentialsStorage.impl;

import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.ErrorCode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.applicationErrorCodes.ApplicationErrorCodes;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.ApplicationInternalErrorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.integration.credentialsStorage.DbConnection;
import com.orange.evabricks.continuaconnector.hl7process.impl.integration.credentialsStorage.DbQueries;
import com.orange.evabricks.continuaconnector.hl7process.impl.integration.credentialsStorage.model.Credential;

@Component("dbQueries")
@Scope("singleton")
public class DbQueriesImpl implements DbQueries {

  private static final Logger logger = Logger.getLogger(DbQueriesImpl.class);
  @Resource(name = "dbConnection")
  private DbConnection dbConnection;
  @Resource(name = "applicationErrorCodes")
  private ApplicationErrorCodes applicationErrorCodes;

  public DbQueriesImpl() {
  }

  @Override
  public void saveCredential(Credential credential) throws ContinuaConnectorException {

    logger.debug("Start saving credentials ...");

    String queryString = "insert into credentials(accessKey,controlKey,indivo_token,indivo_tokenSecret,indivo_recordId) values('" + credential.getAccessKey() + "','" + credential.getControlKey() + "','" + credential.getToken() + "','" + credential.getTokenSecret() + "','" + credential.getRecordId() + "');";
    logger.debug("query : " + queryString);

    int result = 0;
    try {
      Statement stmt = dbConnection.getConnection().createStatement();
      result = stmt.executeUpdate(queryString);
      if (1 != result) {
        throw new ApplicationInternalErrorException(applicationErrorCodes.getCOMMON_DatabaseError_ErrorExecutingQuery());
      }
    } catch (SQLException e) {
      throw new ApplicationInternalErrorException(applicationErrorCodes.getCOMMON_DatabaseError_ErrorSavingCredentials(), e);
    } finally {
      if (dbConnection != null) {
        dbConnection.closeConnection();
      }
    }
  }

  @Override
  public void updateAccessToken(String indivo_recordId, String indivo_token,
          String indivo_tokenSecret) throws ContinuaConnectorException {
    logger.debug("Start updating access token ...");

    String queryString = "update credentials set indivo_token='" + indivo_token + "', indivo_tokenSecret='" + indivo_tokenSecret + "' where indivo_recordId='" + indivo_recordId + "';";
    logger.debug("query : " + queryString);

    int result = 0;
    try {
      Statement stmt = dbConnection.getConnection().createStatement();
      result = stmt.executeUpdate(queryString);
      if (1 != result) {
        ErrorCode errorCode = applicationErrorCodes.getCOMMON_DatabaseError_ErrorExecutingQuery();
        throw new ApplicationInternalErrorException(errorCode);
      }
    } catch (SQLException e) {
      ErrorCode errorCode = applicationErrorCodes.getCOMMON_DatabaseError_ErrorSavingCredentials();
      throw new ApplicationInternalErrorException(errorCode, e);
    } finally {
      if (dbConnection != null) {
        dbConnection.closeConnection();
      }
    }
  }

  @Override
  public Credential loadCredentialByAccessKey(String accessKey) throws ContinuaConnectorException {
    logger.debug("Start loading credential by accessKey ...");

    Credential credential = new Credential();

    String queryString = "select * from credentials where accessKey='" + accessKey + "';";
    logger.debug("query : " + queryString);

    try {
      logger.debug("Open connection and create statement ...");
      Statement stmt = dbConnection.getConnection().createStatement();
      ResultSet result = stmt.executeQuery(queryString);
      if (!result.next()) {
        ErrorCode err = applicationErrorCodes.getCOMMON_DatabaseError_ErrorLoadingCredentials_businessError();
        err.setValueComplement(" Accesskey " + accessKey + " not found in the table.");
        throw new ApplicationInternalErrorException(err);
      }
      credential.setAccessKey(result.getString("accessKey"));
      credential.setControlKey(result.getString("controlKey"));
      credential.setToken(result.getString("indivo_token"));
      credential.setTokenSecret(result.getString("indivo_tokenSecret"));
      credential.setRecordId(result.getString("indivo_recordId"));
    } catch (SQLException e) {
      ErrorCode errorCode = applicationErrorCodes.getCOMMON_DatabaseError_ErrorLoadingCredentials_infrastructureError();
      throw new ApplicationInternalErrorException(errorCode, e);
    } finally {
      if (dbConnection != null) {
        dbConnection.closeConnection();
      }
    }

    return credential;
  }

  @Override
  public Credential loadCredentialByRecordId(String indivo_recordId) throws ContinuaConnectorException {
    logger.debug("Start loading credential by recordId ...");

    Credential credential = new Credential();

    String queryString = "select * from credentials where indivo_recordId='" + indivo_recordId + "';";
    logger.debug("query : " + queryString);

    try {
      Statement stmt = dbConnection.getConnection().createStatement();
      ResultSet result = stmt.executeQuery(queryString);
      if (!result.next()) {
        ErrorCode err = applicationErrorCodes.getCOMMON_DatabaseError_ErrorLoadingCredentials_businessError();
        err.setValueComplement(" recordId " + indivo_recordId + " not found in the table.");
        throw new ApplicationInternalErrorException(err);
      }

      credential.setAccessKey(result.getString("accessKey"));
      credential.setControlKey(result.getString("controlKey"));
      credential.setToken(result.getString("indivo_token"));
      credential.setTokenSecret(result.getString("indivo_tokenSecret"));
      credential.setRecordId(result.getString("indivo_recordId"));


    } catch (SQLException e) {
      ErrorCode errorCode = applicationErrorCodes.getCOMMON_DatabaseError_ErrorLoadingCredentials_infrastructureError();
      throw new ApplicationInternalErrorException(errorCode, e);
    } finally {
      if (dbConnection != null) {
        dbConnection.closeConnection();
      }
    }

    return credential;
  }

  @Override
  public boolean isCredentialStored(String indivo_recordId) throws ContinuaConnectorException {
    logger.debug("Start executing isCredentialStored ...");

    boolean exists = false;

    String queryString = "select * from credentials where indivo_recordId='" + indivo_recordId + "';";
    logger.debug("query : " + queryString);

    try {
      Statement stmt = dbConnection.getConnection().createStatement();
      ResultSet result = stmt.executeQuery(queryString);
      if (result.next()) {
        result.getString("accessKey");
        exists = true;
      }
    } catch (SQLException e) {
      logger.debug("credential does not exist.");
    } finally {
      if (dbConnection != null) {
        dbConnection.closeConnection();
      }
    }

    return exists;
  }
}
