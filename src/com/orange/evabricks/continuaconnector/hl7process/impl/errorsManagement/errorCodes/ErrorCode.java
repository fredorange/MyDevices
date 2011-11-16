package com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes;

/**
 * This class describes the structure of an error code.
 * An error code can be for example 
 * an "application error code" (error code specific to this application) 
 * or a "HL7 error code" (defined in HL7 Table 0357).
 * @author tmdn5264
 *
 */
public class ErrorCode {

  private String value;
  private String description;
  private String nameOfCodingSystem;

  public ErrorCode(String value, String description,
          String nameOfCodingSystem) {
    super();
    this.value = value;
    this.description = description;
    this.nameOfCodingSystem = nameOfCodingSystem;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getNameOfCodingSystem() {
    return nameOfCodingSystem;
  }

  public void setNameOfCodingSystem(String nameOfCodingSystem) {
    this.nameOfCodingSystem = nameOfCodingSystem;
  }

  public ErrorCode setValueComplement(String valueComplement) {
    this.description += valueComplement;
    return this;
  }

  @Override
  public String toString() {
    return "ErrorCode [value=" + value + ", description=" + description
            + ", nameOfCodingSystem=" + nameOfCodingSystem + "]";
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ErrorCode)) {
      return false;
    }
    ErrorCode e = (ErrorCode) o;
    if (this.nameOfCodingSystem.equals(e.nameOfCodingSystem) && this.value.equals(e.value)) {
      return true;
    }
    return false;
  }
/**
 * This method has to be overrided if equals is overrided.
 * @return
 */
  @Override
  public int hashCode() {
    int hash = 5;
    hash = 61 * hash + (this.value != null ? this.value.hashCode() : 0);
    hash = 61 * hash + (this.nameOfCodingSystem != null ? this.nameOfCodingSystem.hashCode() : 0);
    return hash;
  }

 
  
}
