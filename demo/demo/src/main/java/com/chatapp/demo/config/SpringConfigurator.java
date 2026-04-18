package com.chatapp.demo.config;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import jakarta.websocket.server.ServerEndpointConfig;

public class SpringConfigurator
        extends ServerEndpointConfig.Configurator {

    @Override
    public <T> T getEndpointInstance(
            Class<T> endpointClass)
            throws InstantiationException {

        return SpringBeanAutowiringSupport
                .processInjectionBasedOnCurrentContext(
                        endpointClass.newInstance()
                );
    }
}