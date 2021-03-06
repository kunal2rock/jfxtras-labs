package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.component.change.DateTimeCreated;
import jfxtras.labs.icalendarfx.properties.component.change.LastModified;
import jfxtras.labs.icalendarfx.properties.component.change.Sequence;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Attachment;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Categories;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Classification;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Classification.ClassificationType;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Status.StatusType;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.labs.icalendarfx.properties.component.recurrence.ExceptionDates;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceDates;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.labs.icalendarfx.properties.component.relationship.Contact;
import jfxtras.labs.icalendarfx.properties.component.relationship.RecurrenceId;
import jfxtras.labs.icalendarfx.properties.component.relationship.RelatedTo;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeStart;

/**
 * Test following components:
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * 
 * for the following properties:
 * @see Attachment - extended from Describable
 * @see Categories
 * @see Classification
 * @see Contact
 * @see DateTimeCreated
 * @see ExceptionDates
 * @see LastModified - extended from LastModified
 * @see RecurrenceId
 * @see RecurrenceRule - extended from Repeatable
 * @see RecurrenceDates - extended from Repeatable
 * @see RelatedTo
 * @see Sequence
 * @see Status
 * @see Summary - extended from Describable
 * 
 * @author David Bal
 *
 */
public class DisplayableTest // extends FxExceptionPropagatorTest
{
    
//    @Override
//    public Parent getRootNode()
//    {
//        ResourceBundle resources = ResourceBundle.getBundle("jfxtras.labs.icalendaragenda.ICalendarAgenda", Locale.getDefault());
//        Settings.setup(resources);
//        vbox = new VBox();
//        vbox.setPrefSize(1000, 800);
//        return vbox;
//    }
//    protected VBox vbox = null; // cannot make this final and assign upon construction
    
    @Test
    public void canBuildDisplayable() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
        List<VComponentDisplayable<?>> components = Arrays.asList(
                new VEvent()
                    .withStatus(StatusType.NEEDS_ACTION)
                    .withSequence(2)
                    .withRelatedTo("jsmith.part7.19960817T083000.xyzMail@example.com",
                            "RELATED-TO;RELTYPE=SIBLING:19960401-080045-4000F192713@example.com")
                    .withRecurrenceId(ZonedDateTime.of(LocalDateTime.of(2016, 1, 1, 12, 0), ZoneId.of("Z")))
                    .withSummary("a test summary")
                    .withRecurrenceDates("RDATE:19960302T010000Z,19960304T010000Z")
                    .withRecurrenceRule("RRULE:FREQ=DAILY")
                    .withDateTimeLastModified("20160306T080000Z")
                    .withAttachments(Attachment.parse("ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com"))
                    .withDateTimeCreated("20160420T080000Z")
                    .withCategories("group03","group04","group05")
                    .withCategories("group06")
                    .withClassification(ClassificationType.PUBLIC)
                    .withContacts("CONTACT:Jim Dolittle\\, ABC Industries\\, +1-919-555-1234", "Harry Potter\\, Hogwarts\\, by owl")
                    .withExceptionDates("EXDATE:19960301T010000Z,19960304T010000Z,19960307T010000Z"),
                new VTodo()
                    .withStatus(StatusType.NEEDS_ACTION)
                    .withSequence(2)
                    .withRelatedTo("jsmith.part7.19960817T083000.xyzMail@example.com",
                            "RELATED-TO;RELTYPE=SIBLING:19960401-080045-4000F192713@example.com")
                    .withRecurrenceId(ZonedDateTime.of(LocalDateTime.of(2016, 1, 1, 12, 0), ZoneId.of("Z")))
                    .withSummary("a test summary")
                    .withRecurrenceDates("RDATE:19960302T010000Z,19960304T010000Z")
                    .withRecurrenceRule("RRULE:FREQ=DAILY")
                    .withDateTimeLastModified("20160306T080000Z")
                    .withAttachments(Attachment.parse("ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com"))
                    .withDateTimeCreated("20160420T080000Z")
                    .withCategories("group03","group04","group05")
                    .withCategories("group06")
                    .withClassification(ClassificationType.PUBLIC)
                    .withContacts("CONTACT:Jim Dolittle\\, ABC Industries\\, +1-919-555-1234", "Harry Potter\\, Hogwarts\\, by owl")
                    .withExceptionDates("EXDATE:19960301T010000Z,19960304T010000Z,19960307T010000Z"),
                new VJournal()
                    .withStatus(StatusType.NEEDS_ACTION)
                    .withSequence(2)
                    .withRelatedTo("jsmith.part7.19960817T083000.xyzMail@example.com",
                            "RELATED-TO;RELTYPE=SIBLING:19960401-080045-4000F192713@example.com")
                    .withRecurrenceId(ZonedDateTime.of(LocalDateTime.of(2016, 1, 1, 12, 0), ZoneId.of("Z")))
                    .withSummary("a test summary")
                    .withRecurrenceDates("RDATE:19960302T010000Z,19960304T010000Z")
                    .withRecurrenceRule("RRULE:FREQ=DAILY")
                    .withDateTimeLastModified("20160306T080000Z")
                    .withAttachments(Attachment.parse("ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com"))
                    .withDateTimeCreated("20160420T080000Z")
                    .withCategories("group03","group04","group05")
                    .withCategories("group06")
                    .withClassification(ClassificationType.PUBLIC)
                    .withContacts("CONTACT:Jim Dolittle\\, ABC Industries\\, +1-919-555-1234", "Harry Potter\\, Hogwarts\\, by owl")
                    .withExceptionDates("EXDATE:19960301T010000Z,19960304T010000Z,19960307T010000Z")
                );
        
        List<ZonedDateTime> expectedDates = Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(1996, 3, 2, 1, 0), ZoneId.of("Z")),
                ZonedDateTime.of(LocalDateTime.of(1996, 3, 3, 1, 0), ZoneId.of("Z")),
                ZonedDateTime.of(LocalDateTime.of(1996, 3, 5, 1, 0), ZoneId.of("Z")),
                ZonedDateTime.of(LocalDateTime.of(1996, 3, 9, 1, 0), ZoneId.of("Z")),
                ZonedDateTime.of(LocalDateTime.of(1996, 3, 11, 1, 0), ZoneId.of("Z")),
                ZonedDateTime.of(LocalDateTime.of(1996, 3, 13, 1, 0), ZoneId.of("Z"))
                );
        
        for (VComponentDisplayable<?> builtComponent : components)
        {
            String componentName = builtComponent.componentType().toString();
            String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                    "ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com" + System.lineSeparator() +
                    "CATEGORIES:group03,group04,group05" + System.lineSeparator() +
                    "CATEGORIES:group06" + System.lineSeparator() +
                    "CLASS:PUBLIC" + System.lineSeparator() +
                    "CONTACT:Jim Dolittle\\, ABC Industries\\, +1-919-555-1234" + System.lineSeparator() +
                    "CONTACT:Harry Potter\\, Hogwarts\\, by owl" + System.lineSeparator() +
                    "CREATED:20160420T080000Z" + System.lineSeparator() +
                    "EXDATE:19960301T010000Z,19960304T010000Z,19960307T010000Z" + System.lineSeparator() +
                    "LAST-MODIFIED:20160306T080000Z" + System.lineSeparator() +
                    "RDATE:19960302T010000Z,19960304T010000Z" + System.lineSeparator() +
                    "RECURRENCE-ID:20160101T120000Z" + System.lineSeparator() +
                    "RELATED-TO:jsmith.part7.19960817T083000.xyzMail@example.com" + System.lineSeparator() +
                    "RELATED-TO;RELTYPE=SIBLING:19960401-080045-4000F192713@example.com" + System.lineSeparator() +
                    "RRULE:FREQ=DAILY" + System.lineSeparator() +
                    "SEQUENCE:2" + System.lineSeparator() +
                    "STATUS:NEEDS-ACTION" + System.lineSeparator() +
                    "SUMMARY:a test summary" + System.lineSeparator() +
                    "END:" + componentName;

            VComponentDisplayable<?> parsedComponent = builtComponent
                    .getClass()
                    .getConstructor(String.class)
                    .newInstance(expectedContent);
            assertEquals(parsedComponent, builtComponent);
            assertEquals(expectedContent, builtComponent.toContent());     
            
            builtComponent.setRecurrenceRule("RRULE:FREQ=DAILY;INTERVAL=2");
            builtComponent.setDateTimeStart(DateTimeStart.parse(ZonedDateTime.class, "19960301T010000Z"));
            List<Temporal> myDates = builtComponent.streamRecurrences().limit(6).collect(Collectors.toList());
            assertEquals(expectedDates, myDates);
        }
    }
    
    @Test
    public void exceptionTest1()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withCount(6)
                        .withFrequency("DAILY")
                        .withInterval(3))
                .withExceptionDates(new ExceptionDates(LocalDateTime.of(2015, 11, 12, 10, 0)
                                     , LocalDateTime.of(2015, 11, 15, 10, 0)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
              , LocalDateTime.of(2015, 11, 21, 10, 0)
              , LocalDateTime.of(2015, 11, 24, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "EXDATE:20151112T100000,20151115T100000";
        assertEquals(expectedContent, e.getExceptionDates().get(0).toContent());
        String expectedContent2 = "RRULE:FREQ=DAILY;INTERVAL=3;COUNT=6";
        assertEquals(expectedContent2, e.getRecurrenceRule().toContent());
    }
    
    // Google test
    @Test
    public void canStreamGoogleWithExDates()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 7, 12, 30), ZoneId.of("America/Los_Angeles")))
                .withExceptionDates(new ExceptionDates(
                            ZonedDateTime.of(LocalDateTime.of(2016, 2, 10, 12, 30), ZoneId.of("America/Los_Angeles"))
                          , ZonedDateTime.of(LocalDateTime.of(2016, 2, 12, 12, 30), ZoneId.of("America/Los_Angeles"))
                          , ZonedDateTime.of(LocalDateTime.of(2016, 2, 9, 12, 30), ZoneId.of("America/Los_Angeles"))))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2016, 5, 12, 19, 30, 0), ZoneId.of("Z"))));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<Temporal> expectedDates = new ArrayList<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2016, 2, 7, 12, 30), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 8, 12, 30), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 11, 12, 30), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 13, 12, 30), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 12, 30), ZoneId.of("America/Los_Angeles"))
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    @Test
    public void canChangeGoogleWithExDatesToWholeDay()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 7, 12, 30), ZoneId.of("America/Los_Angeles")))
                .withExceptionDates(new ExceptionDates(
                            ZonedDateTime.of(LocalDateTime.of(2016, 2, 10, 12, 30), ZoneId.of("America/Los_Angeles"))
                          , ZonedDateTime.of(LocalDateTime.of(2016, 2, 12, 12, 30), ZoneId.of("America/Los_Angeles"))
                          , ZonedDateTime.of(LocalDateTime.of(2016, 2, 9, 12, 30), ZoneId.of("America/Los_Angeles"))))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2016, 5, 12, 19, 30, 0), ZoneId.of("Z"))));
        e.setExceptionDates(null);
        e.setDateTimeStart(new DateTimeStart(LocalDate.of(2016, 2, 7)));
        e.setExceptionDates(FXCollections.observableArrayList(new ExceptionDates(
                            LocalDate.of(2016, 2, 10)
                          , LocalDate.of(2016, 2, 12)
                          , LocalDate.of(2016, 2, 9)
                          )));
        Temporal start = LocalDate.of(2016, 2, 7);
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<Temporal> expectedDates = new ArrayList<>(Arrays.asList(
                LocalDate.of(2016, 2, 7)
              , LocalDate.of(2016, 2, 8)
              , LocalDate.of(2016, 2, 11)
              , LocalDate.of(2016, 2, 13)
              , LocalDate.of(2016, 2, 14)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    @Test (expected = DateTimeException.class)
    @Ignore // can't catch exception in listener
    public void canHandleDTStartTypeChange()
    {
        VEvent component = new VEvent()
            .withDateTimeStart(LocalDate.of(1997, 3, 1))
            .withExceptionDates("EXDATE;VALUE=DATE:19970304,19970504,19970704,19970904");
//      Platform.runLater(() -> component.setDateTimeStart("20160302T223316Z"));      
        component.setDateTimeStart(DateTimeStart.parse(ZonedDateTime.class, "20160302T223316Z")); // invalid
    }
    
    @Test (expected = DateTimeException.class)
    public void canCatchWrongDateType()
    {
        VEvent component = new VEvent()
                .withDateTimeStart(LocalDate.of(1997, 3, 1));
        ObservableList<ExceptionDates> exceptions = FXCollections.observableArrayList();
        exceptions.add(ExceptionDates.parse("20160228T093000"));
        component.setExceptionDates(exceptions); // invalid        
    }
    
    @Test //(expected = DateTimeException.class)
    @Ignore // JUnit won't recognize exception - exception is thrown in listener is cause
    public void canCatchWrongRecurrenceIdType()
    {
        new VEvent()
                .withDateTimeStart(LocalDate.of(1997, 3, 1))
                .withRecurrenceId("20160306T080000Z");
    }
    
    @Test (expected = DateTimeException.class)
    @Ignore // JUnit won't recognize exception - exception is thrown in listener is cause
    public void canCatchWrongRecurrenceIdType2()
    {
       new VEvent()
                .withRecurrenceId("20160306T080000Z")
                .withDateTimeStart(LocalDate.of(1997, 3, 1));
    }
        
    @Test (expected = DateTimeException.class)
    @Ignore // JUnit won't recognize exception - exception is thrown in listener is cause
    public void canCatchWrongRecurrenceIdType3()
    {
//        TestUtil.runThenWaitForPaintPulse( () -> {
//            try
//            {
            VEvent builtComponent = new VEvent();
            builtComponent.setDateTimeStart(new DateTimeStart(LocalDate.of(1997, 3, 1)));
            builtComponent.setRecurrenceId(new RecurrenceId(LocalDateTime.of(2016, 3, 6, 8, 0)));
//            } catch (Exception e)
//            {
//                System.out.println("got something" + e.getMessage());
//            }
//        });
//        exception.expect(DateTimeException.class);
    }
    
    @Test (expected = ClassCastException.class)
    public void canCatchWrongExceptionType1()
    {
        new VEvent().withExceptionDates(LocalDate.of(2016, 4, 27),
                LocalDateTime.of(2016, 4, 27, 12, 0));
    }
}
