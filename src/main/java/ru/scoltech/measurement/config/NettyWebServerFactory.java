package ru.scoltech.measurement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyWebServerFactory {

    @Autowired
    private EventLoopNettyCustomizer eventLoopNettyCustomizer;

    @Bean
    public NettyReactiveWebServerFactory serverFactory() {
        NettyReactiveWebServerFactory factory = new NettyReactiveWebServerFactory();
        factory.addServerCustomizers(eventLoopNettyCustomizer);
        return factory;
    }
}
