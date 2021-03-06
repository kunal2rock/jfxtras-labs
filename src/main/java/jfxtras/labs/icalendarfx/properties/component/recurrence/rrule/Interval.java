package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule;

/**
 * INTERVAL
 * RFC 5545 iCalendar 3.3.10, page 40
 * 
 * The INTERVAL rule part contains a positive integer representing at
 * which intervals the recurrence rule repeats.  The default value is
 * "1", meaning every second for a SECONDLY rule, every minute for a
 * MINUTELY rule, every hour for an HOURLY rule, every day for a
 * DAILY rule, every week for a WEEKLY rule, every month for a
 * MONTHLY rule, and every year for a YEARLY rule.  For example,
 * within a DAILY rule, a value of "8" means every eight days.
 */
public class Interval extends RRuleElementBase<Integer, Interval>
{
    public static final int DEFAULT_INTERVAL = 1;
//    @Override
//    public Integer getValue() { return (valueProperty().get() == null) ? DEFAULT_INTERVAL : valueProperty().get(); }
    
    public Interval()
    {
        super();
        setValue(DEFAULT_INTERVAL);
        valueProperty().addListener((obs, oldValue, newValue) ->
        {
            if ((newValue != null) && (newValue < 1))
            {
                setValue(oldValue);
                throw new IllegalArgumentException(elementType() + " can't be less than 1");
            }
        });
    }
    
    public Interval(Integer interval)
    {
        this();
        setValue(interval);
    }

    public Interval(Interval source)
    {
        this();
        setValue(source.getValue());
    }

    @Override
    public void parseContent(String content)
    {
        setValue(Integer.parseInt(content));
    }

    public static Interval parse(String content)
    {
        Interval element = new Interval();
        element.parseContent(content);
        return element;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getValue() == null) ? 0 : getValue().hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Interval other = (Interval) obj;
        
        Integer value = (getValue() == null) ? DEFAULT_INTERVAL : getValue();
        Integer otherValue = (other.getValue() == null) ? DEFAULT_INTERVAL : getValue();
        return value.equals(otherValue);
    }
}
