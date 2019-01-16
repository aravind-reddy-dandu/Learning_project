package testing_ant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;


public class aaa {
	static	LinkedHashMap<String, HashSet<String>> bugObjMap = new LinkedHashMap<>();
	static	LinkedHashMap<String, HashSet<String>> newMap = new LinkedHashMap<>();
	static List<String> preSfrNoList = new ArrayList<>();
	
	public void getCorequisiteBugs(/*List<String> baseSfrlist, List<DltmProject> projectList*/) {
		/*for (String bugNo : baseSfrlist) {

			BugDao bugDao = new BugDao();
			ArrayList<String> objectlist = new ArrayList<>();
			for (DltmProject project : projectList) {
				objectlist.addAll(bugDao.getStaticObjectList(bugNo, "DLTB_STATIC_HISTORY", project));
				objectlist.addAll(bugDao.getStaticObjectList(bugNo, "DLTB_STATIC_MASTER", project));
			}
			bugObjMap.put(bugNo, new HashSet<>(objectlist));
			
		}*/
		prepareMap(newMap);
	}

	private static void prepareMap(LinkedHashMap<String, HashSet<String>> newMap2) 
	{
		Set<Entry<String, HashSet<String>>> entrySet = bugObjMap.entrySet();
		for (Entry<String, HashSet<String>> entry : entrySet) 
		{
			for (String file: entry.getValue()) 
			{
				HashSet<String> units = null;
				if (newMap.containsKey(file)) {
					units = newMap.get(file);
				} else {
					units = new HashSet<>();
				}
				units.add(entry.getKey());
				newMap.put(file,units);
			}
		}
		
	}
	private static void getResult(String file)
	{
		Set<Entry<String, HashSet<String>>> entrySetBugMap = bugObjMap.entrySet();
		Set<Entry<String, HashSet<String>>> entrySet = newMap.entrySet();
		HashSet<String> outputSet=null;
		List<HashSet<String>> list= new ArrayList<>();
		for (Entry<String, HashSet<String>> oldMapEntry : entrySetBugMap)
		{
			outputSet= new HashSet<>();
			for (Entry<String, HashSet<String>> newMapEntry : entrySet)
			{
				if (newMapEntry.getValue().contains(oldMapEntry.getKey()))
				{
					outputSet.addAll(newMapEntry.getValue());
					continue;
				}
			}
			list.add(outputSet);
		}
	}
	
	public static void getConsolidatedBugs(HashSet<HashSet<String>> bugSet){
		
		HashSet<String> set1 = new HashSet<>();
		set1.add("1");
		set1.add("2");
		set1.add("3");
		
		HashSet<String> set2 = new HashSet<>();		
		set2.add("4");
		set2.add("5");
		HashSet<String> set3 = new HashSet<>();		
		set3.add("6");
		set3.add("7");
		HashSet<String> set4 = new HashSet<>();		
		set4.add("3");
		set4.add("5");
		HashSet<String> set5 = new HashSet<>();		
		set5.add("7");
		set5.add("9");
		set5.add("10");
		HashSet<String> set6 = new HashSet<>();		
		set6.add("70");
		set6.add("80");
		set6.add("90");
		HashSet<String> set7 = new HashSet<>();		
		set7.add("70");
		set7.add("88");
		set7.add("99");
		set7.add("109");
		HashSet<String> set8 = new HashSet<>();		
		set8.add("a");
		set8.add("vb");
		set8.add("10");		
		bugSet.add(set1);
		bugSet.add(set2);
		bugSet.add(set3);
		bugSet.add(set4);
		bugSet.add(set5);
		bugSet.add(set6);
		bugSet.add(set7);
		bugSet.add(set8);
		System.out.println(bugSet);		
		for(int i =0; i< bugSet.size()-1;i++){
			HashSet<HashSet<String>> tempSet = tempMethod(bugSet);
			bugSet = tempSet;
		}
				
	}
	
	
	public static HashSet<HashSet<String>> tempMethod (HashSet<HashSet<String>> bugSet){
		HashSet<HashSet<String>> finalSet = new HashSet<>();
		for(HashSet<String> set : bugSet){
			HashSet<String> newSet = new HashSet<>();
			for(String bugNo : set){
				for(HashSet<String> interSet : bugSet){
					if(interSet.contains(bugNo)){
						newSet.addAll(interSet);
					}
				}
			}
			finalSet.add(newSet);					
		}
		System.out.println(finalSet);
		return finalSet;		
	}
	public static void main(String args[]){
		getConsolidatedBugs(new HashSet<>());
	}
	
	public static void mainTemp(String args[])
	{
		HashSet<String> hashSet1 = new HashSet<>();
		hashSet1.add("a");
		hashSet1.add("b");
		hashSet1.add("c");
		
		bugObjMap.put("1", hashSet1);
		HashSet<String> hashSet2 = new HashSet<>();
		hashSet2.add("e");
		hashSet2.add("z");
		bugObjMap.put("2", hashSet2);
		HashSet<String> hashSet3 = new HashSet<>();
		hashSet3.add("f");
		hashSet3.add("b");
		bugObjMap.put("3", hashSet3);
		HashSet<String> hashSet4 = new HashSet<>();
		hashSet4.add("e");
		hashSet4.add("k");
		hashSet4.add("c");
		bugObjMap.put("4", hashSet4);
		prepareMap(newMap);
		
		
		System.out.println(newMap);
		Set<Entry<String, HashSet<String>>> entrySet = bugObjMap.entrySet();
		for (Entry<String, HashSet<String>> entry : entrySet) 
		{
			for (String file : entry.getValue()) 
			{
				getResult(file);
			}
		}
//		int count=0;
		/*LinkedHashMap<String, HashSet<String>> newMap2 = new LinkedHashMap<>();
		for (HashSet<String> hashSet : list) 
		{
			newMap2.put(""+count++, hashSet);
		}
		System.out.println(list);
		list.clear();*/
		prepareMap(newMap);
		 Set<Entry<String, HashSet<String>>> entrySetBugMap = bugObjMap.entrySet();
		 entrySet = newMap.entrySet();
		 HashSet<HashSet<String>> list = new HashSet<>();
		for (Entry<String, HashSet<String>> oldMapEntry : entrySetBugMap)
		{
			HashSet<String> outputSet = new HashSet<>();
			for (Entry<String, HashSet<String>> newMapEntry : entrySet)
			{
				if (newMapEntry.getValue().contains(oldMapEntry.getKey()))
				{
					outputSet.addAll(newMapEntry.getValue());
					continue;
				}
			}
			
			list.add(outputSet);
		}
		System.out.println(list);
	}
}
