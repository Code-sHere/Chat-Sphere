package com.chatapp.demo.config;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import jakarta.websocket.server.ServerEndpointConfig;

public class SpringConfigurator
        extends ServerEndpointConfig.Configurator {

    @Override
    public <T> T getEndpointInstance(
            Class<T> endpointClass)
            throws InstantiationException {

        ApplicationContext context =
                ContextLoader
                        .getCurrentWebApplicationContext();

        AutowireCapableBeanFactory factory =
                context.getAutowireCapableBeanFactory();

        return factory.createBean(endpointClass);
    }
}