package jfxtras.labs.icalendarfx.parameters;

import jfxtras.labs.icalendarfx.parameters.AlarmTriggerRelationship.AlarmTriggerRelationshipType;

/**
 * RELATED
 * Alarm Trigger Relationship
 * RFC 5545, 3.2.14, page 24
 * 
 * To specify the relationship of the alarm trigger with
 *  respect to the start or end of the calendar component.
 * 
 * Example:
 * TRIGGER;RELATED=END:PT5M
 * 
 * @author David Bal
 *
 */
public class AlarmTriggerRelationship extends ParameterBase<AlarmTriggerRelationship, AlarmTriggerRelationshipType>
{
    /** Set START as default value */
    public AlarmTriggerRelationship()
    {
        super(AlarmTriggerRelationshipType.START);
    }

    public AlarmTriggerRelationship(AlarmTriggerRelationshipType value)
    {
        super(value);
    }

    public AlarmTriggerRelationship(AlarmTriggerRelationship source)
    {
        super(source);
    }
    
    @Override
    public void parseContent(String content)
    {
        setValue(AlarmTriggerRelationshipType.valueOf(content.toUpperCase()));
    }
    
    public enum AlarmTriggerRelationshipType
    {
        START,
        END;
    }

    public static AlarmTriggerRelationship parse(String content)
    {
        AlarmTriggerRelationship parameter = new AlarmTriggerRelationship();
        parameter.parseContent(content);
        return parameter;
    }
}
