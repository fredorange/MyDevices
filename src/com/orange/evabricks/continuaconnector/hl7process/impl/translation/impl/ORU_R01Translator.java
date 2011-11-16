package com.orange.evabricks.continuaconnector.hl7process.impl.translation.impl;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v26.datatype.CWE;
import ca.uhn.hl7v2.model.v26.datatype.EI;
import ca.uhn.hl7v2.model.v26.datatype.NA;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;
import ca.uhn.hl7v2.model.v26.segment.MSH;
import ca.uhn.hl7v2.model.v26.segment.OBR;
import ca.uhn.hl7v2.model.v26.segment.OBX;
import com.orange.evabricks.continuaconnector.hl7process.impl.commons.HL7MessageParser;
import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.ErrorCode;
import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.applicationErrorCodes.ApplicationErrorCodes;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.ApplicationInternalErrorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.RequiredFieldMissingException;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.Translator;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.PivotObject;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2.AggregatedMeasures;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2.CodedInfo;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2.CodedValue;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2.Device;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2.EIInfo;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2.EIValue;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2.NumericValue;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2.Observation;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2.Observations;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2.SingleMeasure;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2.StringValue;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2.Value;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.segmentidentification.MeasureAndDeviceTypes.MedicalDeviceSystem;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.segmentidentification.SegmentIdentificationManager;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.util.HapiTypeAdapter;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 *
 * @author tmdn5264
 */
@Component("oRU_R01Translator")
public class ORU_R01Translator implements Translator {

  private static final Logger logger = Logger.getLogger(ORU_R01Translator.class);
  @Resource(name = "applicationErrorCodes")
  private ApplicationErrorCodes applicationErrorCodes;
  @Resource(name = "hl7MessageParser")
  private HL7MessageParser hl7MessageParser;
  @Resource(name = "segmentIdentificationManager")
  private SegmentIdentificationManager segmentIdentificationManager;
  private boolean acceptAllSegments = true;

  @Override
  public PivotObject translate(String hl7Message) throws ContinuaConnectorException {
    logger.debug("Start executing translate ...");

    if (hl7Message == null) {
      logger.warn("HL7 message should not be null.");
      throw new ApplicationInternalErrorException(applicationErrorCodes.getTRANSLATION_DevError_NullMessageError());
    }

    ORU_R01 oruMessage = hl7MessageParser.getORU_R01(hl7Message);
    if (oruMessage == null) {
      logger.warn("ORU_R01 message should not be null.");
      throw new ApplicationInternalErrorException(applicationErrorCodes.getTRANSLATION_DevError_BadInputMessageType().setValueComplement(ORU_R01.class.getName()));
    }

    try {
      return getObservationsByORUMessage(oruMessage);
    } catch (ApplicationInternalErrorException e) {
      throw e;
    } catch (HL7Exception e) {
      throw new ApplicationInternalErrorException(applicationErrorCodes.getTRANSLATION_HapiError(), e);
    }

  }

  /***
   * Parse an ORU_R01 message into an Observations object
   * @param ORU_R01 message
   * @return A DeviceObservation object
   * @throws HL7Exception
   */
  private Observations getObservationsByORUMessage(ORU_R01 message) throws ApplicationInternalErrorException, DataTypeException, RequiredFieldMissingException {
    logger.debug("start executing getObservationsByORUMessage ...");

    Observations observations = new Observations();

    MSH msh = message.getMSH();
    // String messageControlId = msh.getMsh10_MessageControlID().getValue();
    observations.setMessageDate(HapiTypeAdapter.getDate(msh.getDateTimeOfMessage()));

    // for each OBR: create an Observation object with start date and end date
    for (int i = 0; i < message.getPATIENT_RESULT().getORDER_OBSERVATIONReps(); i++) {
      OBR obr = message.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBR();
      logger.info("current OBR: " + OBRtoString(obr));

      // common infos to all aggregates will be stored here,
      // and put into the aggregate device when creating each aggregate.
      Map<CodedInfo, Value> commonDeviceItems = new HashMap<CodedInfo, Value>();

      Observation observation = new Observation();
      observations.getObservations().add(observation);

      observation.setStartDate(HapiTypeAdapter.getDate(obr.getObr7_ObservationDateTime()));
      observation.setEndDate(HapiTypeAdapter.getDate(obr.getObr8_ObservationEndDateTime()));
      logger.debug("OBR start date: " + observation.getStartDate());
      logger.debug("OBR end date: " + observation.getEndDate());

      // This map is used to know the observation identifier (OBX-3) for each MDS
      Map<String, AggregatedMeasures> mappingObx3MDS = new HashMap<String, AggregatedMeasures>();

      // for each OBX: identify the nature of the OBX segment (measure or device info) and update Observation object
      for (int j = 0; j < message.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATIONReps(); j++) {
        OBX obx = message.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX();
        logger.info("current OBX:" + OBXtoString(obx));

        String subId = obx.getObx4_ObservationSubID().getValue();
        logger.debug("current OBX sub-ID: " + subId);
        String[] splittedSubId = subId.split("\\.");
        logger.debug("splitted subId length: " + splittedSubId.length);

        // Define the nature of the OBX segment, function of the pattern of the subId.
        switch (splittedSubId.length) {
          case 0:
            logger.error("sub-ID should contain at least one number.");
            throw new ApplicationInternalErrorException(applicationErrorCodes.getTRANSLATION_VoidObxSubId());
          case 1:
            logger.debug("current OBX sub-ID match pattern MDS");
            // OBX that identifies the Medical Device System (ex : MDC_DEV_SPEC_PROFILE_BP)
            // data will be grouped under this item.

            // check if this MDS is valid.
            MedicalDeviceSystem mds = segmentIdentificationManager.getMedicalDeviceSystem(obx.getObx3_ObservationIdentifier().getCwe1_Identifier().getValue(), obx.getObx3_ObservationIdentifier().getCwe3_NameOfCodingSystem().getValue());


            if (MedicalDeviceSystem.UNKNOWN.equals(mds)) {
              if (!acceptAllSegments) {
                throw new ApplicationInternalErrorException(applicationErrorCodes.getTRANSLATION_UnknownMedicalDeviceSystem().setValueComplement(getUnknownMedicalDeviceSystem(obx)));
              }
              // else, if the mds is unknown, it won't throw an exception, but the mds will be treated as a mds named "unknown"
            }


            // if the MDS is a valid MDS: create the AggregatedMeasures object and add it to the collection.
            AggregatedMeasures aggregat = new AggregatedMeasures();
            aggregat.setMeasureType(getCodedInfo(obx.getObx3_ObservationIdentifier()));
            observation.getAggregatedMeasuresCollection().add(aggregat);

            // set device basic infos
            Device device = new Device();
            EIInfo deviceId = getEIInfo(obx.getObx18_EquipmentInstanceIdentifier(0));
//            if(deviceId.getEntityIdentifier()==null){
//              ErrorCode errorCode = applicationErrorCodes.getTRANSLATION_MissingDeviceId();
//              errorCode.setValueComplement(" OBX-18 entity identifier is missing in segment OBX|" + obx.getObx1_SetIDOBX().getValue() + ".");
//              throw new RequiredFieldMissingException(errorCode);
//            }
//            if(deviceId.getNamespace()==null){
//              ErrorCode errorCode = applicationErrorCodes.getTRANSLATION_MissingDeviceId();
//              errorCode.setValueComplement(" OBX-18 namespace is missing in segment OBX|" + obx.getObx1_SetIDOBX().getValue() + ".");
//              throw new RequiredFieldMissingException(errorCode);
//            }
            device.setId(new EIValue(deviceId));
            device.setName(mds.name());
            // put common device items that has previously been stored in commonDeviceItems
            device.getDeviceItems().putAll(commonDeviceItems);
            aggregat.setDevice(device);

            // and update the mapping object
            mappingObx3MDS.put(splittedSubId[0], aggregat);

            break;
          case 2:
            logger.debug("current OBX sub-ID match pattern MDS.VMD");
            logger.error("pattern MDS.VMD is not used in Continua.");
            throw new ApplicationInternalErrorException(applicationErrorCodes.getTRANSLATION_VirtualMedicalDeviceShouldNotBeUsedInContinua().setValueComplement(" Found:" + subId + " in OBX|" + obx.getObx1_SetIDOBX()));
          case 3:
            logger.debug("current OBX sub-ID match pattern MDS.VMD.CHANNEL");
            // header of grouped measures
            // Not used yet. We group only under the Medical Device System.
            logger.debug("Grouped measures reprensented by pattern MDS.VMD.CHANNEL are not taken into account yet.");
            break;
          case 4:
            logger.debug("current OBX sub-ID match pattern MDS.VMD.CHANNEL.METRIC");
            // a measure or a device info


            // get the associated AggregatedMeasures object.
            AggregatedMeasures correspondingAggregat = mappingObx3MDS.get(splittedSubId[0]);
            logger.debug("aggregat corresponding to subId " + splittedSubId[0] + " : " + correspondingAggregat);



            if (segmentIdentificationManager.isDevice(obx.getObx3_ObservationIdentifier().getCwe1_Identifier().getValue(), obx.getObx3_ObservationIdentifier().getCwe3_NameOfCodingSystem().getValue())) {
              setDeviceInfo(obx, correspondingAggregat, commonDeviceItems);

            } else if (correspondingAggregat == null) {
              // if item is not a device item and has no parent, it is an error (measure item should have a parent)
              logger.error("non-device item has no MDS parent");
              throw new ApplicationInternalErrorException(applicationErrorCodes.getTRANSLATION_MDSParentNotFoundForItem().setValueComplement(" in OBX|" + obx.getObx1_SetIDOBX()));

            } else if (segmentIdentificationManager.isMeasure(correspondingAggregat.getMeasureType().getIdentifier(), correspondingAggregat.getMeasureType().getNameOfCodingSystem(), obx.getObx3_ObservationIdentifier().getCwe1_Identifier().getValue(), obx.getObx3_ObservationIdentifier().getCwe3_NameOfCodingSystem().getValue())) {
              // if item is a measure and has a parent
              setSingleMeasure(obx, correspondingAggregat);
            } else {
              // if measure is not a known device item or measure item
              if (!acceptAllSegments) {
                throw new ApplicationInternalErrorException(applicationErrorCodes.getTRANSLATION_UnknownMeasureOrDevice().setValueComplement(getUnknownMeasureOrDeviceItemForMDS(obx, splittedSubId[0])));
              } else {
                MedicalDeviceSystem aggregatMds = segmentIdentificationManager.getMedicalDeviceSystem(correspondingAggregat.getMeasureType().getIdentifier(), correspondingAggregat.getMeasureType().getNameOfCodingSystem());
                if (MedicalDeviceSystem.UNKNOWN.equals(aggregatMds)) {
                  // if the aggregate corresponds to an unknown MDS, we accept all segments.
                  logger.error("Measure is not a known device item or measure item (but it will be treated as a single measure).");
                  setSingleMeasure(obx, correspondingAggregat);
                } else {
                  // if the measure take part of a known aggregate, we don't accept unknown segments.
                  throw new ApplicationInternalErrorException(applicationErrorCodes.getTRANSLATION_UnknownMeasureOrDevice().setValueComplement(getUnknownMeasureOrDeviceItemForMDS(obx, splittedSubId[0])));
                }

              }

            }
            break;
          default:
            logger.debug("current OBX sub-ID match pattern MDS.VMD.CHANNEL.METRIC.FACET or more");
            logger.debug("such pattern is not taken into account yet");
            break;
        }

      }
    }
    return observations;
  }

  /**
   * Put in the model the device info found in current segment
   * @param obx
   * @param correspondingAggregat
   * @param commonDeviceItems
   */
  private void setDeviceInfo(OBX obx, AggregatedMeasures correspondingAggregat, Map<CodedInfo, Value> commonDeviceItems) {

    // if device item
    CodedInfo codedInfo = getCodedInfo(obx.getObx3_ObservationIdentifier());
    Value value = getValue(obx);
    logger.debug("device item found: " + codedInfo.toString() + " / " + value.toString());

    if (correspondingAggregat == null) {
      // if no parent, put device item in commonDeviceItems (this item will be added when creating the aggregate)
      commonDeviceItems.put(codedInfo, value);
      logger.debug("device item has no parent and has been put in commonDeviceItems");
    } else {
      // if the item has a parent, put the device info in the parent
      correspondingAggregat.getDevice().getDeviceItems().put(codedInfo, value);
      logger.debug("device item has a parent and has been put in the corresponding AggregatedMeasures object");
    }

  }

  /**
   * Put in the model the single measure found in current segment
   * @param obx
   * @param correspondingAggregat
   * @throws DataTypeException
   * @throws ApplicationInternalErrorException
   */
  private void setSingleMeasure(OBX obx, AggregatedMeasures correspondingAggregat) throws DataTypeException, ApplicationInternalErrorException {

    SingleMeasure measure = new SingleMeasure();
    measure.setDate(HapiTypeAdapter.getDate(obx.getObx14_DateTimeOfTheObservation()));
    measure.setMeasureType(getCodedInfo(obx.getObx3_ObservationIdentifier()));
    measure.setValue(getValue(obx));
    if (segmentIdentificationManager.isValidUnitForMeasureType(obx.getObx3_ObservationIdentifier().getCwe1_Identifier().getValue(), obx.getObx3_ObservationIdentifier().getCwe3_NameOfCodingSystem().getValue(), obx.getObx6_Units().getCwe1_Identifier().getValue(), obx.getObx6_Units().getCwe3_NameOfCodingSystem().getValue())) {
      measure.getValue().setUnit(getCodedInfo(obx.getObx6_Units()));
    } else {
      if (!acceptAllSegments) {
        throw new ApplicationInternalErrorException(applicationErrorCodes.getTRANSLATION_UncompatibleUnit().setValueComplement(getUncompatibleUnitError(obx)));
      } else {
        measure.getValue().setUnit(getCodedInfo(obx.getObx6_Units()));
      }
    }
    correspondingAggregat.getMeasures().add(measure);
    logger.debug("measure found: " + measure.toString());

  }

  /**
   * Get the string representing the OBR segment
   * @param obr OBR segment
   * @return  string representing the OBR segment
   */
  private String OBRtoString(OBR obr) {
    try {
      return obr.encode();
    } catch (HL7Exception e) {
      logger.error("An error occured when encoding OBR object to a string.", e);
      return "OBR|" + obr.getObr1_SetIDOBR();
    }
  }

  /**
   * Get the string representing the OBX segment
   * @param obx OBX segment
   * @return A String representing the OBX segment
   */
  private String OBXtoString(OBX obx) {
    try {
      return obx.encode();


    } catch (HL7Exception e) {
      logger.error("An error occured when encoding OBX object to a string.", e);


      return "OBX|" + obx.getObx1_SetIDOBX();

    }
  }

  /**
   * Get a string representing the CWE field
   * @param cwe CWE field
   * @return a string representing the CWE field
   */
  private String CWEtoString(CWE cwe) {
    try {
      return cwe.encode();


    } catch (HL7Exception e) {
      logger.error("An error occured when encoding OBX object to a string.", e);


      return cwe.getCwe1_Identifier() + "^" + cwe.getCwe2_Text() + "^" + cwe.getCwe3_NameOfCodingSystem();

    }
  }

  /**
   * Get a string used to log the location of the error field.
   * Example: 150022^MDC_PRESS_BLD_NONINV_DIA^MDC in OBX|5
   * @param obx the OBX segment containing the error field to log
   * @return a string used to log the location of the error field.
   */
  private String getUnknownMedicalDeviceSystem(OBX obx) {
    return " in OBX|" + obx.getObx1_SetIDOBX() + CWEtoString(obx.getObx3_ObservationIdentifier());
  }

  private String getUnknownMeasureOrDeviceItemForMDS(OBX measureOrDeviceItemObx, String parentSubId) {
    return " in OBX|" + measureOrDeviceItemObx.getObx1_SetIDOBX() + ", measure or device item " + CWEtoString(measureOrDeviceItemObx.getObx3_ObservationIdentifier()) + " unknown for MDS with sub-ID (OBX-4) " + parentSubId;
  }

  /**
   * Get a string used to log the location of the error field.
   * Example: 266016^MDC_DIM_MMHG^MDC in OBX|5
   * @param obx the OBX segment containing the error field to log
   * @return a string used to log the location of the error field.
   */
  private String getUncompatibleUnitError(OBX obx) {
    return " in OBX|" + obx.getObx1_SetIDOBX() + ", unit=" + CWEtoString(obx.getObx6_Units()) + "incompatible with measure type " + CWEtoString(obx.getObx3_ObservationIdentifier());
  }

  /**
   * Convert Hapi CWE type to local CodedInfo type
   * @param cwe CWE to convert
   * @return CodedInfo
   */
  private CodedInfo getCodedInfo(CWE cwe) {
    return new CodedInfo(cwe.getCwe1_Identifier().getValue(), cwe.getCwe2_Text().getValue(), cwe.getCwe3_NameOfCodingSystem().getValue());
  }

  private EIInfo getEIInfo(EI ei) {

    return new EIInfo(ei.getEi1_EntityIdentifier().getValue(), ei.getEi2_NamespaceID().getValue());
  }

  private String getNAInfo(NA na) {
    try {
      return na.encode();
    } catch (HL7Exception e) {
      logger.error("Error when encoding NA value.", e);
      return "";
    }
  }

  /**
   * Create a Value object from OBX-2 and OBX-5
   * @param obx OBX segment
   * @return a Value object corresponding to the value of the OBX-5 field
   */
  private Value getValue(OBX obx) {
    Value value = null;

    if ("NM".equals(obx.getObx2_ValueType().getValue())) {
      value = new NumericValue(Double.parseDouble(obx.getObx5_ObservationValue(0).getData().toString()));
    } else if ("CWE".equals(obx.getObx2_ValueType().getValue())) {
      value = new CodedValue(getCodedInfo((CWE) obx.getObx5_ObservationValue(0).getData()));
    } else if ("ST".equals(obx.getObx2_ValueType().getValue())) {
      value = new StringValue(obx.getObx5_ObservationValue(0).getData().toString());
    } else if ("EI".equals(obx.getObx2_ValueType().getValue())) {
      value = new EIValue(getEIInfo((EI) obx.getObx5_ObservationValue(0).getData()));
    }
//    else if ("NA".equals(obx.getObx2_ValueType().getValue())) {
//      value = new StringValue(getNAInfo((NA) obx.getObx5_ObservationValue(0).getData()));
//    } 
    else {
      logger.error(obx.getObx2_ValueType().getValue() + " data type not supported");
      try {
        value = new StringValue(obx.getObx5_ObservationValue(0).getData().encode());
      } catch (HL7Exception e) {
        logger.error("Unable to encode value.", e);
      }
    }

    return value;
  }
}
