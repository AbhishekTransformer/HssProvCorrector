package com.tcs.parser.dao;

import com.tcs.parser.dto.events;
import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.EmConstants;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class HSSEventsDao extends EventsDao{
private String requestArr[] =new String[]{EmConstants.USER_ADD_REQUEST.str(),
											EmConstants.USER_ALTERNATE_NUMBERS_MODIFY_REQUEST.str(),
											EmConstants.USER_MODIFY_REQUEST.str(),
											EmConstants.USERSCA_ADD_ENDPOINT_REQUEST.str(),
											EmConstants.USERSCA_DELETE_ENDPOINTLIST_REQUEST.str(),
											EmConstants.USER_AUTHENTICATION_MODIFY_REQUEST.str()};
	@Override
	public Criteria getOtherFailEvents(List<Integer> failSubProcessIdList,Long startTime, Session session) {
		//LATEST  :  select * from events where doctype ="UserAddRequest17sp4" and timeEndProcess between "1570541658000" and "1570628058000" and asQueueName like "%scale%" and eventStatus ="failed";
		Criteria crit = session.createCriteria(events.class);
		crit.add(Restrictions.like("asQueueName",EmConstants.SCALE.str(), MatchMode.ANYWHERE));  
		crit.add(Restrictions.in("docType",requestArr));
		crit.add(Restrictions.eq("eventStatus",EmConstants.PROCESS_FAILED.str()));
		crit.add(Restrictions.between("timeEndProcess", startTime , ConfigUtils.getCurrentTimestamp())); // propertyname needs to be rechecked 
		if(null!=failSubProcessIdList && failSubProcessIdList.size()!=0)
			crit.add(Restrictions.not(Restrictions.in("eventId", failSubProcessIdList)));
		return crit;
	}
	@Override
	public Criteria getFailEventList(List<Integer> failSubProcessIdList, Long startTime, Session session) {
		Criteria crit = session.createCriteria(events.class);
		crit.add(Restrictions.like("asQueueName",EmConstants.SCALE.str(), MatchMode.ANYWHERE));
		crit.add(Restrictions.in("docType",requestArr));
		crit.add(Restrictions.in("eventId", failSubProcessIdList));
		crit.add(Restrictions.between("timeEndProcess", startTime , ConfigUtils.getCurrentTimestamp())); // propertyname needs to be rechecked 			
		return crit;
	}
}
