package com.tcs.parser.main;

import com.tcs.fixer.http.CorsFilter;
import com.tcs.parser.controller.EmController;
import com.tcs.parser.controller.HSSProvControllerImpl;
import com.tcs.parser.utils.ConfigUtils;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.naming.NamingException;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class EmResources {
	private static Logger LISTENER_LOGGER = LogManager.getLogger(EmResources.class);

	private int serverPort;

	public static void main(String args[]) throws NamingException {
		ConfigUtils.loadPropertiesFile();
		//startHssProvCorrector();
		scheduleHssProvCorrector();

		int serverPort = 8080;
		if (args.length >= 1) {
			try {
				serverPort = Integer.parseInt(args[0]);
				LISTENER_LOGGER.info("------Project Running on Port : --------" + serverPort);
			} catch (NumberFormatException e) {
				LISTENER_LOGGER.error("-----exception in opening port-------" + e.getMessage());
			}
		}
		new EmResources(serverPort);
	}

	private static void scheduleHssProvCorrector() {

		final Timer timer = new Timer();
		final TimerTask task = new TimerTask() {
			@Override
			public void run() {
				try {
					LISTENER_LOGGER.info("---------------------Schedule Hss Corrector Started--------------------");
					System.out.println("Started ");
					startHssProvCorrector();

					LISTENER_LOGGER.info("---------------------Schedule Hss Corrector Finished--------------------");
				} catch (Exception exception) {
					LISTENER_LOGGER.error(ConfigUtils.getStackTraceString(exception));
				}
			}
		};


//		LocalDateTime nextDay8am = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(8, 0));
//		Date nextDay8amAsDate = Date.from(nextDay8am.atZone(ZoneId.systemDefault()).toInstant());
//
//		LocalDateTime nextDay10am = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(10, 0));
//		Date nextDay10amAsDate = Date.from(nextDay10am.atZone(ZoneId.systemDefault()).toInstant());
//
//		LocalDateTime nextDay1pm = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(13, 0));
//		Date nextDay1pmAsDate = Date.from(nextDay1pm.atZone(ZoneId.systemDefault()).toInstant());


		timer.schedule(task, 1000, /*ConfigUtils.schedulingHour*/24*60*60*1000); //uncomment scheduling hour, only after changing code for query time range from yesterdayTimestamp to respective time
//		timer.schedule(task, 80*1000, /*ConfigUtils.schedulingHour*/24*60*60*1000);
		//timer.schedule(task, 1000, /*ConfigUtils.schedulingHour*/1*60*60*1000); //uncomment scheduling hour, only after changing code for query time range from yesterdayTimestamp to respective time
		//timer.schedule(task, 1000, 5*60*1000);
	}

	protected static void startHssProvCorrector() {
		EmController hssProvController = new HSSProvControllerImpl();
		hssProvController.process();
	}

	public static void scheduleStop() {
		System.out.println("Ahjsd");
	}

	public EmResources(int serverPort) {
		this.serverPort = serverPort;
		LISTENER_LOGGER.info("Running on port number : " + serverPort);
		Server server = configureServer();
		LISTENER_LOGGER.info("------Configure Server successful--------");
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			LISTENER_LOGGER.error("Server Intiialization error " + ConfigUtils.getStackTraceString(e));
		}
	}

	private Server configureServer() {
		LISTENER_LOGGER.info("------Server Initialization loaded successfully--------");
		ResourceConfig resourceConfig = new ResourceConfig();
		resourceConfig.packages(FixApi.class.getPackage().getName());
		resourceConfig.register(CorsFilter.class);
		resourceConfig.register(JacksonFeature.class);
		resourceConfig.registerClasses(OpenApiResource.class, AcceptHeaderOpenApiResource.class);

		ServletContainer servletContainer = new ServletContainer(resourceConfig);
		LISTENER_LOGGER.info("------Sevlet Container loaded successfully--------");
		ServletHolder sh = new ServletHolder(servletContainer);
		HandlerCollection handlers = new HandlerCollection();
		Server server = new Server(serverPort);

		//Static-files handler
		ResourceHandler staticResourceHandler = new ResourceHandler();
//		System.out.println(this.getClass().getClassLoader().getResource(".").toExternalForm());
		staticResourceHandler.setResourceBase("./static");
		System.out.println(staticResourceHandler.getBaseResource());
		staticResourceHandler.setWelcomeFiles(new String[]{"index.html"});

		ContextHandler staticContextHandler = new ContextHandler("/swagger");
		staticContextHandler.setHandler(staticResourceHandler);

		//handler1
		handlers.addHandler(staticContextHandler);

		//handler2
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		context.addServlet(sh, "/*");
		handlers.addHandler(context);

		//handler3
		RequestLogHandler requestLogHandler = new RequestLogHandler();
		//NCSARequestLog requestLog = new NCSARequestLog("../logs/jetty/jetty-yyyy_mm_dd.request.log");
		LISTENER_LOGGER.info("------NCSARequestLog class generated successfully--------");
		//requestLog.setRetainDays(10);
		//requestLog.setAppend(true);
		//requestLog.setExtended(false);
		//requestLog.setLogTimeZone(TimeZone.getDefault().getID());
		System.out.println(TimeZone.getDefault().getID());
		//requestLogHandler.setRequestLog(requestLog);

		final ServerConnector connector = new ServerConnector(server);
		connector.setIdleTimeout(60000);
		server.addConnector(connector);

		handlers.addHandler(requestLogHandler);

		server.setHandler(handlers);
		return server;
	}
}







