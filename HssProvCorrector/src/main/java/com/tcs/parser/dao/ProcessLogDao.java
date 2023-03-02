package com.tcs.parser.dao;

import com.tcs.parser.dto.processLog;
import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.EmConstants;
import com.tcs.service.dbconnectionmanager.EmDBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import java.util.List;

public class ProcessLogDao {
	private static Logger LOGGER = LogManager.getLogger(ProcessLogDao.class);

	public List<processLog> fetchSubProcessData() {

		Session session = null;
		SessionFactory sessionFactory;
		// This will contain failures of create or set vhSS. So, all the eventIds which
		// were failed/succeeded on em2 with failure in subprocess
		// are retrieved.
		try {
			sessionFactory = EmDBConnection.getSessionFactory();
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(processLog.class);
			SimpleExpression userFailure = Restrictions.eq("processStatus", EmConstants.SUB_PROCESS_FAILED.str());
			String methodArr[] = new String[]{EmConstants.CREATE_HSS_SUBSCRIBER.str(),
					EmConstants.SET_VHSS_SUBSCRIBER.str(), EmConstants.CREATE_DNS.str(), EmConstants.DELETE_DNS.str()};
			Criterion methodFailure = Restrictions.in("methodName", methodArr);

			LogicalExpression userOrIOFailure = Restrictions.or(
					Restrictions.eq("processStatus", EmConstants.SUB_PROCESS_FAILED.str()),
					Restrictions.eq("processStatus", EmConstants.SUB_PROCESS_IO_FAILED.str()));
			String methodArr2[] = new String[]{EmConstants.GET_IMSI.str()};
			Criterion methodFailure2 = Restrictions.in("methodName", methodArr2);

			criteria.add(Restrictions.or(Restrictions.and(userFailure, methodFailure),
					Restrictions.and(userOrIOFailure, methodFailure2)));

			// criteria.add(Restrictions.not(Restrictions.like("description", "Ensure",
			// MatchMode.ANYWHERE)));
			criteria.add(Restrictions.between("timeResponseReceived", ConfigUtils.getYesterdayTimeStamp(),
					ConfigUtils.getCurrentTimestamp())); // propertyname needs to be rechecked
			List<processLog> subEventsList = criteria.list();
			return subEventsList;
		} catch (Exception e) {
			LOGGER.error(ConfigUtils.getStackTraceString(e));
		} finally {
			try {
				session.close();
			} catch (Exception e) {
				LOGGER.error(ConfigUtils.getStackTraceString(e));
			}
		}
		return null;
	}
}
