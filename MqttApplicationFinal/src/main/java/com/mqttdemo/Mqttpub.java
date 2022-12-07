package com.mqttdemo;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
@Configuration
public class Mqttpub {
    public DefaultMqttPahoClientFactory factory() {
    	
    	String username="iotdevices";
    	String password="Weldercare@321";
    	DefaultMqttPahoClientFactory fact = new DefaultMqttPahoClientFactory();
    	MqttConnectOptions options = new MqttConnectOptions();
    	options.setServerURIs(new String[] {"tcp://65.108.222.73"});
    	options.setCleanSession(true);
    	options.setUserName(username);
    	options.setPassword(password.toCharArray());
    	fact.setConnectionOptions(options);
    	return fact;
    }
    
    @Bean
    public MessageChannel mqttInputChannel() {
    	return new DirectChannel();
    }
    
    @Bean
    public MessageProducer inbound() {
    	MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("serverIn", factory(), "#");
    	adapter.setConverter(new DefaultPahoMessageConverter());
    	adapter.setQos(0);
    	adapter.setOutputChannel(mqttInputChannel());
    	return adapter; 
    }
    
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handle() {
    	return new MessageHandler() {
			
			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				// TODO Auto-generated method stub
				//String topic  = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();				
				System.out.println(message.getPayload());
			}
		};
    }
    
    @Bean
    public MessageChannel mqttOutputChannel() {
    	return new DirectChannel();
    }
    
    @Bean
    public MessageHandler Outbound() {
    	MqttPahoMessageHandler handler = new MqttPahoMessageHandler ("serverOut", factory());
    	handler.setAsync(true);
    	handler.setDefaultTopic("#");
    	return handler;
    	
    }
}
            