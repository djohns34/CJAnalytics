package edu.calpoly.codastjegga.cjanalyticsapp.event;

/**
 * EventFields type
 * @author Gagandeep S. Kohli
 *
 */
public enum EventFields {
  CurrencyV("codastjegga__CurrencyValue__c"),
  FloatV("codastjegga__FloatValue__c"),
  NumberV("codastjegga__NumberValue__c"),
  LocaleV("codastjegga__LocaleValue__c"),
  TextV("codastjegga__TextValue__c"),
  Id("Id"),
  TimestampV("codastjegga__Timestamp__c"),
  EventName("codastjegga__EventName__c"),
  DeviceId("codastjegga__Device_Id__c"),
  ValueType("codastjegga__ValueType__c"),
  DatabaseName("codastjegga__DatabaseName__c");
  
  /** id that corresponds in codastjegga__TrackedEvent__c meta object **/
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
