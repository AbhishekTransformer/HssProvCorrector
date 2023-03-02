package com.tcs.parser.dao;

import com.tcs.parser.dto.events;
import com.tcs.parser.utils.ConfigUtils;
import com.tcs.service.dbconnectionmanager.EmDBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public abstract class EventsDao {
    private static Logger LOGGER = LogManager.getLogger(EventsDao.class);

    public List<events> fetchOtherProcessData(List<Integer> eventIdList, Long startTime) {
        Session session = null;
        SessionFactory sessionFactory;
        try {
            sessionFactory = EmDBConnection.getSessionFactory();
            session = sessionFactory.openSession();
            Criteria crit = getOtherFailEvents(eventIdList, startTime, session);
            List<events> eventsList = crit.list();
            return eventsList;
        } catch (Exception e) {
            LOGGER.error(ConfigUtils.getStackTraceString(e));
            System.out.println(e.getMessage());
        } finally {
            try {
                session.close();
            } catch (Exception e) {
                System.out.println("@@@@@$$$$$$" + e.getMessage());
            }
        }
        return null;
    }

    public abstract Criteria getOtherFailEvents(List<Integer> failSubProcessIdList, Long startTime, Session session);

    public abstract Criteria getFailEventList(List<Integer> failSubProcessIdList, Long startTime, Session session);

    public List<events> getEventDetails(List<Integer> eventIdList, Long startTime) {
        Session session = null;
        SessionFactory sessionFactory;
        try {
            sessionFactory = EmDBConnection.getSessionFactory();
            session = sessionFactory.openSession();
            Criteria crit = getFailEventList(eventIdList, startTime, session);
            List<events> eventsList = crit.list();
            return eventsList;
        } catch (Exception e) {
            LOGGER.error(ConfigUtils.getStackTraceString(e));
            System.out.println(e.getMessage());
        } finally {
            try {
                session.close();
            } catch (Exception e) {
                System.out.println("@@@@@$$$$$$" + e.getMessage());
            }
        }
        return null;

    }
}
