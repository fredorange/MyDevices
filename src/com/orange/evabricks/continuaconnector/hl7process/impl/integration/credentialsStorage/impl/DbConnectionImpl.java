package com.orange.evabricks.continuaconnector.hl7process.impl.integration.credentialsStorage.impl;

import com.orange.evabricks.continuaconnector.hl7process.impl.config.DbConfig;
import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.ErrorCode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.applicationErrorCodes.ApplicationErrorCodes;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.ApplicationInternalErrorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.integration.credentialsStorage.DbConnection;

@Component("dbConnection")
@Scope("singleton")
public class DbConnectionImpl implements DbConnection {

  private static final Logger logger = Logger.getLogger(DbConnectionImpl.class);
  @Resource(name = "dbConfig")
  private DbConfig dbConfig;
  private Connection connection = null;
  @Resource(name = "applicationErrorCodes")
  protected ApplicationErrorCodes applicationErrorCodes;

  // TODO : utiliser spring et hibernate pour gérer l'accès à la bdd
  public DbConnectionImpl() {
  }

  @Override
  public Connection getConnection() throws ContinuaConnectorException {
    return initConnection(dbConfig.getDB_DRIVER(), dbConfig.getDB_URL(), dbConfig.getDB_USER(), dbConfig.getDB_PASSWORD());
  }

  private Connection initConnection(String dbDriver, String dbUrl, String dbUser, String dbPassword) throws ContinuaConnectorException {

    logger.debug("start openning db connection ...");
    try {
      Class.forName(dbDriver);
      Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
      logger.debug("Open db connection OK.");
      return conn;
    } catch (SQLException e) {
      logger.error("Error when openning db connection " + dbUrl, e);
      ErrorCode errorCode = applicationErrorCodes.getCOMMON_DatabaseError_ErrorOpenningDbConnection();
      errorCode.setValueComplement(". SQLException.");
      throw new ApplicationInternalErrorException(errorCode, e);
    } catch (ClassNotFoundException e) {
      logger.error("Error when openning db connection " + dbUrl, e);
      ErrorCode errorCode = applicationErrorCodes.getCOMMON_DatabaseError_ErrorOpenningDbConnection();
      errorCode.setValueComplement(". ClassNotFoundException: " + dbDriver + " not found");
      throw new ApplicationInternalErrorException(errorCode, e);

    } catch (Exception e) {
      logger.error("Error when openning db connection " + dbUrl, e);
      ErrorCode errorCode = applicationErrorCodes.getCOMMON_DatabaseError_ErrorOpenningDbConnection();
      errorCode.setValueComplement("Unexpected exception.");
      throw new ApplicationInternalErrorException(errorCode, e);
    }
  }

  @Override
  public void closeConnection() throws ContinuaConnectorException {
    logger.debug("Start closing db connexion ...");

    try {
      if (connection != null && !connection.isClosed()) {
        connection.close();
        connection = null;
      }
    } catch (SQLException e) {
      logger.error("Error when closing connection.", e);
      ErrorCode errorCode = applicationErrorCodes.getCOMMON_DatabaseError_ErrorClosingConnection();
      throw new ApplicationInternalErrorException(errorCode, e);
    }
  }
}
