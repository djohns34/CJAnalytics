package edu.calpoly.codastjegga.cjanalyticsapp.event;


/**
 * Record Type corresponds to the picklist of value that can 
 * be in ValueType__c column;
 * @author Gagandeep S. Kohli
 *
 */
public enum EventType {
  Currency (EventFields.CurrencyV),
  Float (EventFields.FloatV),
  Number (EventFields.NumberV),
  Locale (EventFields.LocaleV),
  Text (EventFields.TextV);
  
  private EventFields eventField;
  
  EventType (EventFields eventField) {
    this.eventField = eventField;
  }
  
  public EventFields getEventField () {
    return this.eventField;
  }
  
};

