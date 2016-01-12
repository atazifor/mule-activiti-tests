package main.com;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

public class JacksonMapper extends ObjectMapper {
	{configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);}
	public JacksonMapper() {
		super();
		System.out.println("## my jackson mapper  ###");
		configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		getJsonFactory().configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
	}
}
