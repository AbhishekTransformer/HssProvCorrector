package com.tcs.parser.utils;

import com.tcs.parser.dto.events;
import com.tcs.parser.dto.processLog;
import com.tcs.service.dbconnectionmanager.EmDBConnection;
import okhttp3.OkHttpClient;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import javax.net.ssl.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DevHelper {
	
	public List<events> getFailureList(String eventId,String userId){
		List<Integer> subEventsFailIdsList = parseSubProcesses(eventId,userId);
		List<events> failEventList= parseProcesses(subEventsFailIdsList);
		return failEventList;
	}
	
	private List<Integer> parseSubProcesses(String eventId,String userId) {
		DevHelper helper = new DevHelper();
		List<processLog> subEventsFailureList =helper.fetchSubProcessData(eventId,userId);	
		List<Integer> subEventsFailIdsList=new ArrayList<Integer>();
		/*To remove duplicate eventIds*/
		HashMap<Integer,processLog> failEventMap=new HashMap<Integer,processLog>();
		for(processLog event:subEventsFailureList) {
			if(!failEventMap.containsKey(event.getEventId())){
				failEventMap.put(event.getEventId(), event);
				subEventsFailIdsList.add(event.getEventId()); //update these both list after fetching from events table
			}//can try directly to fetch only eventIds from the above query, instead of all the data, if failure reasons are not required
		}
		subEventsFailureList=new ArrayList<processLog>(failEventMap.values());  // can use failure list for listing failure reasons in future 
		return subEventsFailIdsList;
	}

	private List<events> parseProcesses(List<Integer> subEventsFailIdsList) {
		System.out.println("11111111111 :: " + subEventsFailIdsList.size());
		DevHelper helper = new DevHelper();
		List<events> failEventList = new ArrayList<events>();
		if(null!=subEventsFailIdsList && subEventsFailIdsList.size()!=0){
			System.out.println("22222222222222222222222");
			failEventList = helper.getEventDetails(subEventsFailIdsList,ConfigUtils.getYesterdayTimeStamp()); // THis ia a fail list in 24 hours
			List<Integer> failIdsList=new ArrayList<Integer>();
			System.out.println("33333333333333333 :: "+failEventList.size());
			if(null!= failEventList && failEventList.size()!=0) {
				for(events evt: failEventList){
					failIdsList.add(evt.getEventId());
				}
			}			
		}
		return failEventList;
		//indexData.put("Total Service requests",new Integer(eventsList.size())); Not finding total requests as they will be too many
	}
	
	public List<processLog> fetchSubProcessData(String eventId,String userId) {

		Session session = null;
		SessionFactory sessionFactory;
		// This will contain failures of create or set vhSS. So, all the eventIds which
		// were failed/succeeded on em2 with failure in subprocess
		// are retrieved.
		try {
			sessionFactory = EmDBConnection.getSessionFactory();
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(processLog.class);
			if(eventId!=null && !eventId.equals(""))
				criteria.add(Restrictions.eq("eventId", Integer.parseInt(eventId)));
			//if(userId!=null && !userId.equals(""))
			//	criteria.add(Restrictions.eq("docUserId", userId));
			
			//criteria.add(Restrictions.eq("processStatus", EmConstants.SUB_PROCESS_FAILED.str()));
			//String methodArr[] = new String[] { EmConstants.CREATE_HSS_SUBSCRIBER.str(),
			//		EmConstants.SET_VHSS_SUBSCRIBER.str() };
			//criteria.add(Restrictions.in("methodName", methodArr));
			// criteria.add(Restrictions.not(Restrictions.like("description", "Ensure",
			// MatchMode.ANYWHERE)));
			//criteria.add(Restrictions.between("timeResponseReceived", ConfigUtils.getYesterdayTimeStamp(),
			//		ConfigUtils.getCurrentTimestamp())); // propertyname needs to be rechecked
			List<processLog> subEventsList = criteria.list();
			System.out.println("size : " + subEventsList.size());
			return subEventsList;
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				session.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public List<events> getEventDetails (List<Integer> eventIdList,Long startTime){
		Session session = null;
		SessionFactory sessionFactory;		
		try {
			sessionFactory = EmDBConnection.getSessionFactory();
			session = sessionFactory.openSession();			
			Criteria crit = getFailEventList(eventIdList, startTime, session);
			List<events> eventsList =crit.list();			
			return eventsList;	
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try{
				session.close();
			}
			catch(Exception e){
			System.out.println("@@@@@$$$$$$"+e.getMessage());
			}
		}
	return null;
	
	}
	
	public Criteria getFailEventList(List<Integer> failSubProcessIdList, Long startTime, Session session) {
		Criteria crit = session.createCriteria(events.class);
		//crit.add(Restrictions.like("asQueueName",EmConstants.SCALE.str(), MatchMode.ANYWHERE));
		//crit.add(Restrictions.in("docType",requestArr));
		crit.add(Restrictions.in("eventId", failSubProcessIdList));
		crit.add(Restrictions.between("timeEndProcess", startTime , ConfigUtils.getCurrentTimestamp())); // propertyname needs to be rechecked 			
		return crit;
	}
	
	public static Client ignoreSSLClient() {
		// Created only for testing purpose, DO NOT USE IN PRODUCTION !!
		// Create a trust manager that does not validate certificate chains
				TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
				    public X509Certificate[] getAcceptedIssuers(){return null;}
				    public void checkClientTrusted(X509Certificate[] certs, String authType){}
				    public void checkServerTrusted(X509Certificate[] certs, String authType){}
				}};

				// Install the all-trusting trust manager
				SSLContext sc = null;
				try {
				    sc = SSLContext.getInstance("TLS");
				    sc.init(null, trustAllCerts, new SecureRandom());
				    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				} catch (Exception e) {
				    System.out.println("Exception occured : "+e);;
				}
				return ClientBuilder.newBuilder().sslContext(sc).hostnameVerifier((s1,s2)->true).build();
	}
	
	public static OkHttpClient getUnsafeOkHttpClient() {
		try {
			// Create a trust manager that does not validate certificate chains
			final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws CertificateException {
					// Overriding default SSL trusted certificate store
				}

				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws CertificateException {
					// Overriding default SSL trusted certificate store
				}

				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			} };

			// Install the all-trusting trust manager
			final SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			// Create an ssl socket factory with our all-trusting manager
			final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

			return new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
					.hostnameVerifier(new HostnameVerifier() {
						@Override
						public boolean verify(String hostname, SSLSession session) {
							return true;
						}
					}).build();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	}

}
