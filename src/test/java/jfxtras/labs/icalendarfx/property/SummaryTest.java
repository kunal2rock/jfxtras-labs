package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;

public class SummaryTest
{    
    @Test
    public void canParseSummary1()
    {
        String content = "SUMMARY:TEST SUMMARY";
        Summary madeProperty = Summary.parse(content);
        assertEquals(content, madeProperty.toContent());
        Summary expectedProperty = Summary.parse("TEST SUMMARY");
        assertEquals(expectedProperty, madeProperty);
        assertEquals("TEST SUMMARY", madeProperty.getValue());
    }
    
    @Test
    public void canParseSummary2()
    {
        String content = "SUMMARY;ALTREP=\"cid:part1.0001@example.org\";LANGUAGE=en:Department Party";
        Summary madeProperty = Summary.parse(content);
        assertEquals(content, madeProperty.toContent());
        Summary expectedProperty = Summary.parse("Department Party")
                .withAlternateText("cid:part1.0001@example.org")
                .withLanguage("en");
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test
    public void canCopySummary()
    {
        String content = "SUMMARY;ALTREP=\"cid:part1.0001@example.org\";LANGUAGE=en:Department Party";
        Summary property1 = Summary.parse(content);
        Summary property2 = new Summary(property1);
        System.out.println(property2.toContent());
        assertEquals(property2, property1);
        assertFalse(property2 == property1);
    }
}
