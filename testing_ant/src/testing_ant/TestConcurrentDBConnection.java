package testing_ant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class TestConcurrentDBConnection {

	private final static String CONNECTION_STRING = "jdbc:oracle:thin:@amogridxp09-scan.us.oracle.com:1529/ldap_bugap.us.oracle.com";
	private static ConcurrentHashMap<String, String> resultMap = new ConcurrentHashMap<>();
	private static Connection connection;

	public static void main(String[] args) throws Exception {

		System.out.println();

		//connection = getConnection();
		List<Callable<String>> inputList = new ArrayList<>();
		List<Future<String>> resultList = new ArrayList<>();

		final int jobSize = 10;
		for (int i = 0; i < jobSize; i++) {
			inputList.add(new TestConnThreads(i));
		}

		ExecutorService pool = Executors.newFixedThreadPool(8);
		for (int i = 0; i < jobSize; i++) {
			Future<String> submit = pool.submit(inputList.get(i));
			resultList.add(submit);
		}

		for (int i = 0; i < jobSize; i++) {
			resultList.get(i).get();
			System.out.println((jobSize - 1 - i) + " items remaining");
		}
		pool.shutdown();

		resultMap.entrySet().forEach(entry -> System.out.println(entry));
		System.out.println("Done");
		System.out.println("Done");
		//connection.close();
	}

	static class TestConnThreads implements Callable<String> {

		int id;

		TestConnThreads(int id) {
			this.id = id;
		}

		@Override
		public String call() throws Exception {
			extracted(id);
			return null;
		}

	}

	private static String extracted(int id) throws SQLException {
		try {

			Connection connection = getConnection();
			String name = Thread.currentThread().getName();
			System.out.println(name + "=>" + "Connected database successfully...");

			String query = "SELECT r.STATUS FROM RPTHEAD r WHERE r.RPTNO in (29160832) AND STATUS IN (11,26,89,13,25)";
			Statement createStatement = connection.createStatement();

			ResultSet executeQuery = createStatement.executeQuery(query);
			System.out.println(name + "=>" + "executed...");

			if (executeQuery.next()) {
				System.out.println(name +", Job:"+ id + " =>" + executeQuery.getString(1));
				String value = resultMap.getOrDefault(name, "");
				resultMap.put(name, value + "," + id);
			}

			TimeUnit.MINUTES.sleep(3);
			executeQuery.close();
			createStatement.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private static Connection getConnection()  {
		Connection connection = null;
		try {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(CONNECTION_STRING, "SARRATHI", "Geekers32");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
}
