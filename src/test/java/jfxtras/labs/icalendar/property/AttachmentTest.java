package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.net.URISyntaxException;

import org.junit.Test;

import jfxtras.labs.icalendar.parameters.Encoding.EncodingType;
import jfxtras.labs.icalendar.parameters.ValueType.ValueEnum;
import jfxtras.labs.icalendar.properties.component.descriptive.Attachment;
import jfxtras.labs.icalendar.properties.component.descriptive.AttachmentBase64;
import jfxtras.labs.icalendar.properties.component.descriptive.AttachmentURI;

public class AttachmentTest
{
    @Test
    public void canParseAttachementSimple() throws URISyntaxException
    {
        Attachment<?,?> property = new AttachmentURI("ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com");
        String expectedContentLine = "ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com";
        String madeContentLine = property.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
    }
    
    @Test
    public void canParseAttachementComplex() throws URISyntaxException
    {
        String contentLine = "ATTACH;FMTTYPE=application/postscript:ftp://example.com/pub/reports/r-960812.ps";
        Attachment<?,?> madeProperty = new AttachmentURI(contentLine);
        Attachment<?,?> expectedProperty = new AttachmentURI("ftp://example.com/pub/reports/r-960812.ps")
                .withFormatType("application/postscript");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(contentLine, expectedProperty.toContentLine());
    }
    
    @Test
    public void canParseAttachementComplex2() throws URISyntaxException
    {
        String contentLine = "ATTACH;FMTTYPE=text/plain;ENCODING=BASE64;VALUE=BINARY:TG9yZW";
        AttachmentBase64 madeProperty = new AttachmentBase64(contentLine);
        AttachmentBase64 expectedProperty = new AttachmentBase64("TG9yZW")
                .withFormatType("text/plain")
                .withEncoding(EncodingType.BASE64)
                .withValueType(ValueEnum.BINARY);
        assertEquals(expectedProperty, madeProperty);
        assertEquals(contentLine, expectedProperty.toContentLine());
    }
    
    @Test
    public void canCopyAttachement() throws URISyntaxException
    {
        String contentLine = "ATTACH;FMTTYPE=text/plain;ENCODING=BASE64;VALUE=BINARY:TG9yZW";
        AttachmentBase64 expectedProperty = new AttachmentBase64(contentLine);
        AttachmentBase64 madeProperty = new AttachmentBase64(expectedProperty);
        assertEquals(expectedProperty, madeProperty);
        assertFalse(expectedProperty == madeProperty);
    }
}
