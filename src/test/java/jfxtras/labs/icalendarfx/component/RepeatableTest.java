package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import jfxtras.labs.icalendarfx.components.DaylightSavingTime;
import jfxtras.labs.icalendarfx.components.StandardTime;
import jfxtras.labs.icalendarfx.components.VComponentRepeatable;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceDates;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay.ByDayPair;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonthDay;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByWeekNumber;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

/**
 * Test following components:
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see StandardTime
 * @see DaylightSavingTime
 * 
 * for the following properties:
 * @see RecurrenceDates
 * @see RecurrenceRule
 * 
 * @author David Bal
 *
 */
public class RepeatableTest //extends Application
{
//    // Below Application code inserted as an attempt to catch listener-thrown exceptions - not successful
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        // noop
//    }
//
//    @BeforeClass
//    public static void initJFX() {
//        Thread t = new Thread("JavaFX Init Thread")
//        {
//            @Override
//            public void run() {
//                Application.launch(RepeatableTest.class, new String[0]);
//            }
//        };
//        t.setDaemon(true);
//        t.start();
//    }


    @Test
    public void canBuildRepeatable() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
        List<VComponentRepeatable<?>> components = Arrays.asList(
                new VEvent()
                    .withRecurrenceDates("RDATE;VALUE=DATE:20160504,20160508,20160509")
                    .withRecurrenceDates(LocalDate.of(2016, 4, 15), LocalDate.of(2016, 4, 16), LocalDate.of(2016, 4, 17))
                    .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(4)),
                new VTodo()
                    .withRecurrenceDates("RDATE;VALUE=DATE:20160504,20160508,20160509")
                    .withRecurrenceDates(LocalDate.of(2016, 4, 15), LocalDate.of(2016, 4, 16), LocalDate.of(2016, 4, 17))
                    .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(4)),
                new VJournal()
                    .withRecurrenceDates("RDATE;VALUE=DATE:20160504,20160508,20160509")
                    .withRecurrenceDates(LocalDate.of(2016, 4, 15), LocalDate.of(2016, 4, 16), LocalDate.of(2016, 4, 17))
                    .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(4)),
                new DaylightSavingTime()
                    .withRecurrenceDates("RDATE;VALUE=DATE:20160504,20160508,20160509")
                    .withRecurrenceDates(LocalDate.of(2016, 4, 15), LocalDate.of(2016, 4, 16), LocalDate.of(2016, 4, 17))
                    .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(4)),
                new StandardTime()
                    .withRecurrenceDates("RDATE;VALUE=DATE:20160504,20160508,20160509")
                    .withRecurrenceDates(LocalDate.of(2016, 4, 15), LocalDate.of(2016, 4, 16), LocalDate.of(2016, 4, 17))
                    .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(4))
                );
        
        List<LocalDate> expectedDates = new ArrayList<LocalDate>(Arrays.asList(
                LocalDate.of(2016, 4, 13) // DTSTART
              , LocalDate.of(2016, 4, 15) // 2nd RDATE
              , LocalDate.of(2016, 4, 16) // 2nd RDATE
              , LocalDate.of(2016, 4, 17) // 2nd RDATE and RRULE
              , LocalDate.of(2016, 4, 21) // RRULE
              , LocalDate.of(2016, 4, 25) // RRULE
              , LocalDate.of(2016, 4, 29) // RRULE
              , LocalDate.of(2016, 5, 3) // RRULE
              , LocalDate.of(2016, 5, 4) // 1st RDATE
              , LocalDate.of(2016, 5, 7) // RRULE
              , LocalDate.of(2016, 5, 8) // 1st RDATE
              , LocalDate.of(2016, 5, 9) // 1st RDATE
                ));
        
        for (VComponentRepeatable<?> builtComponent : components)
        {
            String componentName = builtComponent.componentType().toString();            
            String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                    "RDATE;VALUE=DATE:20160504,20160508,20160509" + System.lineSeparator() +
                    "RDATE;VALUE=DATE:20160415,20160416,20160417" + System.lineSeparator() +
                    "RRULE:FREQ=DAILY;INTERVAL=4" + System.lineSeparator() +
                    "END:" + componentName;
                    
            VComponentRepeatable<?> parsedComponent = builtComponent
                    .getClass()
                    .getConstructor(String.class)
                    .newInstance(expectedContent);
            assertEquals(parsedComponent, builtComponent);
            assertEquals(expectedContent, builtComponent.toContent());
            
            builtComponent.setDateTimeStart(new DateTimeStart(LocalDate.of(2016, 4, 13)));
            List<Temporal> madeDates = builtComponent                    
                    .streamRecurrences()
                    .limit(12)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, madeDates);
        }
    }

    @Test
    public void canStreamRecurrences1()
    {
        LocalDate dateTimeStart = LocalDate.of(2016, 4, 22);
        VEvent component = new VEvent()
                .withRecurrenceRule("RRULE:FREQ=DAILY")
                .withDateTimeStart(dateTimeStart);
        List<Temporal> expectedRecurrences = Stream
                .iterate(dateTimeStart, a -> a.plus(1, ChronoUnit.DAYS))
                .limit(100)
                .collect(Collectors.toList());
        List<Temporal> madeRecurrences = component.streamRecurrences().limit(100).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }

    @Test
    public void canStreamLaterStart()
    {
        VEvent component = new VEvent()
                .withRecurrenceRule("RRULE:FREQ=DAILY")
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 22, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 10, 2, 0));
        List<Temporal> expectedRecurrences = Stream
                .iterate(LocalDateTime.of(2016, 5, 31, 22, 0), a -> a.plus(1, ChronoUnit.DAYS))
                .limit(10)
                .collect(Collectors.toList());
        List<Temporal> madeRecurrences = component
                .streamRecurrences(LocalDateTime.of(2016, 5, 31, 22, 0))
                .limit(10)
                .collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }

    @Test //(expected = DateTimeException.class)
    @Ignore // can't catch exception in listener
    // TODO - RELY ON isValid test instead of listener - change test
    public void canHandleDTStartTypeChange()
    {
        VEvent component = new VEvent()
            .withDateTimeStart(LocalDate.of(1997, 3, 1))
            .withRecurrenceDates("RDATE;VALUE=DATE:19970304,19970504,19970704,19970904");
//        Platform.runLater(() -> component.setDateTimeStart("20160302T223316Z"));      
        component.setDateTimeStart(DateTimeStart.parse(ZonedDateTime.class, "20160302T223316Z")); // invalid
    }

    @Test (expected = DateTimeException.class)
    public void canCatchWrongDateType()
    {
        VEvent component = new VEvent()
                .withDateTimeStart(LocalDate.of(1997, 3, 1));
        ObservableList<RecurrenceDates> recurrences = FXCollections.observableArrayList();
        recurrences.add(RecurrenceDates.parse(LocalDateTime.class, "20160228T093000"));
        component.setRecurrenceDates(recurrences); // invalid        
    }

    @Test //(expected = DateTimeException.class)
    @Ignore // JUnit won't recognize exception - exception is thrown in listener is cause
    public void canCatchDifferentRepeatableTypes()
    {
        VEvent builtComponent = new VEvent()
                .withRecurrenceDates("RDATE;VALUE=DATE:19970304,19970504,19970704,19970904");
        ObservableSet<Temporal> expectedValues = FXCollections.observableSet(
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 4, 1, 0), ZoneId.of("Z")) );        
        builtComponent.getRecurrenceDates().add(new RecurrenceDates(expectedValues));
    }
    
    /*
     * STREAM RECURRENCES TESTS
     */
    
    /** tests converting ISO.8601.2004 date-time string to LocalDateTime */
    /** Tests daily stream with FREQ=YEARLY */
    @Test
    public void yearlyStreamTest1()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                .withFrequency(FrequencyType.YEARLY));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2016, 11, 9, 10, 0)
              , LocalDateTime.of(2017, 11, 9, 10, 0)
              , LocalDateTime.of(2018, 11, 9, 10, 0)
              , LocalDateTime.of(2019, 11, 9, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=YEARLY";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** Tests daily stream with FREQ=YEARLY;BYDAY=FR */
    @Test
    public void yearlyStreamTest2()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 6, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY)
                        .withByRules(new ByDay(DayOfWeek.FRIDAY)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 6, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 20, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 12, 4, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=YEARLY;BYDAY=FR";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** FREQ=YEARLY;BYDAY=TH;BYMONTH=6,7,8 */
    @Test
    public void yearlyStreamTest3()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(1997, 6, 5, 9, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY)
                        .withByRules(new ByDay(DayOfWeek.THURSDAY),
                                new ByMonth(Month.JUNE, Month.JULY, Month.AUGUST)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(20)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(1997, 6, 5, 9, 0)
              , LocalDateTime.of(1997, 6, 12, 9, 0)
              , LocalDateTime.of(1997, 6, 19, 9, 0)
              , LocalDateTime.of(1997, 6, 26, 9, 0)
              , LocalDateTime.of(1997, 7, 3, 9, 0)
              , LocalDateTime.of(1997, 7, 10, 9, 0)
              , LocalDateTime.of(1997, 7, 17, 9, 0)
              , LocalDateTime.of(1997, 7, 24, 9, 0)
              , LocalDateTime.of(1997, 7, 31, 9, 0)
              , LocalDateTime.of(1997, 8, 7, 9, 0)
              , LocalDateTime.of(1997, 8, 14, 9, 0)
              , LocalDateTime.of(1997, 8, 21, 9, 0)
              , LocalDateTime.of(1997, 8, 28, 9, 0)
              , LocalDateTime.of(1998, 6, 4, 9, 0)
              , LocalDateTime.of(1998, 6, 11, 9, 0)
              , LocalDateTime.of(1998, 6, 18, 9, 0)
              , LocalDateTime.of(1998, 6, 25, 9, 0)
              , LocalDateTime.of(1998, 7, 2, 9, 0)
              , LocalDateTime.of(1998, 7, 9, 9, 0)
              , LocalDateTime.of(1998, 7, 16, 9, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=YEARLY;BYMONTH=6,7,8;BYDAY=TH";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** FREQ=YEARLY;BYMONTH=1,2 */
    @Test
    public void yearlyStreamTest4()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 1, 6, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY)
                        .withByRules(new ByMonth(Month.JANUARY, Month.FEBRUARY)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 1, 6, 10, 0)
              , LocalDateTime.of(2015, 2, 6, 10, 0)
              , LocalDateTime.of(2016, 1, 6, 10, 0)
              , LocalDateTime.of(2016, 2, 6, 10, 0)
              , LocalDateTime.of(2017, 1, 6, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=YEARLY;BYMONTH=1,2";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** FREQ=YEARLY;BYMONTH=11;BYMONTHDAY=10 */
    @Test
    public void yearlyStreamTest5()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 10, 0, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY)
                        .withByRules(new ByMonth(Month.NOVEMBER), new ByMonthDay(10)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 10, 0, 0)
              , LocalDateTime.of(2016, 11, 10, 0, 0)
              , LocalDateTime.of(2017, 11, 10, 0, 0)
              , LocalDateTime.of(2018, 11, 10, 0, 0)
              , LocalDateTime.of(2019, 11, 10, 0, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=YEARLY;BYMONTH=11;BYMONTHDAY=10";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());

    }
    
    /** FREQ=YEARLY;INTERVAL=4;BYMONTH=11;BYMONTHDAY=2,3,4,5,6,7,8;BYDAY=TU
     * (U.S. Presidential Election day) */
    @Test
    public void yearlyStreamTest6()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(1996, 11, 5, 0, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY)
                        .withInterval(4)
                        .withByRules(new ByMonth(Month.NOVEMBER)
                                   , new ByDay(DayOfWeek.TUESDAY)
                                   , new ByMonthDay(2,3,4,5,6,7,8)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(6)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(1996, 11, 5, 0, 0)
              , LocalDateTime.of(2000, 11, 7, 0, 0)
              , LocalDateTime.of(2004, 11, 2, 0, 0)
              , LocalDateTime.of(2008, 11, 4, 0, 0)
              , LocalDateTime.of(2012, 11, 6, 0, 0)
              , LocalDateTime.of(2016, 11, 8, 0, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=YEARLY;INTERVAL=4;BYMONTH=11;BYMONTHDAY=2,3,4,5,6,7,8;BYDAY=TU";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** FREQ=YEARLY;BYDAY=20MO */
    @Test
    public void yearlyStreamTest7()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(1997, 5, 19, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY)
                        .withByRules(new ByDay(new ByDayPair(DayOfWeek.MONDAY, 20))));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(3)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(1997, 5, 19, 10, 0)
              , LocalDateTime.of(1998, 5, 18, 10, 0)
              , LocalDateTime.of(1999, 5, 17, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=YEARLY;BYDAY=20MO";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** FREQ=YEARLY;BYWEEKNO=20;BYDAY=MO */
    @Test
    public void yearlyStreamTest8()
    {
//        Locale oldLocale = Locale.getDefault();
//        Locale.setDefault(Locale.FRANCE); // has Monday as first day of week system.  US is Sunday which causes an error.
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(1997, 5, 12, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY)
                        .withByRules(new ByWeekNumber(20),
                                     new ByDay(DayOfWeek.MONDAY)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(1997, 5, 12, 10, 0)
              , LocalDateTime.of(1998, 5, 11, 10, 0)
              , LocalDateTime.of(1999, 5, 17, 10, 0)
              , LocalDateTime.of(2000, 5, 15, 10, 0)
              , LocalDateTime.of(2001, 5, 14, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=YEARLY;BYWEEKNO=20;BYDAY=MO";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
//        Locale.setDefault(oldLocale);
    }
    
    /** Tests daily stream with FREQ=MONTHLY */
    @Test
    public void monthlyStreamTest()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 12, 9, 10, 0)
              , LocalDateTime.of(2016, 1, 9, 10, 0)
              , LocalDateTime.of(2016, 2, 9, 10, 0)
              , LocalDateTime.of(2016, 3, 9, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=MONTHLY";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** Tests daily stream with FREQ=MONTHLY;BYMONTHDAY=-2 */
    @Test
    public void monthlyStreamTest2()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 29, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY)
                                .withByRules(new ByMonthDay()
                                        .withValue(-2))); // repeats 2nd to last day of month
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 29, 10, 0)
              , LocalDateTime.of(2015, 12, 30, 10, 0)
              , LocalDateTime.of(2016, 1, 30, 10, 0)
              , LocalDateTime.of(2016, 2, 28, 10, 0)
              , LocalDateTime.of(2016, 3, 30, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=MONTHLY;BYMONTHDAY=-2";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** Tests daily stream with FREQ=MONTHLY;BYDAY=TU,WE,FR */
    @Test
    public void monthlyStreamTest3()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY)
                        .withByRules(new ByDay(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(10)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 10, 10, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 10, 0)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
              , LocalDateTime.of(2015, 11, 20, 10, 0)
              , LocalDateTime.of(2015, 11, 24, 10, 0)
              , LocalDateTime.of(2015, 11, 25, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 12, 1, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=MONTHLY;BYDAY=TU,WE,FR";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** Tests daily stream with FREQ=MONTHLY;BYDAY=-1SA */
    @Test
    public void monthlyStreamTest4()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY)
                        .withByRules(new ByDay(new ByDay.ByDayPair(DayOfWeek.SATURDAY, -1)))); // last Saturday in month
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 28, 10, 0)
              , LocalDateTime.of(2015, 12, 26, 10, 0)
              , LocalDateTime.of(2016, 1, 30, 10, 0)
              , LocalDateTime.of(2016, 2, 27, 10, 0)
              , LocalDateTime.of(2016, 3, 26, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=MONTHLY;BYDAY=-1SA";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13 Every Friday the 13th, forever: */
    @Test
    public void monthlyStreamTest5()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(1997, 6, 13, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY)
                        .withByRules(new ByDay(DayOfWeek.FRIDAY), new ByMonthDay(13)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(6)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(1997, 6, 13, 10, 0)
              , LocalDateTime.of(1998, 2, 13, 10, 0)
              , LocalDateTime.of(1998, 3, 13, 10, 0)
              , LocalDateTime.of(1998, 11, 13, 10, 0)
              , LocalDateTime.of(1999, 8, 13, 10, 0)
              , LocalDateTime.of(2000, 10, 13, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=MONTHLY;BYMONTHDAY=13;BYDAY=FR";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
        RecurrenceRule2 r = RecurrenceRule2.parse("FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13");
        assertEquals(r, e.getRecurrenceRule().getValue()); // verify order of parameters doesn't matter
    }
    
    /** Tests daily stream with FREQ=MONTHLY;BYMONTH=11,12;BYDAY=TU,WE,FR */
    @Test
    public void monthlyStreamTest6()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 3, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY)
                        .withByRules(new ByMonth(Month.NOVEMBER, Month.DECEMBER)
                                   , new ByDay(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(13)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 3, 10, 0)
              , LocalDateTime.of(2015, 11, 4, 10, 0)
              , LocalDateTime.of(2015, 11, 6, 10, 0)
              , LocalDateTime.of(2015, 11, 10, 10, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 10, 0)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
              , LocalDateTime.of(2015, 11, 20, 10, 0)
              , LocalDateTime.of(2015, 11, 24, 10, 0)
              , LocalDateTime.of(2015, 11, 25, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 12, 1, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=MONTHLY;BYMONTH=11,12;BYDAY=TU,WE,FR";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** FREQ=WEEKLY */
    @Test
    public void weeklyStreamTest1()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 16, 10, 0)
              , LocalDateTime.of(2015, 11, 23, 10, 0)
              , LocalDateTime.of(2015, 11, 30, 10, 0)
              , LocalDateTime.of(2015, 12, 7, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=WEEKLY";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** FREQ=WEEKLY;INTERVAL=2;BYDAY=MO,WE,FR */
    @Test
    public void weeklyStreamTest2()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withInterval(2)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(10)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 23, 10, 0)
              , LocalDateTime.of(2015, 11, 25, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 12, 7, 10, 0)
              , LocalDateTime.of(2015, 12, 9, 10, 0)
              , LocalDateTime.of(2015, 12, 11, 10, 0)
              , LocalDateTime.of(2015, 12, 21, 10, 0)
              , LocalDateTime.of(2015, 12, 23, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=WEEKLY;INTERVAL=2;BYDAY=MO,WE,FR";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }

    /** FREQ=WEEKLY;BYDAY=MO,WE,FR */
    @Test
    public void weeklyStreamTest3()
    {
        VEvent e = new VEvent()
            .withDateTimeStart(LocalDateTime.of(2015, 11, 7, 10, 0))
            .withRecurrenceRule(new RecurrenceRule2()
                    .withFrequency(FrequencyType.WEEKLY)
                    .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 16, 10, 0)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=WEEKLY;BYDAY=MO,WE,FR";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** FREQ=WEEKLY;INTERVAL=2;COUNT=11;BYDAY=MO,WE,FR */
    @Test
    public void canStreamWeekly4()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withInterval(2)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))
                        .withCount(11));
        List<Temporal> madeDates = e.streamRecurrences()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 23, 10, 0)
              , LocalDateTime.of(2015, 11, 25, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 12, 7, 10, 0)
              , LocalDateTime.of(2015, 12, 9, 10, 0)
              , LocalDateTime.of(2015, 12, 11, 10, 0)
              , LocalDateTime.of(2015, 12, 21, 10, 0)
              , LocalDateTime.of(2015, 12, 23, 10, 0)
              , LocalDateTime.of(2015, 12, 25, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=WEEKLY;INTERVAL=2;COUNT=11;BYDAY=MO,WE,FR";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    @Test // tests starting on Sunday (1st day of week) with other day of the week
    public void canStreamWeekly5()
    {
        VEvent e = new VEvent()
            .withDateTimeStart(LocalDateTime.of(2016, 1, 3, 5, 0))
            .withRecurrenceRule(new RecurrenceRule2()
                    .withFrequency(FrequencyType.WEEKLY)
                    .withByRules(new ByDay(DayOfWeek.SUNDAY, DayOfWeek.WEDNESDAY))); 
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(10)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2016, 1, 3, 5, 0)
              , LocalDateTime.of(2016, 1, 6, 5, 0)
              , LocalDateTime.of(2016, 1, 10, 5, 0)
              , LocalDateTime.of(2016, 1, 13, 5, 0)
              , LocalDateTime.of(2016, 1, 17, 5, 0)
              , LocalDateTime.of(2016, 1, 20, 5, 0)
              , LocalDateTime.of(2016, 1, 24, 5, 0)
              , LocalDateTime.of(2016, 1, 27, 5, 0)
              , LocalDateTime.of(2016, 1, 31, 5, 0)
              , LocalDateTime.of(2016, 2, 3, 5, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=WEEKLY;BYDAY=SU,WE";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    @Test
    public void canStreamWeeklyZoned()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 10, 0), ZoneId.of("America/Los_Angeles")))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(10)
                .collect(Collectors.toList());
        List<ZonedDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 13, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 16, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 18, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 20, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 23, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 25, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 27, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 30, 10, 0), ZoneId.of("America/Los_Angeles"))
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=WEEKLY;BYDAY=MO,WE,FR";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** Tests daily stream with FREQ=DAILY */
    @Test
    public void dailyStreamTest1()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 10, 10, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 12, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=DAILY";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** Tests daily stream with FREQ=DAILY;INTERVAL=3;COUNT=6 */
    @Test
    public void dailyStreamTest2()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withCount(6)
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(3));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 12, 10, 0)
              , LocalDateTime.of(2015, 11, 15, 10, 0)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
              , LocalDateTime.of(2015, 11, 21, 10, 0)
              , LocalDateTime.of(2015, 11, 24, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=DAILY;INTERVAL=3;COUNT=6";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** Tests daily stream with FREQ=DAILY;INTERVAL=3;BYMONTHDAY=9,10,11,12,13,14 */
    @Test
    public void dailyStreamTest3()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(3)
                        .withByRules(new ByMonthDay()
                            .withValue(9,10,11,12,13,14)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(10)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 12, 10, 0)
              , LocalDateTime.of(2015, 12, 9, 10, 0)
              , LocalDateTime.of(2015, 12, 12, 10, 0)
              , LocalDateTime.of(2016, 1, 11, 10, 0)
              , LocalDateTime.of(2016, 1, 14, 10, 0)
              , LocalDateTime.of(2016, 2, 10, 10, 0)
              , LocalDateTime.of(2016, 2, 13, 10, 0)
              , LocalDateTime.of(2016, 3, 11, 10, 0)
              , LocalDateTime.of(2016, 3, 14, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=DAILY;INTERVAL=3;BYMONTHDAY=9,10,11,12,13,14";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** Tests daily stream with FREQ=DAILY;INTERVAL=2;BYMONTHDAY=9 */
    @Test
    public void dailyStreamTest4()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(2)
                        .withByRules(new ByMonthDay(9)) );
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(6)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 12, 9, 10, 0)
              , LocalDateTime.of(2016, 2, 9, 10, 0)
              , LocalDateTime.of(2016, 4, 9, 10, 0)
              , LocalDateTime.of(2016, 5, 9, 10, 0)
              , LocalDateTime.of(2016, 8, 9, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=DAILY;INTERVAL=2;BYMONTHDAY=9";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** Tests daily stream with FREQ=DAILY;INTERVAL=2;BYDAY=FR */
    @Test
    public void dailyStreamTest5()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(2)
                        .withByRules(new ByDay(DayOfWeek.FRIDAY)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(6)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 12, 11, 10, 0)
              , LocalDateTime.of(2015, 12, 25, 10, 0)
              , LocalDateTime.of(2016, 1, 8, 10, 0)
              , LocalDateTime.of(2016, 1, 22, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=DAILY;INTERVAL=2;BYDAY=FR";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    @Test
    public void dailyStreamTest6()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 12, 1, 9, 59, 59), ZoneOffset.systemDefault())
                                .withZoneSameInstant(ZoneId.of("Z")))
                        .withFrequency(FrequencyType.DAILY)
                                .withInterval(2));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 15, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 10, 0)
              , LocalDateTime.of(2015, 11, 19, 10, 0)
              , LocalDateTime.of(2015, 11, 21, 10, 0)
              , LocalDateTime.of(2015, 11, 23, 10, 0)
              , LocalDateTime.of(2015, 11, 25, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 11, 29, 10, 0)
              ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=DAILY;INTERVAL=2;UNTIL=" + 
                DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER.format(
                        ZonedDateTime.of(LocalDateTime.of(2015, 12, 1, 9, 59, 59), ZoneOffset.systemDefault())
                        .withZoneSameInstant(ZoneId.of("Z")));
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    @Test
    public void dailyStreamTest7()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 11, 29, 10, 0), ZoneOffset.systemDefault())
                        .withZoneSameInstant(ZoneId.of("Z"))) // LocalDateTime adjusted to system default zone offset
                .withFrequency(FrequencyType.DAILY)
                        .withInterval(2));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 15, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 10, 0)
              , LocalDateTime.of(2015, 11, 19, 10, 0)
              , LocalDateTime.of(2015, 11, 21, 10, 0)
              , LocalDateTime.of(2015, 11, 23, 10, 0)
              , LocalDateTime.of(2015, 11, 25, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 11, 29, 10, 0)
              ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=DAILY;INTERVAL=2;UNTIL=" +
                DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER.format(
                        ZonedDateTime.of(LocalDateTime.of(2015, 11, 29, 10, 0), ZoneOffset.systemDefault())
                        .withZoneSameInstant(ZoneId.of("Z")));
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    @Test
    public void dailyStreamTestJapanZone()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 0), ZoneId.of("Japan")))
                .withRecurrenceRule(new RecurrenceRule2()
                .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 11, 19, 1, 0), ZoneId.of("Z")))
                .withFrequency(FrequencyType.DAILY));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .collect(Collectors.toList());
        List<Temporal> expectedDates = new ArrayList<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 12, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 13, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 14, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 15, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 16, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 17, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 18, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 19, 8, 0), ZoneId.of("Japan"))
              ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=DAILY;UNTIL=20151119T010000Z";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    @Test
    public void dailyStreamTestUTC()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 10, 0), ZoneId.of("Z")))
                .withRecurrenceRule(new RecurrenceRule2()
                .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 12, 1, 10, 0), ZoneId.of("Z")))
                .withFrequency(FrequencyType.DAILY)
                        .withInterval(2));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .collect(Collectors.toList());
        List<ZonedDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 10, 0), ZoneId.of("Z"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 10, 0), ZoneId.of("Z"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 13, 10, 0), ZoneId.of("Z"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 15, 10, 0), ZoneId.of("Z"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 17, 10, 0), ZoneId.of("Z"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 19, 10, 0), ZoneId.of("Z"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 21, 10, 0), ZoneId.of("Z"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 23, 10, 0), ZoneId.of("Z"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 25, 10, 0), ZoneId.of("Z"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 27, 10, 0), ZoneId.of("Z"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 29, 10, 0), ZoneId.of("Z"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 12, 1, 10, 0), ZoneId.of("Z"))
              ));
        assertEquals(expectedDates, madeDates);
    }
    
    /** Tests individual VEvent */
    @Test
    public void individualTest1()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 30));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 11, 10, 30)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    /** Tests VEvent with RDATE VEvent */
    @Test
    public void canStreamRDate()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceDates(new RecurrenceDates(LocalDateTime.of(2015, 11, 12, 10, 0)
                                     , LocalDateTime.of(2015, 11, 14, 12, 0)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 12, 10, 0)
              , LocalDateTime.of(2015, 11, 14, 12, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    @Test
    public void getWeekly2ChangeStart()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withInterval(2)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));

        LocalDateTime start2 = LocalDateTime.of(2015, 12, 6, 0, 0);
        List<Temporal> madeDates2 = e
                .streamRecurrences(start2)
                .limit(3)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates2 = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 12, 7, 10, 0)
              , LocalDateTime.of(2015, 12, 9, 10, 0)
              , LocalDateTime.of(2015, 12, 11, 10, 0)
                ));
        assertEquals(expectedDates2, madeDates2);
    }
    
    // ten years in future
    @Test
    public void getWeekly2FarFuture()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withInterval(2)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));
        
        LocalDateTime start = LocalDateTime.of(2025, 11, 10, 0, 0);
        List<Temporal> madeDates = e
                .streamRecurrences(start)
                .limit(3)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2025, 11, 10, 10, 0)
              , LocalDateTime.of(2025, 11, 12, 10, 0)
              , LocalDateTime.of(2025, 11, 14, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        
        LocalDateTime start2 = LocalDateTime.of(2015, 11, 11, 0, 0);
        List<Temporal> madeDates2 = e
                .streamRecurrences(start2)
                .limit(2)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates2 = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
                ));
        assertEquals(expectedDates2, madeDates2);
        
        LocalDateTime start3 = LocalDateTime.of(2025, 11, 12, 0, 0);
        List<Temporal> madeDates3 = e
                .streamRecurrences(start3)
                .limit(3)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates3 = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2025, 11, 12, 10, 0)
              , LocalDateTime.of(2025, 11, 14, 10, 0)
              , LocalDateTime.of(2025, 11, 24, 10, 0)
                ));
        assertEquals(expectedDates3, madeDates3);

        LocalDateTime start4 = LocalDateTime.of(2025, 11, 17, 0, 0);
        List<Temporal> madeDates4 = e
                .streamRecurrences(start4)
                .limit(3)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates4 = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2025, 11, 24, 10, 0)
              , LocalDateTime.of(2025, 11, 26, 10, 0)
              , LocalDateTime.of(2025, 11, 28, 10, 0)
                ));
        assertEquals(expectedDates4, madeDates4);
    }
    
    // Whole day tests
    
    @Test
    public void makeDatesWholeDayDaily2()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDate.of(2015, 11, 9))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withCount(6)
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(3));
        List<Temporal> madeDates = e               
                .streamRecurrences()
                .collect(Collectors.toList());
        List<LocalDate> expectedDates = new ArrayList<>(Arrays.asList(
                LocalDate.of(2015, 11, 9)
              , LocalDate.of(2015, 11, 12)
              , LocalDate.of(2015, 11, 15)
              , LocalDate.of(2015, 11, 18)
              , LocalDate.of(2015, 11, 21)
              , LocalDate.of(2015, 11, 24)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    @Test
    public void makeDatesWholeDayDaily3()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDate.of(2015, 11, 9))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withUntil(LocalDate.of(2015, 11, 24))
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(3));
        List<Temporal> madeDates = e                
                .streamRecurrences()
                .collect(Collectors.toList());
        List<LocalDate> expectedDates = new ArrayList<>(Arrays.asList(
                LocalDate.of(2015, 11, 9)
              , LocalDate.of(2015, 11, 12)
              , LocalDate.of(2015, 11, 15)
              , LocalDate.of(2015, 11, 18)
              , LocalDate.of(2015, 11, 21)
              , LocalDate.of(2015, 11, 24)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    @Test // LocalDate
    public void canChangeToWholeDay()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                .withFrequency(FrequencyType.DAILY));
        e.setDateTimeStart(new DateTimeStart(LocalDate.of(2015, 11, 9))); // change to whole-day
        {
            List<Temporal> madeDates = e
                    .streamRecurrences()
                    .limit(6)
                    .collect(Collectors.toList());
            List<LocalDate> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDate.of(2015, 11, 9)
                  , LocalDate.of(2015, 11, 10)
                  , LocalDate.of(2015, 11, 11)
                  , LocalDate.of(2015, 11, 12)
                  , LocalDate.of(2015, 11, 13)
                  , LocalDate.of(2015, 11, 14)
                    ));
            assertEquals(expectedDates, madeDates);
        }
        
        // Change back to date/time
        e.setDateTimeStart(new DateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))); // change to date/time
        { // start date/time
            List<Temporal> madeDates = e                
                    .streamRecurrences()
                    .limit(6)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 11, 10, 0)
                  , LocalDateTime.of(2015, 11, 12, 10, 0)
                  , LocalDateTime.of(2015, 11, 13, 10, 0)
                  , LocalDateTime.of(2015, 11, 14, 10, 0)
                    ));
            assertEquals(expectedDates, madeDates);
        }
    }
    
    @Test // tests cached stream ability to reset when RRule and start changes
    public void canChangeStartStreamTest()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                .withFrequency(FrequencyType.DAILY));

        { // initialize stream
            List<Temporal> madeDates = e
                    .streamRecurrences()
                    .limit(50)
                    .collect(Collectors.toList());
            Temporal seed = LocalDateTime.of(2015, 11, 9, 10, 00);
            List<Temporal> expectedDates = Stream
                    .iterate(seed, a -> a.plus(1, ChronoUnit.DAYS))
                    .limit(50)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, madeDates);
        }
        
        e.setDateTimeStart(new DateTimeStart(LocalDateTime.of(2015, 11, 10, 10, 0))); // change start
        { // make new stream
            List<Temporal> madeDates = e
                    .streamRecurrences(LocalDateTime.of(2015, 12, 9, 10, 0))
//                    .recurrenceStreamer().stream(LocalDateTime.of(2015, 12, 9, 10, 0))
                    .limit(50)
                    .collect(Collectors.toList());
            Temporal seed = LocalDateTime.of(2015, 12, 9, 10, 0);
            List<Temporal> expectedDates = Stream
                    .iterate(seed, a -> a.plus(1, ChronoUnit.DAYS))
                    .limit(50)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, madeDates);
        }

        // request date beyond first cached date to test cache system
        Temporal t = e.streamRecurrences(LocalDateTime.of(2016, 12, 25, 10, 0)).findFirst().get();
        assertEquals(LocalDateTime.of(2016, 12, 25, 10, 0), t);
    }
    
    @Test // tests cached stream ability to reset when RRule and start changes
    public void canChangeRRuleStreamTest()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                .withFrequency(FrequencyType.DAILY));        
        { // initialize stream
            List<Temporal> madeDates = e
                    .streamRecurrences()
                    .limit(50)
                    .collect(Collectors.toList());
            Temporal seed = LocalDateTime.of(2015, 11, 9, 10, 00);
            List<Temporal> expectedDates = Stream
                    .iterate(seed, a -> a.plus(1, ChronoUnit.DAYS))
                    .limit(50)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, madeDates);
        }
System.out.println("here1:");
        // Change RRule
        e.setRecurrenceRule(new RecurrenceRule2()
                .withFrequency(FrequencyType.WEEKLY)
                .withInterval(2)
                .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));
        System.out.println("here2:");

        { // check new repeatable stream
            List<Temporal> madeDates = e
                    .streamRecurrences()
                    .limit(50)
                    .collect(Collectors.toList());
            Temporal seed = LocalDateTime.of(2015, 11, 9, 10, 00);
            List<Temporal> expectedDates = Stream
                    .iterate(seed, a -> a.plus(2, ChronoUnit.WEEKS))
                    .flatMap(d -> 
                    {
                        List<Temporal> days = new ArrayList<>();
                        days.add(d); // Mondays
                        days.add(d.plus(2, ChronoUnit.DAYS)); // Wednesdays
                        days.add(d.plus(4, ChronoUnit.DAYS)); // Fridays
                        return days.stream();
                    })
                    .limit(50)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, madeDates);
        }
        System.out.println("here3:");

        // request date beyond first cached date to test cache system
        Temporal date = e.streamRecurrences(LocalDateTime.of(2015, 12, 9, 10, 0)).findFirst().get();
        assertEquals(LocalDateTime.of(2015, 12, 9, 10, 0), date);
    }
    
    @Test
    public void canFindPreviousStreamTemporal()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 7, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));
        e.streamRecurrences().limit(100).collect(Collectors.toList()); // set cache
        assertEquals(LocalDateTime.of(2016, 1, 20, 10, 0), e.recurrenceStreamer().previousValue(LocalDateTime.of(2016, 1, 21, 10, 0)));

        VEvent e2 = new VEvent() // without cache
                .withDateTimeStart(LocalDate.of(2015, 11, 9))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withCount(6)
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(3));
        assertEquals(LocalDate.of(2015, 11, 24), e2.recurrenceStreamer().previousValue(LocalDate.of(2015, 12, 31)));
    }
    
    // Tests added components with recurrence ID to parent's list of recurrences
    @Test
    public void canHandleRecurrenceID()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 7, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));
        e.streamRecurrences().limit(100).collect(Collectors.toList()); // set cache
        assertEquals(LocalDateTime.of(2016, 1, 20, 10, 0), e.recurrenceStreamer().previousValue(LocalDateTime.of(2016, 1, 21, 10, 0)));

        VEvent e2 = new VEvent() // without cache
                .withDateTimeStart(LocalDate.of(2015, 11, 9))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withCount(6)
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(3));
        assertEquals(LocalDate.of(2015, 11, 24), e2.recurrenceStreamer().previousValue(LocalDate.of(2015, 12, 31)));
    }
    
    @Test // TODO - FOR CALENDAR
    public void canHandleExceptions()
    {
        
    }
}
