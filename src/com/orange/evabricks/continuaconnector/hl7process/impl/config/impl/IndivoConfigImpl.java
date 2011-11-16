package com.orange.evabricks.continuaconnector.hl7process.impl.config.impl;

import com.orange.evabricks.continuaconnector.hl7process.impl.config.IndivoConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author tmdn5264
 */
@Component("indivoConfig")
public class IndivoConfigImpl implements IndivoConfig {

  @Value("${PHA_CONSUMER_KEY}")
  private String PHA_CONSUMER_KEY = "";
  @Value("${PHA_CONSUMER_SECRET}")
  private String PHA_CONSUMER_SECRET = "";
  @Value("${PHA_APP_ID}")
  private String PHA_APP_ID = "";
  @Value("${INDIVO_SERVER_URL}")
  private String INDIVO_SERVER_URL = "";
  @Value("${INDIVO_UI_SERVER_URL}")
  private String INDIVO_UI_SERVER_URL = "";

  public IndivoConfigImpl() {
  }

  @Override
  public String getPHA_CONSUMER_KEY() {
    return PHA_CONSUMER_KEY;
  }

  @Override
  public String getPHA_CONSUMER_SECRET() {
    return PHA_CONSUMER_SECRET;
  }

  @Override
  public String getPHA_APP_ID() {
    return PHA_APP_ID;
  }

  @Override
  public String getINDIVO_SERVER_URL() {
    return INDIVO_SERVER_URL;
  }

  @Override
  public String getINDIVO_UI_SERVER_URL() {
    return INDIVO_UI_SERVER_URL;
  }

  public void setINDIVO_SERVER_URL(String INDIVO_SERVER_URL) {
    this.INDIVO_SERVER_URL = INDIVO_SERVER_URL;
  }

  public void setINDIVO_UI_SERVER_URL(String INDIVO_UI_SERVER_URL) {
    this.INDIVO_UI_SERVER_URL = INDIVO_UI_SERVER_URL;
  }

  public void setPHA_APP_ID(String PHA_APP_ID) {
    this.PHA_APP_ID = PHA_APP_ID;
  }

  public void setPHA_CONSUMER_KEY(String PHA_CONSUMER_KEY) {
    this.PHA_CONSUMER_KEY = PHA_CONSUMER_KEY;
  }

  public void setPHA_CONSUMER_SECRET(String PHA_CONSUMER_SECRET) {
    this.PHA_CONSUMER_SECRET = PHA_CONSUMER_SECRET;
  }
}
