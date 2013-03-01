package edu.calpoly.codastjegga.cjanalyticsapp.event;

import java.math.BigDecimal;

/**
 * A CurrencyEvent corresponds to event whose value type is Currency
 * @author Gagandeep S. Kohli
 *
 */
public class CurrencyEvent extends Event<BigDecimal>{
  //currency value of the event
  private BigDecimal currencyValue;

  /**
   * Constructor for CurrentEvent
   * @param eventName
   *            @see {@link Event} 
   * @param deviceid
   *            @see {@link Event}
   *           
   * @param timestamp @see {@link Event}
   * @param databaseName @see {@link Event}
   * @param currencyValue value of type BigDecimal to represent currency
   */
  public CurrencyEvent (String eventName, String deviceId, String timestamp, String databaseName, BigDecimal currencyValue) {
    super(eventName, deviceId, timestamp, databaseName, currencyValue);
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
  public BigDecimal getValue () {
    return this.currencyValue;
  }

}
