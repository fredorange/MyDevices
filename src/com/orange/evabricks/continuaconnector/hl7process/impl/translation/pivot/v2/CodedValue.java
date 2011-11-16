package com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2;

/**
 * Value used in a single measure or in a device item.
 * @author tmdn5264
 */
public class CodedValue extends Value {

  private CodedInfo value;

  public CodedValue() {
  }

    public CodedValue(CodedInfo value) {
    this.value = value;
  }

  public CodedInfo getValue() {
    return value;
  }

  public void setValue(CodedInfo value) {
    this.value = value;
  }

   @Override
  public boolean equals(Object obj) {
   if(obj==null){
      return false;
    }
    if(!(obj instanceof CodedValue)){
      return false;
    }
   return this.value.equals(((CodedValue)obj).getValue());
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public String getValueAsString() {
    return value.toString();
  }

 
  
}
