package test.com;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import main.com.ActivitiProcessDefinitionsResponse;
import main.com.ActivitiStartProcessInstanceResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.api.client.MuleClient;
import org.mule.api.context.notification.EndpointMessageNotificationListener;
import org.mule.context.notification.EndpointMessageNotification;
import org.mule.tck.junit4.FunctionalTestCase;

import sun.misc.BASE64Encoder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class MuleActivitiTestSuite extends FunctionalTestCase {
	public static final String ORIGINAL_MESSAGE = "The original message sent";
	public static final String FINAL_MESSAGE = ORIGINAL_MESSAGE+ " AND SOME";
	//test-connectors has in-memory connectors used for testing
	//mule-exceptions contain actual application flows
	protected String getConfigResources() {
		return "test-connectors.xml, mule-activiti.xml";
	}
	
	CountDownLatch muleActivitiGetLatch;
	CountDownLatch muleActivitiPuLatch;
	@Override
	protected void doSetUp() throws Exception {
		super.doSetUp();
		muleActivitiGetLatch = new CountDownLatch(1);
		muleActivitiPuLatch = new CountDownLatch(1);
		muleContext.registerListener(new EndpointMessageNotificationListener<EndpointMessageNotification>(){
			public void onNotification(EndpointMessageNotification notification) {
				if("end dispatch".equals(notification.getActionName())){
					muleActivitiGetLatch.countDown();
				}
			}
			
		}, "MuleActivitiConnectFlowGet");
		
		muleContext.registerListener(new EndpointMessageNotificationListener<EndpointMessageNotification>(){
			public void onNotification(EndpointMessageNotification notification) {
				if("end dispatch".equals(notification.getActionName())){
					muleActivitiPuLatch.countDown();
				}
			}
			
		}, "MuleActivitiConnectFlowPost");
	}
	
	
	@Test
	public void testMuleActivitiGet() throws Exception{
		MuleClient client = muleContext.getClient();
		//client.send("http://localhost:8080/activiti-rest/service/repository/process-definitions", "IGNORED" , null); //<co id="lis_02_product_test-2"/>
		client.dispatch("vm://mule.activiti.get.in", "default", null);
		//Thread.sleep(20000);
		assertTrue(muleActivitiGetLatch.await(getTestTimeoutSecs(), TimeUnit.SECONDS)); 
		MuleMessage message = client.request("jms://mule.activiti.get.out", 500 * getTimeoutSystemProperty());//time in milliseconds
		assertNotNull(message);
		System.out.println("the class" + message.getPayload().getClass());
		assertTrue(message.getPayload() instanceof ActivitiProcessDefinitionsResponse[]);//array of JsonProvider
		boolean fixSystemFailure = false; //Fix system failure
		boolean escalationExample = false;//Helpdesk process
		for(ActivitiProcessDefinitionsResponse p: (ActivitiProcessDefinitionsResponse[])message.getPayload()){
			//these two processes come with default activiti-rest application, so guaranteed to be there
			if("fixSystemFailure".equals(p.key))
				fixSystemFailure = true;
			if("escalationExample".equals(p.key))
				escalationExample = true;
		}
		assertTrue(fixSystemFailure);
		assertTrue(escalationExample);
	}
	
	@Test
	public void testMuleActivitiPost() throws Exception{
		MuleClient client = muleContext.getClient();
		//client.send("http://localhost:8080/activiti-rest/service/repository/process-definitions", "IGNORED" , null); //<co id="lis_02_product_test-2"/>
		client.dispatch("vm://mule.activiti.put.in", "default", null);
		assertTrue(muleActivitiPuLatch.await(getTestTimeoutSecs(), TimeUnit.SECONDS)); 
		MuleMessage message = client.request("jms://mule.activiti.put.out", 500 * getTimeoutSystemProperty());//time in milliseconds
		assertNotNull(message);
		assertTrue(message.getPayload() instanceof ActivitiStartProcessInstanceResponse);
		
		ActivitiStartProcessInstanceResponse payload = (ActivitiStartProcessInstanceResponse)message.getPayload();
		assertNotNull(payload.id);
	}
	
	@Test
	public void testActivitiProcessParticipants() throws Exception{
		/*ClientConfig clientConfig = new DefaultClientConfig();

        clientConfig.getFeatures().put( JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        Client client = Client.create(clientConfig);
		//Client client = Client.create();
		 final HTTPBasicAuthFilter authFilter = new HTTPBasicAuthFilter("kermit", "kermit");
         client.addFilter(authFilter);
         client.addFilter(new LoggingFilter());*/
		
		String url = "http://localhost:8080/activiti-rest/service/runtime/process-instances";
        String name = "kermit";
        String password = "kermit";
        String authString = name + ":" + password;
        String authStringEnc = new BASE64Encoder().encode(authString.getBytes());
        System.out.println("Base64 encoded auth string: " + authStringEnc);
        Client restClient = Client.create();
        WebResource webResource = restClient.resource(url);
         
		//WebResource webResource = client.resource(url);

		/*ClientResponse response = webResource.accept("application/json")
                   .get(ClientResponse.class);*/
		ClientResponse response = webResource.type("application/json")
				.accept("application/json")
				.header("Authorization", "Basic " + authStringEnc)
                .post(ClientResponse.class, "{\"processDefinitionKey\":\"escalationExample\"}");

		if (response.getStatus() != 201) {
		   throw new RuntimeException("Failed : HTTP error code : "
			+ response.getStatus());
		}

		String output = response.getEntity(String.class);
		
		ObjectMapper objectMapper = new ObjectMapper(); 
		Map map = objectMapper.readValue(output, HashMap.class);
		
		System.out.println("Output from Server .... \n");
		System.out.println(output);
		System.out.println("my map " + map);
		
		assertTrue(((String)map.get("processDefinitionId")).contains("escalationExample"));

	}
	
	
}


