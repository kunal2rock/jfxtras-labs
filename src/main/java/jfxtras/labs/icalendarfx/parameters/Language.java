package jfxtras.labs.icalendarfx.parameters;

/**
 * LANGUAGE
 * Language
 * RFC 5545, 3.2.10, page 21
 * 
 * To specify the language for text values in a property or property parameter.
 * 
 * Example:
 * SUMMARY;LANGUAGE=en-US:Company Holiday Party
 * 
 * @author David Bal
 *
 */
public class Language extends ParameterText<Language>
{
    public Language()
    {
        super();
    }

    public Language(Language source)
    {
        super(source);
    }

    public static Language parse(String content)
    {
        Language parameter = new Language();
        parameter.parseContent(content);
        return parameter;
    }
}
