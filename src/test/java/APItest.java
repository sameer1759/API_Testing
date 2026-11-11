import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

public class APItest {

    @Test
    public void e2eApiTest()
    {
        //LOGIN
        HashMap<Object,Object> LoginPayload = new HashMap<>();
        LoginPayload.put("email","your_email");
        LoginPayload.put("password","your_passworldgit status");

        Playwright playwright= Playwright.create();
        APIRequestContext apirequest = playwright.request().newContext();
        APIResponse loginResponse = apirequest.post("https://api.eventhub.rahulshettyacademy.com/api/auth/login",
                RequestOptions.create().setData(LoginPayload));
        Assert.assertTrue(loginResponse.ok());
        System.out.println(loginResponse.text());

        // Extract the auth token from the login response
        String token = JsonPath.read(loginResponse.text(),"$.token");
        System.out.println("Login success "+token);

        //CREATE EVENT
        HashMap<Object,Object> createEventPayload = new HashMap<>();
        createEventPayload.put("title","Weeknd 7");
        createEventPayload.put("description","");
        createEventPayload.put("category","Concert");
        createEventPayload.put("venue","Blah Blah");
        createEventPayload.put("city","Pune");
        createEventPayload.put("eventDate","2026-12-17T14:30:00.000Z");
        createEventPayload.put("price", 99);
        createEventPayload.put("totalSeats",200);


        APIResponse createEventResponse = apirequest.post("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setHeader("authorization","Bearer "+ token)
                        .setData(createEventPayload));
        Assert.assertTrue(createEventResponse.ok(),"Event Created!");
        // Save the new event id, needed later for delete and verification
        int eventId = JsonPath.read(createEventResponse.text(),"$.data.id");
        System.out.println("Event Id: "+eventId);

        //GET EVENTS
        APIResponse getEvent = apirequest.get("https://api.eventhub.rahulshettyacademy.com/api/events/",
                RequestOptions.create().setHeader("authorization","Bearer "+ token)
                        .setQueryParam("page",1).setQueryParam("limit",12));
        Assert.assertTrue(getEvent.ok(),"Event retrieved!");
        // Collect ids of all events on page 1
        List<Integer> allEventId = JsonPath.read(getEvent.text(),"$.data[*].id");
        System.out.println(allEventId);

        //DELETE EVENT
        APIResponse deleteEvent= apirequest.delete("https://api.eventhub.rahulshettyacademy.com/api/events/"+eventId,
                RequestOptions.create().setHeader("authorization","Bearer "+ token));
        Assert.assertTrue(deleteEvent.ok(),"Event Deleted!");

        //VERIFY THE DELETED EVENT
        APIResponse verifyDeletion = apirequest.get("https://api.eventhub.rahulshettyacademy.com/api/events/",
                RequestOptions.create().setHeader("authorization","Bearer "+ token)
                        .setQueryParam("page",1).setQueryParam("limit",12));
        Assert.assertTrue(verifyDeletion.ok(),"--");
        List<String> afterDeletionEventId = JsonPath.read(verifyDeletion.text(),"$.data[*].id");
        System.out.println(afterDeletionEventId);
        // Deleted event id should not be in the list anymore
        Assert.assertFalse(afterDeletionEventId.contains(eventId));

    }
}