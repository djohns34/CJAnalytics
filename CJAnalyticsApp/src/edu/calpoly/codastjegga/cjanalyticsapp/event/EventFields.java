package edu.calpoly.codastjegga.cjanalyticsapp.event;

public enum EventFields {
  CurrencyV("CurrencyValue__c"),
  FloatV("FloatValue__c"),
  NumberV("NumberValue__c"),
  LocaleV("LocaleValue__c"),
  TextV("TextValue__c"),
  Id("Id"),
  TimestampV("Timestamp__c"),
  EventName("EventName__c"),
  DeviceId("Device_Id__c"),
  ValueType("ValueType__c");
  
  private String columnId;

  private EventFields(String columnId) {
      this.columnId = columnId;
  }
  
  public String getColumnId() {
    return this.columnId;
  }
}
