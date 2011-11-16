package com.orange.evabricks.continuaconnector.adapter.tooling.hl7processor.util;

import org.apache.log4j.Logger;

public class MessageCorrector {

  private static Logger logger = Logger.getLogger(MessageCorrector.class);

  /**
   * Correct the message by removing characters specific to web services
   * @param msg
   * @return
   */
  public static String correctWebserviceMessage2HL7Message(String msg) {
    if (msg == null) {
      return null;
    }

    // replace all occurences of \n by \r
    String corrected = msg.replace("\n", "\r");

    // if the message has been corrected (ie initial message contained \n), log the modification
    if (!msg.equals(corrected)) {
      int nbOccurences = msg.split("\n").length - 1;
      logger.error("Invalid incoming HL7 message: Message apparently contains "+nbOccurences+" of '\n'. Rule: Segment separator should be an '\\r' instead of '\\n'");
    }

    // delete \r at the beginning of the message
    int nb=0;
    while (corrected.startsWith("\r")) {
      nb++;
      corrected = corrected.replaceFirst("\r", "");      
    }
    if(nb!=0){
      logger.error("Invalid incoming HL7 message: Message starts with "+nb+"'\\r'. Rule: No '\\r' allowed at the begenning of the HL7 message");
    }
    return corrected;
  }

//  /**
//   * Correct the message to be compatible with the web services standard
//   * @param msg
//   * @return
//   */
//  public static String correctHL7Message2WebserviceMessage(String msg) {
//    if (msg == null) {
//      return null;
//    }
//    return msg.replace("\r", "\n");
//
//  }
}
