package com.orange.evabricks.continuaconnector.hl7process.impl.integration.indivo.model;

import com.orange.jlinx.document.medical.Equipment;
import com.orange.jlinx.document.medical.VitalSign;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the model of vital signs that will be registered into Indivo,
 * ie some vital sign that will be linked together and linked to an equipment.
 * @author tmdn5264
 */
public class MyIndivoGroupedVitalSignsAndEquipment {

  private List<VitalSign> groupedVitalSigns;
  private Equipment equipment;

  public MyIndivoGroupedVitalSignsAndEquipment(){
    this.groupedVitalSigns = new ArrayList<VitalSign>();
  }

  public Equipment getEquipment() {
    return equipment;
  }

  public void setEquipment(Equipment equipment) {
    this.equipment = equipment;
  }

  public List<VitalSign> getGroupedVitalSigns() {
    return groupedVitalSigns;
  }

}
