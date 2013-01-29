package edu.calpoly.codastjegga.cjanalyticsapp.event;

import java.math.BigDecimal;

/**
 * A CurrencyEvent corresponds to event whose value type is Currency
 * @author gagandeep
 *
 */
public class CurrencyEvent extends Event{
  //currency value of the event
  private BigDecimal currencyValue;
  
  /**
   * Constructor for CurrentEvent
   * @param id @see Event id
   * @param eventName @see Event eventName
   * @param currencyValue value of type BigDecimal to represent currency
   */
  public CurrencyEvent (String id, String eventName, BigDecimal currencyValue) {
    super (id, eventName);
    this.currencyValue = currencyValue;
  }
  
  /**
   * Setter for CurrencyValue
   * @param currencyValue currency value of this event
   */
  public void setCurrencyValue (BigDecimal currencyValue) {
    this.currencyValue = currencyValue;
  }
  
  /**
   * Getter for CurrencyValue
   * @return currency value of this event
   */
  public BigDecimal getCurrencyValue () {
    return this.currencyValue;
  }
}
