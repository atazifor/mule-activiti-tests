package main.com;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class ActivitiProcessDefinitionsResponse implements Serializable{

	@JsonProperty("id")
	public String id;
	@JsonProperty("url")
	public String url;
	@JsonProperty("key")
	public String key;
	@JsonProperty("version")
	public Integer version;
	@JsonProperty("name")
	public String name;
	@JsonProperty("description")
	public String description;
	@JsonProperty("tenantId")
	public String tenantId;
	@JsonProperty("deploymentId")
	public String deploymentId;
	@JsonProperty("deploymentUrl")
	public String deploymentUrl;
	@JsonProperty("resource")
	public String resource;
	@JsonProperty("diagramResource")
	public Object diagramResource;
	@JsonProperty("category")
	public String category;
	@JsonProperty("graphicalNotationDefined")
	public Boolean graphicalNotationDefined;
	@JsonProperty("suspended")
	public Boolean suspended;
	@JsonProperty("startFormDefined")
	public Boolean startFormDefined;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JsonProvider [id=");
		builder.append(id);
		builder.append(", url=");
		builder.append(url);
		builder.append(", key=");
		builder.append(key);
		builder.append(", name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}

}
