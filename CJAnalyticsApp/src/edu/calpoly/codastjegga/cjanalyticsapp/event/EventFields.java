package edu.calpoly.codastjegga.cjanalyticsapp.event;

/**
 * EventFields type
 * @author Gagandeep S. Kohli
 *
 */
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
  
  /** id that corresponds in TrackedEvent__c meta object **/
  private String columnId;

  /** 
   * Constuctor for EventFields
   * @param columnId column name
   */
  private EventFields(String columnId) {
      this.columnId = columnId;
  }
  
  /**
   * Getter for columnid
   * @return column id/name
   */
  public String getColumnId() {
    return this.columnId;
  }
}
