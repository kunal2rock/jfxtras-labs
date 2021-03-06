package jfxtras.labs.icalendarfx.properties.component.change;

import java.time.ZonedDateTime;

import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.PropertyBaseUTC;

/**
 * CREATED
 * Date-Time Created
 * RFC 5545, 3.8.7.1, page 136
 * 
 * This property specifies the date and time that the calendar information
 * was created by the calendar user agent in the calendar store.
 * 
 * The value MUST be specified as a date with UTC time.
 * 
 * Example:
 * CREATED:19960329T133000Z
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 * @see VJournal
 */
public class DateTimeCreated extends PropertyBaseUTC<DateTimeCreated>
{
    public DateTimeCreated(ZonedDateTime temporal)
    {
        super(temporal);
    }

//    public DateTimeCreated(CharSequence contentLine)
//    {
//        super(contentLine);
//    }
    
    public DateTimeCreated(DateTimeCreated source)
    {
        super(source);
    }
    
    public DateTimeCreated()
    {
        super();
    }

    public static DateTimeCreated parse(String value)
    {
        DateTimeCreated property = new DateTimeCreated();
        property.parseContent(value);
        return property;
    }
}
