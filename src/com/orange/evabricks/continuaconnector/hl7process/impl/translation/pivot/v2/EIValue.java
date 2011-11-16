package com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2;

/**
 *
 * @author tmdn5264
 */
public class EIValue extends Value {

  private EIInfo value;

  public EIValue() {
  }

  public EIValue(EIInfo value) {
    this.value = value;
  }

  public EIInfo getValue() {
    return value;
  }

  public void setValue(EIInfo value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object obj) {
   if(obj==null){
      return false;
    }
    if(!(obj instanceof EIValue)){
      return false;
    }
   return this.value.equals(((EIValue)obj).getValue());
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }


  @Override
  public String getValueAsString() {
   return this.value.toString();
  }


 
}
