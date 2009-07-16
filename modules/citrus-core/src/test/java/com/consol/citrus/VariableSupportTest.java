package com.consol.citrus;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.consol.citrus.actions.ReceiveMessageBean;
import com.consol.citrus.message.XMLMessage;
import com.consol.citrus.service.Service;
import com.consol.citrus.validation.XMLMessageValidator;

public class VariableSupportTest extends AbstractBaseTest {
    @Autowired
    XMLMessageValidator validator;
    
    Service service = EasyMock.createMock(Service.class);
    
    ReceiveMessageBean receiveMessageBean;
    
    @Override
    @BeforeMethod
    public void setup() {
        super.setup();
        
        receiveMessageBean = new ReceiveMessageBean();
        receiveMessageBean.setService(service);
        receiveMessageBean.setValidator(validator);
    }
    
    @Test
    public void testValidateMessageElementsVariablesSupport() {
        reset(service);
        
        XMLMessage message = new XMLMessage();
        
        message.setMessagePayload("<root>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value' >"
                            + "<sub-elementA attribute='A'>text-value</sub-elementA>"
                            + "<sub-elementB attribute='B'>text-value</sub-elementB>"
                            + "<sub-elementC attribute='C'>text-value</sub-elementC>"
                        + "</element>" 
                        + "</root>");
        
        expect(service.receiveMessage()).andReturn(message);
        replay(service);
        
        context.getVariables().put("variable", "text-value");
        
        Map<String, String> validateMessageElements = new HashMap<String, String>();
        validateMessageElements.put("//root/element/sub-elementA", "${variable}");
        validateMessageElements.put("//sub-elementB", "${variable}");
        
        receiveMessageBean.setValidateMessageElements(validateMessageElements);
        
        receiveMessageBean.execute(context);
    }
    
    @Test
    public void testValidateMessageElementsFunctionSupport() {
        reset(service);
        
        XMLMessage message = new XMLMessage();
        
        message.setMessagePayload("<root>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value' >"
                            + "<sub-elementA attribute='A'>text-value</sub-elementA>"
                            + "<sub-elementB attribute='B'>text-value</sub-elementB>"
                            + "<sub-elementC attribute='C'>text-value</sub-elementC>"
                        + "</element>" 
                        + "</root>");
        
        expect(service.receiveMessage()).andReturn(message);
        replay(service);
        
        context.getVariables().put("variable", "text-value");
        context.getVariables().put("text", "text");
        
        Map<String, String> validateMessageElements = new HashMap<String, String>();
        validateMessageElements.put("//root/element/sub-elementA", "citrus:concat('text', '-', 'value')");
        validateMessageElements.put("//sub-elementB", "citrus:concat(${text}, '-', 'value')");
        
        receiveMessageBean.setValidateMessageElements(validateMessageElements);
        
        receiveMessageBean.execute(context);
    }
    
    @Test
    public void testValidateMessageElementsVariableSupportInExpression() {
        reset(service);
        
        XMLMessage message = new XMLMessage();
        
        message.setMessagePayload("<root>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value' >"
                            + "<sub-elementA attribute='A'>text-value</sub-elementA>"
                            + "<sub-elementB attribute='B'>text-value</sub-elementB>"
                            + "<sub-elementC attribute='C'>text-value</sub-elementC>"
                        + "</element>" 
                        + "</root>");
        
        expect(service.receiveMessage()).andReturn(message);
        replay(service);
        
        context.getVariables().put("expression", "//root/element/sub-elementA");
        
        Map<String, String> validateMessageElements = new HashMap<String, String>();
        validateMessageElements.put("${expression}", "text-value");
        
        receiveMessageBean.setValidateMessageElements(validateMessageElements);
        
        receiveMessageBean.execute(context);
    }
    
    @Test
    public void testValidateMessageElementsFunctionSupportInExpression() {
        reset(service);
        
        XMLMessage message = new XMLMessage();
        
        message.setMessagePayload("<root>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value' >"
                            + "<sub-elementA attribute='A'>text-value</sub-elementA>"
                            + "<sub-elementB attribute='B'>text-value</sub-elementB>"
                            + "<sub-elementC attribute='C'>text-value</sub-elementC>"
                        + "</element>" 
                        + "</root>");
        
        expect(service.receiveMessage()).andReturn(message);
        replay(service);
        
        context.getVariables().put("variable", "B");
        
        Map<String, String> validateMessageElements = new HashMap<String, String>();
        validateMessageElements.put("citrus:concat('//root/', 'element/sub-elementA')", "text-value");
        validateMessageElements.put("citrus:concat('//sub-element', ${variable})", "text-value");
        
        receiveMessageBean.setValidateMessageElements(validateMessageElements);
        
        receiveMessageBean.execute(context);
    }
    
    @Test
    public void testValidateHeaderValuesVariablesSupport() {
        reset(service);
        
        XMLMessage message = new XMLMessage();
        message.addHeaderElement("header-valueA", "A");
        message.addHeaderElement("header-valueB", "B");
        message.addHeaderElement("header-valueC", "C");
        
        message.setMessagePayload("<root>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value' >"
                            + "<sub-elementA attribute='A'>text-value</sub-elementA>"
                            + "<sub-elementB attribute='B'>text-value</sub-elementB>"
                            + "<sub-elementC attribute='C'>text-value</sub-elementC>"
                        + "</element>" 
                        + "</root>");
        
        expect(service.receiveMessage()).andReturn(message);
        replay(service);
        
        receiveMessageBean.setMessageData("<root>"
                + "<element attributeA='attribute-value' attributeB='attribute-value' >"
                    + "<sub-elementA attribute='A'>text-value</sub-elementA>"
                    + "<sub-elementB attribute='B'>text-value</sub-elementB>"
                    + "<sub-elementC attribute='C'>text-value</sub-elementC>"
                + "</element>" 
                + "</root>");
        
        context.getVariables().put("variableA", "A");
        context.getVariables().put("variableB", "B");
        context.getVariables().put("variableC", "C");
        
        HashMap<String, String> validateHeaderValues = new HashMap<String, String>();
        validateHeaderValues.put("header-valueA", "${variableA}");
        validateHeaderValues.put("header-valueB", "${variableB}");
        validateHeaderValues.put("header-valueC", "${variableC}");
        
        receiveMessageBean.setHeaderValues(validateHeaderValues);
        
        receiveMessageBean.execute(context);
    }
    
    @Test
    public void testValidateHeaderValuesFunctionSupport() {
        reset(service);
        
        XMLMessage message = new XMLMessage();
        message.addHeaderElement("header-valueA", "A");
        message.addHeaderElement("header-valueB", "B");
        message.addHeaderElement("header-valueC", "C");
        
        message.setMessagePayload("<root>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value' >"
                            + "<sub-elementA attribute='A'>text-value</sub-elementA>"
                            + "<sub-elementB attribute='B'>text-value</sub-elementB>"
                            + "<sub-elementC attribute='C'>text-value</sub-elementC>"
                        + "</element>" 
                        + "</root>");
        
        expect(service.receiveMessage()).andReturn(message);
        replay(service);
        
        receiveMessageBean.setMessageData("<root>"
                + "<element attributeA='attribute-value' attributeB='attribute-value' >"
                    + "<sub-elementA attribute='A'>text-value</sub-elementA>"
                    + "<sub-elementB attribute='B'>text-value</sub-elementB>"
                    + "<sub-elementC attribute='C'>text-value</sub-elementC>"
                + "</element>" 
                + "</root>");
        
        context.getVariables().put("variableC", "c");
        
        HashMap<String, String> validateHeaderValues = new HashMap<String, String>();
        validateHeaderValues.put("header-valueA", "citrus:upperCase('a')");
        validateHeaderValues.put("header-valueB", "citrus:upperCase('b')");
        validateHeaderValues.put("header-valueC", "citrus:upperCase(${variableC})");
        
        receiveMessageBean.setHeaderValues(validateHeaderValues);
        
        receiveMessageBean.execute(context);
    }
    
    @Test
    public void testHeaderNameVariablesSupport() {
        reset(service);
        
        XMLMessage message = new XMLMessage();
        message.addHeaderElement("header-valueA", "A");
        message.addHeaderElement("header-valueB", "B");
        message.addHeaderElement("header-valueC", "C");
        
        message.setMessagePayload("<root>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value' >"
                            + "<sub-elementA attribute='A'>text-value</sub-elementA>"
                            + "<sub-elementB attribute='B'>text-value</sub-elementB>"
                            + "<sub-elementC attribute='C'>text-value</sub-elementC>"
                        + "</element>" 
                        + "</root>");
        
        expect(service.receiveMessage()).andReturn(message);
        replay(service);
        
        receiveMessageBean.setMessageData("<root>"
                + "<element attributeA='attribute-value' attributeB='attribute-value' >"
                    + "<sub-elementA attribute='A'>text-value</sub-elementA>"
                    + "<sub-elementB attribute='B'>text-value</sub-elementB>"
                    + "<sub-elementC attribute='C'>text-value</sub-elementC>"
                + "</element>" 
                + "</root>");
        
        context.getVariables().put("variableA", "header-valueA");
        context.getVariables().put("variableB", "header-valueB");
        context.getVariables().put("variableC", "header-valueC");
        
        HashMap<String, String> validateHeaderValues = new HashMap<String, String>();
        validateHeaderValues.put("${variableA}", "A");
        validateHeaderValues.put("${variableB}", "B");
        validateHeaderValues.put("${variableC}", "C");
        
        receiveMessageBean.setHeaderValues(validateHeaderValues);
        
        receiveMessageBean.execute(context);
    }
    
    @Test
    public void testHeaderNameFunctionSupport() {
        reset(service);
        
        XMLMessage message = new XMLMessage();
        message.addHeaderElement("header-valueA", "A");
        message.addHeaderElement("header-valueB", "B");
        message.addHeaderElement("header-valueC", "C");
        
        message.setMessagePayload("<root>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value' >"
                            + "<sub-elementA attribute='A'>text-value</sub-elementA>"
                            + "<sub-elementB attribute='B'>text-value</sub-elementB>"
                            + "<sub-elementC attribute='C'>text-value</sub-elementC>"
                        + "</element>" 
                        + "</root>");
        
        expect(service.receiveMessage()).andReturn(message);
        replay(service);
        
        receiveMessageBean.setMessageData("<root>"
                + "<element attributeA='attribute-value' attributeB='attribute-value' >"
                    + "<sub-elementA attribute='A'>text-value</sub-elementA>"
                    + "<sub-elementB attribute='B'>text-value</sub-elementB>"
                    + "<sub-elementC attribute='C'>text-value</sub-elementC>"
                + "</element>" 
                + "</root>");
        
        HashMap<String, String> validateHeaderValues = new HashMap<String, String>();
        validateHeaderValues.put("citrus:concat('header', '-', 'valueA')", "A");
        validateHeaderValues.put("citrus:concat('header', '-', 'valueB')", "B");
        validateHeaderValues.put("citrus:concat('header', '-', 'valueC')", "C");
        
        receiveMessageBean.setHeaderValues(validateHeaderValues);
        
        receiveMessageBean.execute(context);
    }
    
    @Test
    public void testExtractMessageElementsVariablesSupport() {
        reset(service);
        
        XMLMessage message = new XMLMessage();
        
        message.setMessagePayload("<root>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value' >"
                            + "<sub-elementA attribute='A'>text-value</sub-elementA>"
                            + "<sub-elementB attribute='B'>text-value</sub-elementB>"
                            + "<sub-elementC attribute='C'>text-value</sub-elementC>"
                        + "</element>" 
                        + "</root>");
        
        expect(service.receiveMessage()).andReturn(message);
        replay(service);
        
        context.getVariables().put("variableA", "initial");
        context.getVariables().put("variableB", "initial");
        
        receiveMessageBean.setMessageData("<root>"
                + "<element attributeA='attribute-value' attributeB='attribute-value' >"
                    + "<sub-elementA attribute='A'>text-value</sub-elementA>"
                    + "<sub-elementB attribute='B'>text-value</sub-elementB>"
                    + "<sub-elementC attribute='C'>text-value</sub-elementC>"
                + "</element>" 
                + "</root>");
        
        HashMap<String, String> extractMessageElements = new HashMap<String, String>();
        extractMessageElements.put("//root/element/sub-elementA", "${variableA}");
        extractMessageElements.put("//root/element/sub-elementB", "${variableB}");
        
        receiveMessageBean.setExtractMessageElements(extractMessageElements);
        
        receiveMessageBean.execute(context);
        
        Assert.assertTrue(context.getVariables().containsKey("variableA"));
        Assert.assertEquals(context.getVariables().get("variableA"), "text-value");
        Assert.assertTrue(context.getVariables().containsKey("variableB"));
        Assert.assertEquals(context.getVariables().get("variableB"), "text-value");
    }
    
    @Test
    public void testExtractHeaderValuesVariablesSupport() {
        reset(service);
        
        XMLMessage message = new XMLMessage();
        message.addHeaderElement("header-valueA", "A");
        message.addHeaderElement("header-valueB", "B");
        message.addHeaderElement("header-valueC", "C");
        
        message.setMessagePayload("<root>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value' >"
                            + "<sub-elementA attribute='A'>text-value</sub-elementA>"
                            + "<sub-elementB attribute='B'>text-value</sub-elementB>"
                            + "<sub-elementC attribute='C'>text-value</sub-elementC>"
                        + "</element>" 
                        + "</root>");
        
        expect(service.receiveMessage()).andReturn(message);
        replay(service);
        
        context.getVariables().put("variableA", "initial");
        context.getVariables().put("variableB", "initial");
        
        receiveMessageBean.setMessageData("<root>"
                + "<element attributeA='attribute-value' attributeB='attribute-value' >"
                    + "<sub-elementA attribute='A'>text-value</sub-elementA>"
                    + "<sub-elementB attribute='B'>text-value</sub-elementB>"
                    + "<sub-elementC attribute='C'>text-value</sub-elementC>"
                + "</element>" 
                + "</root>");
        
        HashMap<String, String> extractHeaderValues = new HashMap<String, String>();
        extractHeaderValues.put("header-valueA", "${variableA}");
        extractHeaderValues.put("header-valueB", "${variableB}");
        
        receiveMessageBean.setExtractHeaderValues(extractHeaderValues);
        
        receiveMessageBean.execute(context);
        
        Assert.assertTrue(context.getVariables().containsKey("variableA"));
        Assert.assertEquals(context.getVariables().get("variableA"), "A");
        Assert.assertTrue(context.getVariables().containsKey("variableB"));
        Assert.assertEquals(context.getVariables().get("variableB"), "B");
    }
}
