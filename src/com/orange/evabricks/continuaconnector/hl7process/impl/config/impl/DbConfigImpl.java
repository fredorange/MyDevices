package com.orange.evabricks.continuaconnector.hl7process.impl.config.impl;

import com.orange.evabricks.continuaconnector.hl7process.impl.config.DbConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author tmdn5264
 */
@Component("dbConfig")
public class DbConfigImpl implements DbConfig {

  @Value("${DB_DRIVER}")
  private String DB_DRIVER = "";
  @Value("${DB_URL}")
  private String DB_URL = "";
  @Value("${DB_USER}")
  private String DB_USER = "";
  @Value("${DB_PASSWORD}")
  private String DB_PASSWORD = "";

  public DbConfigImpl() {
  }

  @Override
  public String getDB_DRIVER() {
    return DB_DRIVER;
  }

  @Override
  public String getDB_URL() {
    return DB_URL;
  }

  @Override
  public String getDB_USER() {
    return DB_USER;
  }

  @Override
  public String getDB_PASSWORD() {
    return DB_PASSWORD;
  }

  public void setDB_DRIVER(String DB_DRIVER) {
    this.DB_DRIVER = DB_DRIVER;
  }

  public void setDB_PASSWORD(String DB_PASSWORD) {
    this.DB_PASSWORD = DB_PASSWORD;
  }

  public void setDB_URL(String DB_URL) {
    this.DB_URL = DB_URL;
  }

  public void setDB_USER(String DB_USER) {
    this.DB_USER = DB_USER;
  }
}
