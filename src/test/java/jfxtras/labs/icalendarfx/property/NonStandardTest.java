package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import jfxtras.labs.icalendarfx.parameters.ValueType;
import jfxtras.labs.icalendarfx.properties.component.misc.NonStandardProperty;

public class NonStandardTest
{
    @Test
    public void canParseNonStandard1()
    {
        String content = "X-MYPROP;VALUE=BOOLEAN:FALSE";
        NonStandardProperty madeProperty = NonStandardProperty.parse(content);
        assertEquals(content, madeProperty.toContent());
        NonStandardProperty expectedProperty = NonStandardProperty.parse("FALSE")
                .withPropertyName("X-MYPROP")
                .withValueParameter(ValueType.BOOLEAN);
        assertEquals(expectedProperty, madeProperty);
        assertEquals(Boolean.FALSE, madeProperty.getValue());
    }
    
    @Test
    public void canParseNonStandard2() throws URISyntaxException
    {
        String content = "X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au";
        NonStandardProperty madeProperty = NonStandardProperty.parse(content);
        assertEquals(content, madeProperty.toContent());
        NonStandardProperty expectedProperty = NonStandardProperty.parse("http://www.example.org/mysubj.au")
                .withFormatType("audio/basic")
                .withValueParameter(ValueType.UNIFORM_RESOURCE_IDENTIFIER)
                .withPropertyName("X-ABC-MMSUBJ");

        assertEquals(expectedProperty, madeProperty);
        assertEquals(ValueType.UNIFORM_RESOURCE_IDENTIFIER, madeProperty.getValueParameter().getValue());
        assertEquals(new URI("http://www.example.org/mysubj.au"), madeProperty.getValue());
    }

}
