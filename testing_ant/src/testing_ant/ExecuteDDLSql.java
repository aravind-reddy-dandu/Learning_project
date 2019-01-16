package testing_ant;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ExecuteDDLSql {

	public static void main(String[] args) throws IOException {
		ArrayList<SchemaDetail> schemaDetailArray = getSchemaDetailArray();

		String filename = "D:\\installer\\12.4_RETRO_FROM_12.3\\12.5_core_support_UI\\source\\FCUBS_14.0.0.0.2\\MAIN\\TRG\\cytr_rates_history_upd.trg";
//		List<String> readAllLines = Files.readAllLines(Paths.get(filename));

		for (SchemaDetail schemaDetail : schemaDetailArray) {
//			for (String string : readAllLines) {
				compile(filename, schemaDetail);
//			}
		}
	}

	public static ArrayList<SchemaDetail> getSchemaDetailArray() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<SchemaDetail> schemadetailArray = new ArrayList<>();
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@" + "10.184.159.172" + ":" + "1521" + "/" + "FCTOOLSNEW", "DDLJ",
					"DDLJAdmin123");
			ps = conn.prepareStatement(
					"SELECT * FROM DLTB_SCHEMA_DETAILS WHERE SCHEMA_TYPE = 'DDL'  AND SERVICE_NAME  IN ('FCTOOLS','FCTOOLSNEW') ORDER BY SCHEMA_NAME");

			rs = ps.executeQuery();

			while (rs.next()) {
				SchemaDetail schemaDetail = new SchemaDetail();

				schemaDetail.setDatabase(rs.getString("SERVICE_NAME"));
				schemaDetail.setIpAddress(rs.getString("IP_ADDRESS"));
				schemaDetail.setPort(rs.getString("PORT"));
				schemaDetail.setSchemaName(rs.getString("SCHEMA_NAME"));
				schemaDetail.setPassword(rs.getString("PASSWORD"));

				schemadetailArray.add(schemaDetail);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResource(ps);
			closeResource(rs);
			closeResource(conn);
		}

		return schemadetailArray;
	}

	public static void closeResource(AutoCloseable ps) {
		try {
			if (ps != null)
				ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean compile(String fileName, SchemaDetail schemaDetail) {

		String compileResult = "";
		if (fileName != null) {

			String cmd = "cmd /c sqlplus.exe " + schemaDetail.getSchemaName() + "/" + schemaDetail.getPassword() + "@"
					+ schemaDetail.getDatabase() + " @" + fileName;
			new ExecCommand(cmd, new File("D:\\app12cadmin\\client\\dandredd\\product\\12.1.0\\client_1\\bin\\"),
					new File("D:\\dump\\out.txt"), new File("D:\\dump\\err.txt")).execute();

		}
		return "SUCCESS".equals(compileResult);
	}

	public static String hostExecute(String cmd) {
		String status = "SUCCESS";
		try {
			Process proc = Runtime.getRuntime().exec(cmd);
			BufferedReader buffRead = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line;
			while ((line = buffRead.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		return status;
	}
}
