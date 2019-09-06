package ru.scoltech.measurement.config;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.stereotype.Component;
import reactor.netty.http.server.HttpServer;

@Component
public class EventLoopNettyCustomizer implements NettyServerCustomizer {

    @Value("${nio.eventloop.group.thread.count:10}")
    private int numberOfThreads;

    @Value("${server.port:8082}")
    private int port;

    @Override
    public HttpServer apply(HttpServer httpServer) {
        EventLoopGroup eventExecutors = new NioEventLoopGroup(numberOfThreads);
        return httpServer.tcpConfiguration(tcpServer ->
                tcpServer.bootstrap(serverBootstrap ->
                        serverBootstrap
                                .group(eventExecutors)
                                .channel(NioServerSocketChannel.class)))
                .port(port);
    }
}
