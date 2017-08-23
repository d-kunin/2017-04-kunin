package ru.otus.kunin.app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import ru.otus.messageSystem.MessageSystem;
import ru.otus.messageSystem.MessageSystemContext;

public class MessagingServiceRunner {

  public static void main(String[] args) throws Exception {
    final Server server = new Server(8090);

    final ResourceHandler resourceHandler = new ResourceHandler();
    resourceHandler.setDirectoriesListed(true);
    resourceHandler.setBaseResource(Resource.newClassPathResource("./static/"));

    final MessageSystem messageSystem = new MessageSystem();
    final AddressableCache addressableCache = new AddressableCache();
    final MessageSystemContext messageSystemContext = MessageSystemContext.builder()
        .messageSystem(messageSystem)
        .cacheAddress(addressableCache.getAddress())
        .build();
    messageSystem.addAddressee(addressableCache);

    final ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
    final ServletHolder servletHolder = new ServletHolder(new WebsocketConnectorServlet(messageSystemContext));
    servletContextHandler.addServlet(servletHolder, "/cache/websocket");
    server.setHandler(new HandlerList(resourceHandler, servletContextHandler));

    messageSystem.start();
    server.start();
    server.join();
  }

}
