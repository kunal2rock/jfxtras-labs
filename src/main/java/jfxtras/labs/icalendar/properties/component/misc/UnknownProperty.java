package jfxtras.labs.icalendar.properties.component.misc;

import java.net.URI;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.parameters.AlternateText;
import jfxtras.labs.icalendar.parameters.Encoding;
import jfxtras.labs.icalendar.parameters.Encoding.EncodingType;
import jfxtras.labs.icalendar.parameters.FormatType;
import jfxtras.labs.icalendar.parameters.FreeBusyType;
import jfxtras.labs.icalendar.parameters.FreeBusyType.FreeBusyTypeEnum;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.Range;
import jfxtras.labs.icalendar.parameters.Range.RangeType;
import jfxtras.labs.icalendar.parameters.TimeZoneIdentifierParameter;
import jfxtras.labs.icalendar.properties.PropertyAltText;
import jfxtras.labs.icalendar.properties.PropertyAttachment;
import jfxtras.labs.icalendar.properties.PropertyAttendee;
import jfxtras.labs.icalendar.properties.PropertyBaseAttendee;
import jfxtras.labs.icalendar.properties.PropertyDateTime;
import jfxtras.labs.icalendar.properties.PropertyFreeBusy;
import jfxtras.labs.icalendar.properties.PropertyRecurrenceID;

/**
 * 
 * contains all parameters
 * 
 * @author David Bal
 *
 */
// TODO - DO I WANT TO MAKE INTERFACES FOR PARAMETERS?
public abstract class UnknownProperty<T, U> extends PropertyBaseAttendee<T, U> implements PropertyAttendee<U>, PropertyAltText<U>, PropertyAttachment<U>, PropertyFreeBusy<U>, PropertyRecurrenceID<U>, PropertyDateTime<U>
{
    /**
     * ALTREP : Alternate Text Representation
     * To specify an alternate text representation for the property value.
     * 
     * Example:
     * DESCRIPTION;ALTREP="CID:part3.msg.970415T083000@example.com":
     *  Project XYZ Review Meeting will include the following agenda
     *   items: (a) Market Overview\, (b) Finances\, (c) Project Man
     *  agement
     *
     *The "ALTREP" property parameter value might point to a "text/html"
     *content portion.
     *
     * Content-Type:text/html
     * Content-Id:<part3.msg.970415T083000@example.com>
     *
     * <html>
     *   <head>
     *    <title></title>
     *   </head>
     *   <body>
     *     <p>
     *       <b>Project XYZ Review Meeting</b> will include
     *       the following agenda items:
     *       <ol>
     *         <li>Market Overview</li>
     *         <li>Finances</li>
     *         <li>Project Management</li>
     *       </ol>
     *     </p>
     *   </body>
     * </html>
     */
    @Override
    public AlternateText getAlternateText() { return (alternateText == null) ? null : alternateText.get(); }
    @Override
    public ObjectProperty<AlternateText> alternateTextProperty()
    {
        if (alternateText == null)
        {
            alternateText = new SimpleObjectProperty<>(this, ParameterEnum.ALTERNATE_TEXT_REPRESENTATION.toString());
        }
        return alternateText;
    }
    private ObjectProperty<AlternateText> alternateText;
    @Override
    public void setAlternateText(AlternateText alternateText)
    {
        if (alternateText != null)
        {
            alternateTextProperty().set(alternateText);
        }
    }
    public void setAlternateText(String value) { setAlternateText(new AlternateText(value)); }
    public T withAlternateText(AlternateText altrep) { setAlternateText(altrep); return (T) this; }
    public T withAlternateText(URI value) { setAlternateText(new AlternateText(value)); return (T) this; }
    public T withAlternateText(String content) { setAlternateText(content); return (T) this; }
    
    /**
     * ENCODING: Incline Encoding
     * RFC 5545, 3.2.7, page 18
     * 
     * Specify an alternate inline encoding for the property value.
     * Values can be "8BIT" text encoding defined in [RFC2045]
     *               "BASE64" binary encoding format defined in [RFC4648]
     *
     * If the value type parameter is ";VALUE=BINARY", then the inline
     * encoding parameter MUST be specified with the value" ;ENCODING=BASE64".
     */
    @Override
    public Encoding getEncoding() { return (encoding == null) ? null : encoding.get(); }
    @Override
    public ObjectProperty<Encoding> encodingProperty()
    {
        if (encoding == null)
        {
            encoding = new SimpleObjectProperty<>(this, ParameterEnum.INLINE_ENCODING.toString());
        }
        return encoding;
    }
    private ObjectProperty<Encoding> encoding;
    @Override
    public void setEncoding(Encoding encoding)
    {
        if (encoding.getValue() != EncodingType.BASE64)
        {
            throw new IllegalArgumentException("Attachment property only allows ENCODING to be set to" + EncodingType.BASE64);
        }

        if (encoding != null)
        {
            encodingProperty().set(encoding);
        }
    }
    public T withEncoding(Encoding encoding) { setEncoding(encoding); return (T) this; }
    public T withEncoding(EncodingType encoding) { setEncoding(new Encoding(encoding)); return (T) this; }

    /**
     * FBTYPE: Incline Free/Busy Time Type
     * RFC 5545, 3.2.9, page 20
     * 
     * To specify the free or busy time type.
     * 
     * Values can be = "FBTYPE" "=" ("FREE" / "BUSY" / "BUSY-UNAVAILABLE" / "BUSY-TENTATIVE"
     */
    @Override
    public FreeBusyType getFreeBusyType() { return (freeBusyType == null) ? null : freeBusyType.get(); }
    @Override
    public ObjectProperty<FreeBusyType> freeBusyTypeProperty()
    {
        if (freeBusyType == null)
        {
            freeBusyType = new SimpleObjectProperty<>(this, ParameterEnum.INLINE_ENCODING.toString());
        }
        return freeBusyType;
    }
    private ObjectProperty<FreeBusyType> freeBusyType;
    @Override
    public void setFreeBusyType(FreeBusyType freeBusyType)
    {
        if (freeBusyType != null)
        {
            freeBusyTypeProperty().set(freeBusyType);
        }
    }
    public void setFreeBusyType(FreeBusyTypeEnum type) { setFreeBusyType(new FreeBusyType(type)); }
    public T withFreeBusyType(FreeBusyType freeBusyType) { setFreeBusyType(freeBusyType); return (T)this; }
    public T withFreeBusyType(FreeBusyTypeEnum type) { setFreeBusyType(type); return (T) this; }
    public T withFreeBusyType(String freeBusyType) { setFreeBusyType(new FreeBusyType(freeBusyType)); return (T) this; }

    /**
     * FMTTYPE: Format type parameter
     * RFC 5545, 3.2.8, page 19
     * specify the content type of a referenced object.
     */
    @Override
    public FormatType getFormatType() { return (formatType == null) ? null : formatType.get(); }
    @Override
    public ObjectProperty<FormatType> formatTypeProperty()
    {
        if (formatType == null)
        {
            formatType = new SimpleObjectProperty<>(this, ParameterEnum.FORMAT_TYPE.toString());
        }
        return formatType;
    }
    private ObjectProperty<FormatType> formatType;
    @Override
    public void setFormatType(FormatType formatType)
    {
        if (formatType != null)
        {
            formatTypeProperty().set(formatType);
        }
    }
    public T withFormatType(FormatType format) { setFormatType(format); return (T) this; }
    public T withFormatType(String format) { setFormatType(new FormatType(format)); return (T) this; }

    /**
     * RANGE
     * Recurrence Identifier Range
     * RFC 5545, 3.2.13, page 23
     * 
     * To specify the effective range of recurrence instances from
     *  the instance specified by the recurrence identifier specified by
     *  the property.
     * 
     * Example:
     * RECURRENCE-ID;RANGE=THISANDFUTURE:19980401T133000Z
     * 
     * @author David Bal
     *
     */
    @Override
    public Range getRange() { return (range == null) ? null : range.get(); }
    @Override
    public ObjectProperty<Range> rangeProperty()
    {
        if (range == null)
        {
            range = new SimpleObjectProperty<>(this, ParameterEnum.RECURRENCE_IDENTIFIER_RANGE.toString());
        }
        return range;
    }
    private ObjectProperty<Range> range;
    @Override
    public void setRange(Range range)
    {
        if (range != null)
        {
            rangeProperty().set(range);
        }
    }
    public void setRange(String value) { setRange(new Range(value)); }
    public T withRange(Range altrep) { setRange(altrep); return (T) this; }
    public T withRange(RangeType value) { setRange(new Range(value)); return (T) this; }
    public T withRange(String content) { setRange(content); return (T) this; }

    /**
     * TZID
     * Time Zone Identifier
     * To specify the identifier for the time zone definition for
     * a time component in the property value.
     * 
     * Examples:
     * DTSTART;TZID=America/New_York:19980119T020000
     */
    public TimeZoneIdentifierParameter getTimeZoneIdentifier() { return (timeZoneIdentifier == null) ? null : timeZoneIdentifier.get(); }
    public ObjectProperty<TimeZoneIdentifierParameter> timeZoneIdentifierProperty()
    {
        if (timeZoneIdentifier == null)
        {
            timeZoneIdentifier = new SimpleObjectProperty<>(this, ParameterEnum.TIME_ZONE_IDENTIFIER.toString());
        }
        return timeZoneIdentifier;
    }
    private ObjectProperty<TimeZoneIdentifierParameter> timeZoneIdentifier;
    public void setTimeZoneIdentifier(TimeZoneIdentifierParameter timeZoneIdentifier)
    {
        if (timeZoneIdentifier != null)
        {
            timeZoneIdentifierProperty().set(timeZoneIdentifier);
        }
    }
    public void setTimeZoneIdentifier(String value) { setTimeZoneIdentifier(new TimeZoneIdentifierParameter(value)); }
    public T withTimeZoneIdentifier(TimeZoneIdentifierParameter timeZoneIdentifier) { setTimeZoneIdentifier(timeZoneIdentifier); return (T) this; }
    public T withTimeZoneIdentifier(String content) { ParameterEnum.TIME_ZONE_IDENTIFIER.parse(this, content); return (T) this; }        

    /*
     * CONSTRUCTORS
     */
    
    public UnknownProperty(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public UnknownProperty(U value)
    {
        super(value);
    }
    
    public UnknownProperty(UnknownProperty<T, U> source)
    {
        super(source);
    }
    
    @Override
    protected U valueFromString(String propertyValueString)
    {
        return (U) propertyValueString;
    }
}