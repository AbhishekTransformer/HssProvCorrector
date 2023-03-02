package com.tcs.service.dbconnectionmanager;


import com.tcs.parser.utils.ConfigUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.File;

public class EmDBConnection {
    private static Logger LOGGER = LogManager.getLogger(EmDBConnection.class);
    public static SessionFactory sessionFactory;

    static {
        try {
            File f = new File(ConfigUtils.hibernateCfgXmlPath);
            Configuration cfg = new Configuration();
            cfg.addAnnotatedClass(com.tcs.parser.dto.events.class);
            cfg.addAnnotatedClass(com.tcs.parser.dto.processLog.class);
            cfg.configure(f);
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
            sessionFactory = cfg.buildSessionFactory(serviceRegistry);
            //sessionFactory =  conf.buildSessionFactory();
        } catch (Exception ex) {
            LOGGER.error(ConfigUtils.getStackTraceString(ex));
        }
    }

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            try {
                File f = new File(ConfigUtils.hibernateCfgXmlPath);
                sessionFactory = new Configuration().configure(f).buildSessionFactory();
            } catch (Exception e) {
                LOGGER.error(ConfigUtils.getStackTraceString(e));
            }
        }
        return sessionFactory;
    }

    public static synchronized void closeSessionFactory() {
        sessionFactory.close();
        sessionFactory = null;
//        LOGGER.error("Closing Session Factory");  
        LOGGER.info("Closing Session Factory");   
    }
}
