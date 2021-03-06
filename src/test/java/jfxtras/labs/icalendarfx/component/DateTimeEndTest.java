package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import jfxtras.labs.icalendarfx.components.VComponentDateTimeEnd;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VFreeBusy;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeStart;

/**
 * Test following components:
 * @see VEvent
 * @see VFreeBusy
 * 
 * for the following properties:
 * @see DateTimeEnd
 * 
 * @author David Bal
 *
 */
public class DateTimeEndTest
{
    @Test
    public void canBuildLastModified() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
        List<VComponentDateTimeEnd<?>> components = Arrays.asList(
                new VEvent()
                        .withDateTimeEnd(DateTimeEnd.parse("20160306T080000Z")),
                new VFreeBusy()
                        .withDateTimeEnd(DateTimeEnd.parse("20160306T080000Z"))
                );
        
        for (VComponentDateTimeEnd<?> builtComponent : components)
        {
            String componentName = builtComponent.componentType().toString();            
            String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                    "DTEND:20160306T080000Z" + System.lineSeparator() +
                    "END:" + componentName;
                    
            VComponentDateTimeEnd<?> parsedComponent = builtComponent
                    .getClass()
                    .getConstructor(String.class)
                    .newInstance(expectedContent);
            assertEquals(parsedComponent, builtComponent);
            assertEquals(expectedContent, builtComponent.toContent());            
        }
    }
    
    @Test (expected = DateTimeException.class)
    @Ignore // JUnit won't recognize exception - exception is thrown in listener is cause
    public void canCatchWrongDateType()
    {
        new VEvent()
                .withDateTimeStart(LocalDate.of(1997, 3, 1))
                .withDateTimeEnd("20160306T080000Z");
    }
    
    @Test (expected = DateTimeException.class)
    @Ignore // JUnit won't recognize exception - exception is thrown in listener is cause
    public void canCatchWrongDateType2()
    {
       new VEvent()
                .withDateTimeEnd("20160306T080000Z")
                .withDateTimeStart(LocalDate.of(1997, 3, 1));
    }
    
    @Test (expected = DateTimeException.class)
    @Ignore // JUnit won't recognize exception - exception is thrown in listener is cause
    public void canCatchWrongDateType3()
    {
        VEvent builtComponent = new VEvent();
        builtComponent.setDateTimeEnd(new DateTimeEnd(LocalDateTime.of(2016, 3, 6, 8, 0)));
        builtComponent.setDateTimeStart(new DateTimeStart(LocalDate.of(1997, 3, 1)));
    }
}
