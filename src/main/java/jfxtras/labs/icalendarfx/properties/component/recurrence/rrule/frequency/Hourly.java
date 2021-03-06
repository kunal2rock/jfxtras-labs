package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency;

import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.FrequencyType;

/** HOURLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Hourly extends FrequencyAbstract<Hourly>
{
    // Constructor
    public Hourly() { super(FrequencyType.HOURLY); }
    
    public Hourly(Frequency source) { super(source); }
}
