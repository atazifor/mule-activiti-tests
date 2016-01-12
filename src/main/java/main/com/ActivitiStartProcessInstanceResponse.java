package main.com;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class ActivitiStartProcessInstanceResponse implements Serializable{

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ActivitiStartProcessInstanceResponse [id=");
		builder.append(id);
		builder.append(", url=");
		builder.append(url);
		builder.append(", processDefinitionId=");
		builder.append(processDefinitionId);
		builder.append(", completed=");
		builder.append(completed);
		builder.append("]");
		return builder.toString();
	}

	@JsonProperty("id")
	public String id;
	@JsonProperty("url")
	public String url;
	@JsonProperty("businessKey")
	public Object businessKey;
	@JsonProperty("suspended")
	public Boolean suspended;
	@JsonProperty("ended")
	public Boolean ended;
	@JsonProperty("processDefinitionId")
	public String processDefinitionId;
	@JsonProperty("processDefinitionUrl")
	public String processDefinitionUrl;
	@JsonProperty("activityId")
	public Object activityId;
	@JsonProperty("variables")
	public List<Object> variables = new ArrayList<Object>();
	@JsonProperty("tenantId")
	public String tenantId;
	@JsonProperty("completed")
	public Boolean completed;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}