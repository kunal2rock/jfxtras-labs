package jfxtras.labs.icalendarfx.components;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.PropertyBaseRecurrence;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceDates;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRuleCache;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities.DateTimeType;
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities;

/**
 * Contains following properties:
 * @see RecurrenceRule
 * @see RecurrenceDates
 * 
 * @author David Bal
 * @see VEventOld
 * @see VTodo
 * @see VJournal
 * @see StandardTime
 * @see DaylightSavingTime
 *
 * @param <T> implemented class
 * @param <R> recurrence type
 */
public interface VComponentRepeatable<T> extends VComponentPrimary<T>
{    
    /**
     * RDATE: Recurrence Date-Times
     * Set of date/times for recurring events, to-dos, journal entries.
     * 3.8.5.2, RFC 5545 iCalendar
     * 
     * Examples:
     * RDATE;TZID=America/New_York:19970714T083000
     * RDATE;VALUE=DATE:19970101,19970120,19970217,19970421
     *  19970526,19970704,19970901,19971014,19971128,19971129,1997122
     */
    ObservableList<RecurrenceDates> getRecurrenceDates();
    void setRecurrenceDates(ObservableList<RecurrenceDates> recurrences);
    default T withRecurrenceDates(ObservableList<RecurrenceDates> recurrenceDates)
    {
        setRecurrenceDates(recurrenceDates);
        return (T) this;
    }
    default T withRecurrenceDates(String...recurrenceDates)
    {
        Arrays.stream(recurrenceDates).forEach(s -> PropertyType.RECURRENCE_DATE_TIMES.parse(this, s));   
        return (T) this;
    }
    default T withRecurrenceDates(Temporal...recurrenceDates)
    {
        if (recurrenceDates.length > 0)
        {
            final ObservableList<RecurrenceDates> list;
            if (getRecurrenceDates() == null)
            {
                list = FXCollections.observableArrayList();
                setRecurrenceDates(list);
            } else
            {
                list = getRecurrenceDates();
            }
            Set<Temporal> recurrences2 = Arrays.stream(recurrenceDates).map(r -> (LocalDate) r).collect(Collectors.toSet());
            getRecurrenceDates().add(new RecurrenceDates(FXCollections.observableSet(recurrences2)));
            
//            Temporal t = recurrenceDates[0];
//            if (t instanceof LocalDate)
//            {
//                Set<LocalDate> recurrences2 = Arrays.stream(recurrenceDates).map(r -> (LocalDate) r).collect(Collectors.toSet());
//                getRecurrenceDates().add(new Recurrences<LocalDate>(FXCollections.observableSet(recurrences2)));
//            } else if (t instanceof LocalDateTime)
//            {
//                getRecurrenceDates().add(new Recurrences<LocalDateTime>(FXCollections.observableSet((LocalDateTime[]) recurrenceDates)));
//            } else if (t instanceof ZonedDateTime)
//            {
//                getRecurrenceDates().add(new Recurrences<ZonedDateTime>(FXCollections.observableSet((ZonedDateTime[]) recurrenceDates)));
//            }
        }
        return (T) this;
    }
    default T withRecurrenceDates(RecurrenceDates...recurrenceDates)
    {
        if (getRecurrenceDates() == null)
        {
            setRecurrenceDates(FXCollections.observableArrayList());
            Arrays.stream(recurrenceDates).forEach(r -> getRecurrenceDates().add(r)); // add one at a time to ensure date-time type compliance
        } else
        {
            getRecurrenceDates().addAll(recurrenceDates);
        }
        return (T) this;
    }
    
    /** Ensures new recurrence values match previously added ones.  Also ensures recurrence
     * value match DateTimeStart.  Should be called after dateTimeEndProperty() 
     * @param exceptions */
    default ListChangeListener<PropertyBaseRecurrence<?>> getRecurrencesConsistencyWithDateTimeStartListener()
    {
        return (ListChangeListener.Change<? extends PropertyBaseRecurrence<?>> change) ->
        {
            ObservableList<? extends PropertyBaseRecurrence<?>> list = change.getList();
            while (change.next())
            {
                if (change.wasAdded())
                {
                    List<? extends PropertyBaseRecurrence<?>> changeList = change.getAddedSubList();
                    Temporal firstRecurrence = list.get(0).getValue().iterator().next();
                    // check consistency with previous recurrence values
                    checkRecurrencesConsistency(changeList, firstRecurrence);
                }
            }
        };
    }
    /**
     * Determines if recurrence objects are valid.  They are valid if the date-time types are the same and matches
     * DateTimeStart.  This should be run when a change occurs to the recurrences list and when the recurrences
     * Observable list is set.
     * 
     * Also works for exceptions.
     * 
     * @param list - list of recurrence objects to be tested.
     * @param firstRecurrence - example of Temporal to match against.  If null uses first element in first recurrence in list
     * @return - true is valid, throws exception otherwise
     */
    default boolean checkRecurrencesConsistency(List<? extends PropertyBaseRecurrence<?>> list, Temporal firstRecurrence)
    {
        if ((list == null) || (list.isEmpty()))
        {
            return true;
        }
        firstRecurrence = (firstRecurrence == null) ? list.get(0).getValue().iterator().next() : firstRecurrence;
        DateTimeType firstDateTimeTypeType = DateTimeUtilities.DateTimeType.of(firstRecurrence);
        Iterator<? extends PropertyBaseRecurrence<?>> i = list.iterator();
        while (i.hasNext())
        {
            PropertyBaseRecurrence<?> r = i.next();
            Temporal myTemporalClass = r.getValue().iterator().next();
            DateTimeType myDateTimeType = DateTimeUtilities.DateTimeType.of(myTemporalClass);
            if (myDateTimeType != firstDateTimeTypeType)
            {
                throw new DateTimeException("Added recurrences DateTimeType " + myDateTimeType +
                        " doesn't match previous recurrences DateTimeType " + firstDateTimeTypeType);
            }
        }
        // check consistency with dateTimeStart
        if (getDateTimeStart() != null)
        {
            DateTimeType dateTimeStartType = DateTimeUtilities.DateTimeType.of(getDateTimeStart().getValue());
            if (firstDateTimeTypeType != dateTimeStartType)
            {
                throw new DateTimeException("DateTimeType (" + firstDateTimeTypeType +
                        ") must be same as the DateTimeType of DateTimeStart (" + dateTimeStartType + ")");
            }
        }
        return true;
    }
    
    @Override
    default void checkDateTimeStartConsistency()
    {
        if ((getRecurrenceDates() != null) && (getDateTimeStart() != null))
        {
            Temporal firstRecurrence = getRecurrenceDates().get(0).getValue().iterator().next();
            DateTimeType recurrenceType = DateTimeUtilities.DateTimeType.of(firstRecurrence);
            DateTimeType dateTimeStartType = DateTimeUtilities.DateTimeType.of(getDateTimeStart().getValue());
            if (recurrenceType != dateTimeStartType)
            {
                throw new DateTimeException("Recurrences DateTimeType (" + recurrenceType +
                        ") must be same as the DateTimeType of DateTimeStart (" + dateTimeStartType + ")");
            }
        }        
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
    ObjectProperty<RecurrenceRule> recurrenceRuleProperty();
    RecurrenceRule getRecurrenceRule();
    default void setRecurrenceRule(RecurrenceRule recurrenceRule) { recurrenceRuleProperty().set(recurrenceRule); }
    default void setRecurrenceRule(RecurrenceRule2 rrule)
    {
        if (getRecurrenceRule() == null)
        {
            setRecurrenceRule(new RecurrenceRule(rrule));
        } else
        {
            getRecurrenceRule().setValue(rrule);
        }
    }
    default void setRecurrenceRule(String rrule)
    {
        if (getRecurrenceRule() == null)
        {
            setRecurrenceRule(RecurrenceRule.parse(rrule));
        } else
        {
            RecurrenceRule temp = RecurrenceRule.parse(rrule);
            getRecurrenceRule().setValue(temp.getValue());
        }
    }
    default T withRecurrenceRule(String rrule)
    {
        if (getRecurrenceRule() == null)
        {
            setRecurrenceRule(rrule);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withRecurrenceRule(RecurrenceRule rrule)
    {
        if (getRecurrenceRule() == null)
        {
            setRecurrenceRule(rrule);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withRecurrenceRule(RecurrenceRule2 rrule)
    {
        if (getRecurrenceRule() == null)
        {
            setRecurrenceRule(rrule);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }

    /**
     * Handles caching of recurrence start Temporal values.
     */
    RecurrenceRuleCache recurrenceStreamer();

    /**
     * Produces a stream of dates or date-times bounded by the start and end parameters.  See {@link #streamRecurrences(Temporal)}
     * 
     * @param start - include recurrences that END before this value (inclusive)
     * @param end - include recurrences that START before this value (exclusive)
     * @return - stream of start dates or date/times for the recurrence set
     */
    default Stream<Temporal> streamRecurrences(Temporal start, Temporal end)
    {
        return ICalendarUtilities.takeWhile(streamRecurrences(start), a -> DateTimeUtilities.isBefore(a, end)); // exclusive
    }

    
    /** 
     * Produces a stream of dates or date-times (depending on DTSTART) that represents the start
     * of each element in the recurrence set.
     * The values are calculated after applying DTSTART, RDATE, RRULE, and EXDATE properties.
     * 
     * If the RRULE is forever, then the stream has no end as well.

     * For a VEvent without RRULE or RDATE the stream will contain only one element.
     * 
     * @param start - include recurrences that END before this value
     * @return - stream of start dates or date/times for the recurrence set
     */
    default Stream<Temporal> streamRecurrences(Temporal start)
    {
        DateTimeType startType = DateTimeUtilities.DateTimeType.of(start);
        DateTimeType dateTimeStartType = DateTimeUtilities.DateTimeType.of(getDateTimeStart().getValue());
        if (startType != dateTimeStartType)
        {
            System.out.println(("Start type " + startType + " must match DTSTART type of " + dateTimeStartType));
            throw new DateTimeException("Start type " + startType + " must match DTSTART type of " + dateTimeStartType);
        }
        // get recurrence rule stream, or make a one-element stream from DTSTART if no recurrence rule is present
        final Stream<Temporal> stream1;
        if (getRecurrenceRule() == null)
        {
            stream1 = Arrays.asList(getDateTimeStart().getValue()).stream();
        } else
        {
            Temporal cacheStart = recurrenceStreamer().getStartFromCache(start);
            stream1 = getRecurrenceRule().getValue().streamRecurrences(cacheStart);
        }
        
        // assign temporal comparator to match start type
        final Comparator<Temporal> temporalComparator = DateTimeUtilities.getTemporalComparator(start);
        
        // add recurrences, if present
        final Stream<Temporal> stream2 = (getRecurrenceDates() == null) ? stream1 : merge(
                stream1,
                getRecurrenceDates()
                        .stream()
                        .flatMap(r -> r.getValue().stream())
                        .map(v -> v)
                        .filter(t -> ! DateTimeUtilities.isBefore(t, start)) // remove too early events;
                        .sorted(temporalComparator)
                , temporalComparator);
        
        return stream2
                .filter(t -> ! DateTimeUtilities.isBefore(t, start));
//                .peek(t -> System.out.println("stream:" + t + " " + start))
    }
    
    /** Stream of recurrences starting at dateTimeStart (DTSTART) 
     * @link {@link #streamRecurrences(Temporal)}*/
    default Stream<Temporal> streamRecurrences()
    {
        return streamRecurrences(getDateTimeStart().getValue());
    }
    
    /**
     * finds previous stream Temporal before input parameter value
     * 
     * @param value
     * @return
     */
    default Temporal previousStreamValue(Temporal value)
    {
        Temporal cacheStart = recurrenceStreamer().getStartFromCache(value);
        Iterator<Temporal> i = streamRecurrences(cacheStart).iterator();
        Temporal lastT = null;
        while (i.hasNext())
        {
            Temporal t = i.next();
            if (! DateTimeUtilities.isBefore(t, value)) break;
            lastT = t;
        }
        return lastT;
    }
    
    /** Returns true if temporal is in vComponent's stream of start date-time
     * values, false otherwise.
     */
    default boolean isRecurrence(Temporal temporal)
    {
        Iterator<Temporal> startInstanceIterator = streamRecurrences(temporal).iterator();
        while (startInstanceIterator.hasNext())
        {
            Temporal myStartInstance = startInstanceIterator.next();
            if (myStartInstance.equals(temporal))
            {
                return true;
            }
            if (DateTimeUtilities.isAfter(myStartInstance, temporal))
            {
                return false;
            }
        }
        return false;
    }

    /** Returns true if VComponent has zero instances in recurrence set */
    default boolean isRecurrenceSetEmpty()
    {
        Iterator<Temporal> i = streamRecurrences().iterator();
        return ! i.hasNext();
    }
    
    /** returns the last date or date/time of the series.  If infinite returns null */
    default Temporal lastRecurrence()
    {
        if ((getRecurrenceRule() != null) && (getRecurrenceRule().getValue().isInfinite()))
        {
            return null;
        } else
        {
            Iterator<Temporal> i = streamRecurrences().iterator();
            Temporal myTemporal = null;
            while (i.hasNext())
            {
                myTemporal = i.next();
            }
            return myTemporal;
        }       
    }
    
//    @Override
//    default List<String> errors()
//    {
////        List<String> errors = new ArrayList<>();
//        List<String> errors = VComponentPrimary.super.errors();
//        boolean isRecurrenceRulePresent = getRecurrenceRule() != null;
//        if (! isRecurrenceRulePresent)
//        {
//            errors.addAll(getRecurrenceRule().errors());
//        }
//        if (getRecurrenceDates() != null)
//        {
//            DateTimeType startType = DateTimeUtilities.DateTimeType.of(getDateTimeStart().getValue());
//            Temporal r1 = getRecurrenceDates().get(0).getValue().iterator().next();
//            DateTimeType recurrenceType = DateTimeUtilities.DateTimeType.of(r1);
//            boolean isRecurrenceTypeMatch = startType == recurrenceType;
//            if (! isRecurrenceTypeMatch)
//            {
//                errors.add("The value type of RDATE elements MUST be the same as the DTSTART property (" + recurrenceType + ", " + startType);
//            }
//        }
//        return errors;
//    }
    
    @Deprecated // may not be used - if not remove or move to utility class
    public static <T> Stream<T> merge(Stream<T> stream1, Stream<T> stream2, Comparator<T> comparator)
    {
            Iterator<T> iterator = new MergedIterator<T>(
                    stream1.iterator()
                  , stream2.iterator()
                  , comparator);
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }
    
    /*
     * Recommend using with StreamSupport.stream(iteratorStream, false);
     */

    /** Merge two sorted iterators */
    static class MergedIterator<T> implements Iterator<T>
    {
        private final Iterator<T> iterator1;
        private final Iterator<T> iterator2;
        private final Comparator<T> comparator;
        private T next1;
        private T next2;
        
        public MergedIterator(Iterator<T> iterator1, Iterator<T> iterator2, Comparator<T> comparator)
        {
            this.iterator1 = iterator1;
            this.iterator2 = iterator2;
            this.comparator = comparator;
        }
        
        @Override
        public boolean hasNext()
        {
            return  iterator1.hasNext() || iterator2.hasNext() || (next1 != null) || (next2 != null);
        }

        @Override
        public T next()
        {
            if (iterator1.hasNext() && (next1 == null)) next1 = iterator1.next();
            if (iterator2.hasNext() && (next2 == null)) next2 = iterator2.next();
            T theNext;
            int result = (next1 == null) ? 1 :
                         (next2 == null) ? -1 :
                         comparator.compare(next1, next2);
            if (result > 0)
            {
                theNext = next2;
                next2 = null;
            } else if (result < 0)
            {
                theNext = next1;
                next1 = null;
            } else
            { // same element, return one, advance both
                theNext = next1;
                next1 = null;
                next2 = null;
            }
            return theNext;
        }
    }

}
