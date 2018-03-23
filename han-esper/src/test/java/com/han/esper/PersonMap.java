package com.han.esper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

public class PersonMap  
{  
    public static void main(String[] args)  
    {  
        EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();  
        EPAdministrator admin = epService.getEPAdministrator();  
          
        // Person¶¨Òå  
        Map<String,Object> person = new HashMap<String,Object>();  
        person.put("name", String.class);  
        person.put("age", int.class);  
        person.put("children", List.class);  
        person.put("phones", Map.class);  
          
        // ×¢²áPersonµ½Esper  
        admin.getConfiguration().addEventType("Person", person);  
    }  
} 