package com.orange.evabricks.continuaconnector.adapter.tooling.hl7processor.decorator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author alain
 */
public class HL7MessageSniffer extends HL7MessageDecoratorAbstract {

  private static final Logger log = Logger.getLogger(HL7MessageSniffer.class);
  private Map<String, Stuff> map = new HashMap<String, Stuff>();

  @Override
  public String process(String hl7Msg) {
    snif(hl7Msg);
    display();
    return forward(hl7Msg);

  }

  @Override
  public String process(String hl7Msg, String onUserBehalf) {
    snif(hl7Msg);
    return forward(hl7Msg, onUserBehalf);
  }

  protected void snif(String msg) {
    try {
      String h = digestIt(msg);
      Stuff stuff = this.map.get(h);
      if (stuff == null) {
        stuff = new Stuff(msg);
        this.map.put(h, stuff);
      } 
      stuff.incHitCount();  
    }
    catch (Exception e) {
      throw new RuntimeException("something went wrong", e);
    }

  }

  protected String digestIt(String msg) throws Exception {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-512");
      byte[] dataBytes = new byte[1024];

      ByteArrayInputStream fis = new ByteArrayInputStream(msg.getBytes());
      int nread = 0;
      while ((nread = fis.read(dataBytes)) != -1) {
        md.update(dataBytes, 0, nread);
      }
      byte[] mdbytes = md.digest();

      StringBuilder hexString = new StringBuilder();
      for (int i = 0; i < mdbytes.length; i++) {
        hexString.append(Integer.toHexString(0xFF & mdbytes[i]));
      }
      String res = hexString.toString();
      System.out.println("Hex format : " + res);
      return res;
    }
    catch (NoSuchAlgorithmException e) {
      log.error(e);
      throw new Exception(e);
    }
    catch (IOException e) {
      log.error(e);
      throw new Exception(e);
    }
  }

  private void display() {
    StringBuilder sb = new StringBuilder();
    sb.append("--- content------------\n");
    for (Map.Entry<String, Stuff> e : this.map.entrySet()) {
      String h = e.getKey();
      Stuff s = e.getValue();
      sb.append(String.format("%s\t%s\n", h, s));
    }
    sb.append("--- fin content------------");
    System.out.println(sb.toString());
    log.debug(sb.toString());
  }

  class Stuff {

    private String hl7Msg;
    private int hitCount;

    public Stuff(String hl7Msg) {
      this.hl7Msg = hl7Msg;
      this.hitCount = 0;
    }

    public int incHitCount() {
      return ++this.hitCount;
    }

    @Override
    public String toString() {
      return String.format("(%d)%s", this.hitCount, this.hl7Msg);
    }
  }
}
