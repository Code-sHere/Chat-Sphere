package com.chatapp.demo.config;

import jakarta.websocket.server.ServerEndpointConfig;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringConfigurator
                extends ServerEndpointConfig.Configurator {

        private static ApplicationContext context;

        public static void setApplicationContext(
                        ApplicationContext applicationContext) {

                context = applicationContext;

        }

        @Override
        public <T> T getEndpointInstance(
                        Class<T> endpointClass)
                        throws InstantiationException {

                return context
                                .getAutowireCapableBeanFactory()
                                .createBean(endpointClass);

        }

}