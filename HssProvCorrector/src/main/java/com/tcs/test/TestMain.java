package com.tcs.test;

import com.tcs.parser.dto.events;
import com.tcs.parser.utils.TimeComparator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

public class TestMain {

	public static void main1(String args[]) {

		System.out.println(" Local Date time : " + LocalDateTime.now().toString());
		System.out.println("Date : " + new Date(Long.parseLong("1568816697121")).toString());


		LocalDateTime triggerTime =
				LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong("1568816697121")),
						TimeZone.getDefault().toZoneId());

		System.out.println(triggerTime);

		List<events> testList = new ArrayList<events>();
		events ev1 = new events();
		ev1.setDocUserId("ABC");
		events ev2 = new events();
		ev2.setDocUserId("FGHG");
		events ev3 = new events();
		ev3.setDocUserId("ABC");
		events ev4 = new events();
		ev4.setDocUserId("AR");
		events ev5 = new events();
		ev5.setDocUserId("ABCD");
		testList.add(ev1);
		testList.add(ev2);
		testList.add(ev3);
		testList.add(ev4);
		testList.add(ev5);
		
		for(events ev:testList){
			System.out.println("Before: "+ev.getDocUserId());
		}
		
		HashMap<String,events> testMap=new HashMap<String,events>();
		for(events ev:testList) {
			if(!testMap.containsKey(ev.getDocUserId())){
				testMap.put(ev.getDocUserId(), ev);
			}
		}
		List<events> finalList=new ArrayList<events>(testMap.values());
		for(events ev:finalList){
			System.out.println("After : "+ev.getDocUserId());
		}
		

		
		/*SortedSet<events> set = new TreeSet<events>(new Comparator<events>(){

		    public int compare(events o1, events o2) {
				if(ev1.getDocUserId().equals(ev2.getDocUserId()))
					return 0;
				else 
					return 1;
		    }
		});

		set.addAll(testList);
		List<events> duplicateFreeList = new ArrayList<events>(set);
		for(events ev:duplicateFreeList){
			System.out.println(ev.getDocUserId());
		}*/
		List<events> testList2 = new ArrayList<events>();
		events ev11=new events();
		ev11.setTimeEndProcess(new Long(123456330));
		events ev12= new events();
		ev12.setTimeEndProcess(new Long(123456327));
		events ev13= new events();
		ev13.setTimeEndProcess(new Long(123456325));
		events ev14= new events();
		ev14.setTimeEndProcess(new Long(123456332));
		events ev15= new events();
		ev15.setTimeEndProcess(new Long(123456325));
		testList2.add(ev11);
		testList2.add(ev12);
		testList2.add(ev13);
		testList2.add(ev14);
		testList2.add(ev15);
		
		for(events ev:testList2){
			System.out.println("Before: "+ev.getTimeEndProcess());
		}
		
		Collections.sort(testList2,new TimeComparator());
		for(events ev:testList2){
			System.out.println("After : "+ev.getTimeEndProcess());
		}
	}
	
	
	
	public static void main(String[] args) {
		List<events> list = new ArrayList<events>();
		for(int i=0;i<5;i++) {
			events event = new events();
			event.setDocUserId(Integer.toString(i));
			list.add(event);
		}
		
		for(events ev:list) {
			if(ev.getDocUserId().equals("2"))
				ev.setAsClusterId("haha");
		}
		
		for(events ev:list) {
			System.out.println(ev.getAsClusterId());
		}
	}
}
