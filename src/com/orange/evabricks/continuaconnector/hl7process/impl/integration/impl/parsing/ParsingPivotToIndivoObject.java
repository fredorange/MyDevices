package com.orange.evabricks.continuaconnector.hl7process.impl.integration.impl.parsing;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.applicationErrorCodes.ApplicationErrorCodes;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.ApplicationInternalErrorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.RequiredFieldMissingException;
import com.orange.evabricks.continuaconnector.hl7process.impl.integration.indivo.model.MyIndivoGroupedVitalSignsAndEquipment;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2.AggregatedMeasures;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2.CodedInfo;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2.Device;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2.Measure;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2.NumericValue;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2.Observation;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2.Observations;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2.SingleMeasure;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.segmentidentification.MeasureAndDeviceTypes.DeviceItem;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.segmentidentification.SegmentIdentificationManager;
import com.orange.jlinx.document.medical.Equipment;
import com.orange.jlinx.document.medical.VitalSign;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author tmdn5264
 */
@Component("parsingPivotToIndivoObject")
@Scope("singleton")
public class ParsingPivotToIndivoObject {

  private static final Logger logger = Logger.getLogger(ParsingPivotToIndivoObject.class);
  @Resource(name = "applicationErrorCodes")
  private ApplicationErrorCodes applicationErrorCodes;
  @Resource(name = "segmentIdentificationManager")
  private SegmentIdentificationManager segmentIdentificationManager;

  /**
   * Parse Observations object into a list of objects containing grouped vital signs and equipment
   * @param observations
   * @return
   * @throws ApplicationInternalErrorException
   */
  public List<MyIndivoGroupedVitalSignsAndEquipment> getVitalSignsFromObservations(Observations observations) throws ApplicationInternalErrorException, RequiredFieldMissingException {

    List<MyIndivoGroupedVitalSignsAndEquipment> list = new ArrayList<MyIndivoGroupedVitalSignsAndEquipment>();

    for (Observation observation : observations.getObservations()) {
      for (AggregatedMeasures aggregat : observation.getAggregatedMeasuresCollection()) {
        MyIndivoGroupedVitalSignsAndEquipment group = new MyIndivoGroupedVitalSignsAndEquipment();
        list.add(group);
        // set equipment
        group.setEquipment(getEquipment(aggregat.getDevice()));
        for (Measure measure : aggregat.getMeasures()) {
          if (!(measure instanceof SingleMeasure)) {
            throw new ApplicationInternalErrorException(applicationErrorCodes.getINTEGRATION_DevError_AggregateOfAggregateNoSupported());
          }
          group.getGroupedVitalSigns().add(getVitalSign((SingleMeasure) measure, observation.getStartDate(), observation.getEndDate(), observations.getMessageDate()));
        }
      }
    }


    return list;
  }

  /**
   * Parse Device object into Indivo Equipment object
   * @param device
   * @return
   * @throws ApplicationInternalErrorException
   */
  private Equipment getEquipment(Device device) throws ApplicationInternalErrorException, RequiredFieldMissingException {

    String name = device.getName();
    String id = device.getId() != null ? device.getId().getValueAsString() : null;

    String type = "";
    String vendor = "";
    String description = "";
    String specification = "";
    String certification = "";

    // for each device item from the map,
    // ask what kind of device item it is,
    // and update adequate equipment attribute
    for (Iterator<CodedInfo> i = device.getDeviceItems().keySet().iterator(); i.hasNext();) {
      //System.out.println(i.next());
      CodedInfo deviceItemType = i.next();
      DeviceItem deviceItem = segmentIdentificationManager.getDeviceItem(deviceItemType.getIdentifier(), deviceItemType.getNameOfCodingSystem());

      switch (deviceItem) {
        case SYSTEM_TYPE:
          type = device.getDeviceItems().get(deviceItemType).getValueAsString();
          break;
        case SYSTEM_MODEL:
          description += " MODEL=" + device.getDeviceItems().get(deviceItemType).getValueAsString();
          break;
        case SYSTEM_MANUFACTURER:
          vendor = device.getDeviceItems().get(deviceItemType).getValueAsString();
          break;
        case SYSTEM_ID:
          // if both device id are filled but are not equals, throw an exception
          if (id != null && device.getDeviceItems().get(deviceItemType).getValueAsString() != null && !id.equals(device.getDeviceItems().get(deviceItemType).getValueAsString())) {
            throw new ApplicationInternalErrorException(applicationErrorCodes.getINTEGRATION_IncoherentsSystemId().setValueComplement("Found " + id + " in MDS segment and " + device.getDeviceItems().get(deviceItemType).getValueAsString() + " in system-id segment."));
          }
          // if device id from OBX-18 is missing, take the SYSTEM-ID
          // (else, the device id from OBX-18 is kept)
          if (id == null) {
            id = device.getDeviceItems().get(deviceItemType).getValueAsString();
          }
          break;
        case PRODUCTION_SPECIFICATION_UNSPECIFIED:
          specification += " UNSPECIFIED=" + device.getDeviceItems().get(deviceItemType).getValueAsString();
          break;
        case PRODUCTION_SPECIFICATION_SERIAL:
          specification += " SERIAL=" + device.getDeviceItems().get(deviceItemType).getValueAsString();
          break;
        case PRODUCTION_SPECIFICATION_PART:
          specification += " PART=" + device.getDeviceItems().get(deviceItemType).getValueAsString();
          break;
        case PRODUCTION_SPECIFICATION_HARDWARE:
          specification += " HARDWARE=" + device.getDeviceItems().get(deviceItemType).getValueAsString();
          break;
        case PRODUCTION_SPECIFICATION_SOFTWARE:
          specification += " SOFTWARE=" + device.getDeviceItems().get(deviceItemType).getValueAsString();
          break;
        case PRODUCTION_SPECIFICATION_FIRMWARE:
          specification += " FIRMWARE=" + device.getDeviceItems().get(deviceItemType).getValueAsString();
          break;
        case PRODUCTION_SPECIFICATION_PROTOCOL:
          specification += " PROTOCOL=" + device.getDeviceItems().get(deviceItemType).getValueAsString();
          break;
        case PRODUCTION_SPECIFICATION_GMDN_GROUP:
          specification += " GMDN_GROUP=" + device.getDeviceItems().get(deviceItemType).getValueAsString();
          break;
        case REGULATION_CERTIFICATION_AUTH_BODY:
          certification += " AUTH_BODY=" + device.getDeviceItems().get(deviceItemType).getValueAsString();
          break;
        case REGULATION_CERTIFICATION_CONTINUA_VERSION:
          certification += " CONTINUA_VERSION=" + device.getDeviceItems().get(deviceItemType).getValueAsString();
          break;
        case REGULATION_CERTIFICATION_CONTINUA_CERTIFIED_DEVICE_LIST:
          certification += " CONTINUA_CERTIFIED_DEVICE_LIST=" + device.getDeviceItems().get(deviceItemType).getValueAsString();
          break;
        case REGULATION_CERTIFICATION_CONTINUA_REGULATION_STATUS:
          certification += " CONTINUA_REGULATION_STATUS=" + device.getDeviceItems().get(deviceItemType).getValueAsString();
          break;
        case SYSTEM_TYPE_SPEC_LIST:
          description += " TIME_SPEC_LIST=" + device.getDeviceItems().get(deviceItemType).getValueAsString();
          break;
      }
    }
    // if device id is missing no OBX18 and no SYSTEM-ID item), throw an exception
    if (id == null) {
      throw new RequiredFieldMissingException(applicationErrorCodes.getINTEGRATION_MissingDeviceId());
    }

    return new Equipment(type, name, vendor, id, description, specification, certification, null, null);
  }

  /**
   * Parse SingleMeasure object into Indivo VitalSign object
   * @param measure
   * @param observationStartDate
   * @param observationEndDate
   * @param messageDate
   * @return
   * @throws ApplicationInternalErrorException
   */
  private VitalSign getVitalSign(SingleMeasure measure, Date observationStartDate, Date observationEndDate, Date messageDate) throws ApplicationInternalErrorException {

    logger.debug("Start getVitalSign ...");

    // the type of the measure (ex : blood pressure)
    URI uriVitalSign = null;
    try {
      uriVitalSign = new URI("http://codes.indivo.org/vitalsigns/");
    } catch (URISyntaxException e) {
      logger.error("Error when parsing vitalsigns uri.", e);
    }
    com.orange.jlinx.document.ext.CodedValue name = new com.orange.jlinx.document.ext.CodedValue(
            measure.getMeasureType().getText(),
            uriVitalSign,
            measure.getMeasureType().getIdentifier(),
            measure.getMeasureType().getNameOfCodingSystem());

    // the unit of the measure (ex : mmHg)
    URI uriUnit = null;
    try {
      uriUnit = new URI("http://codes.indivo.org/units/");
    } catch (URISyntaxException e) {
      logger.error("Error when parsing units uri.", e);
    }
    com.orange.jlinx.document.ext.CodedValue unit = new com.orange.jlinx.document.ext.CodedValue(
            measure.getValue().getUnit().getText(),
            uriUnit,
            measure.getValue().getUnit().getIdentifier(),
            measure.getValue().getUnit().getNameOfCodingSystem());

    // get the date of the measure
    MyDate date = chooseDate(measure.getDate(), observationStartDate, observationEndDate, messageDate);

    // set date type in comments
    String comments = "date type: " + date.getDateType().name();

    // get value (in Indivo VitalSign object, value should be a numeric value
    Double value = null;
    if (measure.getValue() == null || !(measure.getValue() instanceof NumericValue)) {
      String foundClass = measure.getValue() != null ? measure.getValue().getClass().getSimpleName() : "null";
      logger.error("Unsupported value type for vital sign: " + foundClass);
      throw new ApplicationInternalErrorException(applicationErrorCodes.getINTEGRATION_UnsupportedValueTypeForVitalSign().setValueComplement(" Found: " + foundClass));
    } else {
      value = new Double(((NumericValue) measure.getValue()).getValue());
    }

    // Creation of the VitalSign object
    return new VitalSign(date.getDate(), name, value, unit, "", "", comments);

  }

  private MyDate chooseDate(Date measureDate, Date observationStartDate, Date observationEndDate, Date messageDate) {
    logger.debug("Start chooseDate ...");
    MyDate date = null;
    if (measureDate != null) {
      date = new MyDate(measureDate, MyDate.DateType.OBX_date);
    } else if (observationStartDate != null) {
      date = new MyDate(observationStartDate, MyDate.DateType.OBR_start_date);
    } else if (observationEndDate != null) {
      date = new MyDate(observationEndDate, MyDate.DateType.OBR_end_date);
    } else if (messageDate != null) {
      date = new MyDate(messageDate, MyDate.DateType.MSH_date);
    }
    logger.debug("date choosen: " + date.toString());
    return date;
  }
}

/**
 * Class containing the choosen date of the measure and the type of this date (OBX, OBR, MSH)
 * @author tmdn5264
 */
class MyDate {

  private Date date;

  public enum DateType {

    OBX_date, OBR_start_date, OBR_end_date, MSH_date
  };
  private DateType dateType;

  public MyDate() {
  }

  public MyDate(Date date, DateType dateType) {
    this.date = date;
    this.dateType = dateType;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public DateType getDateType() {
    return dateType;
  }

  public void setDateType(DateType dateType) {
    this.dateType = dateType;
  }

  @Override
  public String toString() {
    return dateType.name() + " " + date;
  }
}
