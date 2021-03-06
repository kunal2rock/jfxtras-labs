package jfxtras.labs.icalendarfx.components;

import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceDates;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRuleCache;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities.DateTimeType;
/**
 * Contains following properties:
 * @see RecurrenceRule
 * @see RecurrenceDates
 * 
 * @author David Bal
 *
 * @param <T> - concrete subclass
 * @see DaylightSavingTime
 * @see StandardTime
 */
public abstract class VComponentRepeatableBase<T> extends VComponentPrimaryBase<T> implements VComponentRepeatable<T>
{
    /**
     * RDATE
     * Recurrence Date-Times
     * RFC 5545 iCalendar 3.8.5.2, page 120.
     * 
     * This property defines the list of DATE-TIME values for
     * recurring events, to-dos, journal entries, or time zone definitions.
     * 
     * NOTE: DOESN'T CURRENTLY SUPPORT PERIOD VALUE TYPE
     * */
    @Override
    public ObservableList<RecurrenceDates> getRecurrenceDates() { return recurrenceDates; }
    private ObservableList<RecurrenceDates> recurrenceDates;
    @Override
    public void setRecurrenceDates(ObservableList<RecurrenceDates> recurrenceDates)
    {
        this.recurrenceDates = recurrenceDates;
        recurrenceDates.addListener(getRecurrencesConsistencyWithDateTimeStartListener());
        checkRecurrencesConsistency(recurrenceDates, null);
    }

    /**
     * RRULE, Recurrence Rule
     * RFC 5545 iCalendar 3.8.5.3, page 122.
     * This property defines a rule or repeating pattern for recurring events, 
     * to-dos, journal entries, or time zone definitions
     * If component is not repeating the value is null.
     * 
     * Examples:
     * RRULE:FREQ=DAILY;COUNT=10
     * RRULE:FREQ=WEEKLY;UNTIL=19971007T000000Z;WKST=SU;BYDAY=TU,TH
     */
    @Override public ObjectProperty<RecurrenceRule> recurrenceRuleProperty()
    {
        if (recurrenceRule == null)
        {
            recurrenceRule = new SimpleObjectProperty<>(this, PropertyType.UNIQUE_IDENTIFIER.toString());
        }
        return recurrenceRule;
    }
    @Override
    public RecurrenceRule getRecurrenceRule() { return (recurrenceRule == null) ? null : recurrenceRuleProperty().get(); }
    private ObjectProperty<RecurrenceRule> recurrenceRule;
    
    /*
     * CONSTRUCTORS
     */
    VComponentRepeatableBase() { }
    
    VComponentRepeatableBase(String contentLines)
    {
        super(contentLines);
    }    
    
    @Override
    public Stream<Temporal> streamRecurrences(Temporal start)
    {
        Stream<Temporal> inStream = VComponentRepeatable.super.streamRecurrences(start);
        if (getRecurrenceRule() == null)
        {
            return inStream; // no cache is no recurrence rule
        }
        return recurrenceStreamer().makeCache(inStream);   // make cache of start date/times
    }
    
    @Override
    public List<String> errors()
    {
        List<String> errors = new ArrayList<>();
        if (getRecurrenceDates() != null)
        {
            Temporal r1 = getRecurrenceDates().get(0).getValue().iterator().next();
            DateTimeType recurrenceType = DateTimeUtilities.DateTimeType.of(r1);
            DateTimeType startType = DateTimeUtilities.DateTimeType.of(getDateTimeStart().getValue());
            boolean isRecurrenceTypeMatch = startType == recurrenceType;
            if (! isRecurrenceTypeMatch)
            {
                errors.add("The value type of RDATE elements MUST be the same as the DTSTART property (" + recurrenceType + ", " + startType);
            }
        }
        
        if (getRecurrenceRule() != null)
        {
            errors.addAll(getRecurrenceRule().errors());
        }      
        return errors;
    }

    /*
     *  RECURRENCE STREAMER
     *  produces recurrence set
     */
    private RecurrenceRuleCache streamer = new RecurrenceRuleCache(this);
    @Override
    public RecurrenceRuleCache recurrenceStreamer() { return streamer; }
    
}
