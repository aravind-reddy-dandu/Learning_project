package testing_ant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class testingnew {
	private static ThreadLocal<Boolean> isCheckRequired = new ThreadLocal<>();

	public static void maintemp(String[] args) {
		try {
			// String incName = "20180813_182606012_12_SMTB_MENU.INC";
			// String tempString = incName.substring(ordinalIndexOf(incName,
			// "_", 3)+1);
			// System.out.println(tempString.substring(0,tempString.indexOf(".")));
			// System.out.println(serviceName.substring(0,
			// serviceName.lastIndexOf(".")));

			File[] fileList = new File("Y:\\Scripts\\FCUBS_12.2.0.0.0_SUPPORT\\MAIN\\DATABASE\\HOST\\FILES\\INC")
					.listFiles();

			HashMap<String, List<File>> groupMap = new HashMap<>();

			for (File incFile : fileList) {
				String tempString = incFile.getName().substring(ordinalIndexOf(incFile.getName(), "_", 3) + 1);
				String tableName = tempString.substring(0, tempString.indexOf("."));
				List<File> sameTableList = groupMap.get(tableName);
				if (sameTableList == null) {
					sameTableList = new ArrayList<File>();
				}
				sameTableList.add(incFile);
				groupMap.put(tableName, sameTableList);
			}
			System.out.println(groupMap.size());
			// List<File> ertbList = groupMap.get("ERTB_MSGS");

			PrintWriter writer = new PrintWriter(new File("D:/dump/out.txt"));
			Set<Entry<String, List<File>>> entrySet = groupMap.entrySet();
			for (Entry<String, List<File>> entry : entrySet) {
				List<File> value = entry.getValue();

				writer.println("#####################");
				Collections.sort(value);
				for (File file : value) {

					writer.println(file.getAbsolutePath());
				}
				writer.flush();
			}

			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void mainTEMP(String[] args) {
		int noThreads = 10;

		int groupSize = 44;

		int threadSize = 0;
		if (!(groupSize / noThreads == 0)) {
			if (groupSize % noThreads == 0) {
				threadSize = (groupSize / noThreads);
			} else {
				threadSize = (groupSize / noThreads) + 1;
				if (groupSize % threadSize == 0) {
					noThreads = groupSize / threadSize;
				} else {
					noThreads = groupSize / threadSize + 1;
				}
			}
		} else {
			threadSize = 1;
			noThreads = groupSize;
		}

		System.out.println("threadsize= " + threadSize);
		System.out.println("nothreads= " + noThreads);

		try {
			System.out.println(decrypt("6uxpWHqovGfAbseHj3DJyw==".getBytes(), "oraclefinancialsolutions".getBytes()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(decrypt);
	}

	public static void main(String[] args) {
		StringBuffer sbf = new StringBuffer();
		sbf.append(new StringBuffer("SELECT decode(role_cd,                                 "
				+ "              'SA',                               	"
				+ "              'SuperAdmin',                       	"
				+ "              'A',                                	"
				+ "              'Admin',                            	"
				+ "              'N',                                	"
				+ "              'Developer',                           "
				+ "              'SD',                               	"
				+ "              'Documentation',     					"
				+ "              'SV',                                	"
				+ "              'Vercon',                           	"
				+ "              'R',                                	"
				+ "              'ReleaseAdmin' )ROLE,					"
				+ "				 PROJECT_ASSIGNED, STREAM_NAME  		"
				+ " FROM DLTM_USER_PROJ_ROLE_MAPPING WHERE emp_id=?		"));
		
		System.out.println(sbf.toString());
	}

	public static byte[] doAESCrypt(byte[] inputText, byte[] secretKey, int operation) throws Exception {
		MessageDigest mDigest = MessageDigest.getInstance("SHA-512");
		byte[] digestSeed = mDigest.digest(secretKey);
		byte[] hashKey = Arrays.copyOf(digestSeed, 16);
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		SecretKeySpec skspec = new SecretKeySpec(hashKey, "AES");
		cipher.init(operation, skspec);
		byte[] ret_array = cipher.doFinal(inputText);
		cipher = null;
		skspec = null;
		return ret_array;
	}

	public static byte[] encrypt(byte[] pwdToEncrypt, byte[] secretKey) throws Exception {
		return doAESCrypt(pwdToEncrypt, secretKey, Cipher.ENCRYPT_MODE);
	}

	public static byte[] decrypt(byte[] encryptedPwd, byte[] secretKey) throws Exception {
		return doAESCrypt(encryptedPwd, secretKey, Cipher.DECRYPT_MODE);
	}

	public static int ordinalIndexOf(String str, String substr, int n) {
		int pos = str.indexOf(substr);
		while (--n > 0 && pos != -1)
			pos = str.indexOf(substr, pos + 1);
		return pos;
	}

	public static void copyFolder(File src, File dest, String excludeList) {

		if (src.isDirectory()) {
			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdir();
			}
			// list all the directory contents
			String files[] = src.list();
			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// recursive copy
				copyFolder(srcFile, destFile, excludeList);
			}

		} else {
			try {
				if (!src.getName().equals(excludeList)) {
					Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
				}
			} catch (IOException e) {
			}

		}

	}

	public static void getModuleFromSXML(Path sxmlPath) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document sxmlDoc = documentBuilder.parse(sxmlPath.toFile());
		NodeList nList = sxmlDoc.getElementsByTagName("MODULE");
		Node nNode = nList.item(0);
		System.out.println(nNode.getTextContent().toString());
	}

	private static List<String> getRADXMLNodeList(File sxmlFile) {
		List<String> radXMLList = new ArrayList<>();
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document sxmlDoc = documentBuilder.parse(sxmlFile);
			NodeList nList = sxmlDoc.getElementsByTagName("RAD_KERNEL");
			Node nNode = nList.item(0);
			Element eElement = (Element) nNode;
			NodeList funcIdElements = eElement.getElementsByTagName("RAD_FUNCTION_ID");
			for (int i = 0; i < funcIdElements.getLength(); i++) {
				Element e = null;
				Node item = funcIdElements.item(i);
				e = (Element) item;
				radXMLList.add(e.getElementsByTagName("NAME").item(0).getTextContent());
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		return radXMLList;
	}

	public static void mainTT(String[] args) {
		// String preReqstaticOpMasterQuery = " SELECT distinct SFR_NO FROM
		// DLTB_STATIC_OPERATION_MASTER d "
		// + "WHERE (D.VERSION_NO, d.Object_Name) in (select
		// max(C.OPERATION_VERSION), A.OBJECT_NAME "
		// + " from dltb_static_master b, dltb_static_history c,
		// dltb_static_operation_master a "
		// + " where b.stream_name = c.stream_name AND b.project_name =
		// c.project_name and b.table_name = c.table_name "
		// + "and b.key_id = c.key_id and b.table_version_no =
		// c.table_version_no and b.key_version_no = c.key_version_no "
		// + "and a.stream_name = b.stream_name and a.project_name =
		// b.project_name and a.object_name = b.table_name "
		// + "and a.table_version_no = b.table_version_no and a.key_version_no =
		// b.key_version_no and a.version_no = b.operation_version "
		// + " AND a.PROJECT_NAME = ? and a.stream_name = ? and c.maker_id <>
		// 'SYSTEM' and trim(a.sfr_no) = ?"
		// + " GROUP by A.OBJECT_NAME, c.key_id) AND D.PROJECT_NAME = ? and
		// D.stream_name = ? order by sfr_no";
		// System.out.println(preReqstaticOpMasterQuery);

		// String temp ="'25303115','25199056','25473186'";
		//
		// temp = temp.substring(1,temp.lastIndexOf("'"));
		// System.out.println(temp);

		// Path relativize = Paths.get("Y:\\Scripts").relativize(new
		// File("Y:\\Scripts\\FCUBS_12.2.0.0.0_SUPPORT\\MAIN\\DATABASE\\HOST\\FILES\\DDL\\20170222_121353595_4_LCTB_CLAUSES.DDL").toPath());
		// String relativePath = relativize.toString();
		// System.out.println(relativePath.substring(0,relativePath.indexOf(File.separator)));
		// String[] readBugListFile =
		// readBugListFile("C:\\Users\\dandredd\\Desktop\\BugList.txt");
		// System.out.println(readBugListFile("C:\\Users\\dandredd\\Desktop\\BugList.txt"));

		// System.out.println(new StringBuffer().append("select f.sfr_no,
		// f.stream_name, f.project_name, m.folder_name, m.release, "
		// + " d.schema_name, d.password, d.database, d.ip_address, d.port from
		// dltm_sfr_details f "
		// + " join dltm_project_schema_mapping p on (p.project_name =
		// f.project_name and p.stream_name = f.stream_name) "
		// + " join dltb_schema_details d on (p.schema_name = d.schema_name and
		// p.database = d.database) "
		// + " join dltm_project_folder_mapping m on (p.project_name =
		// m.project_name and p.stream_name = m.stream_name) "
		// + " where p.schema_type = 'DDL' and f.sfr_no in ('").append("
		// ").append("')").toString());
		// String irFileName = "258956_APP";
		// irFileName = irFileName.substring(0, irFileName.indexOf("_"));
		// System.out.println(irFileName);

		// String readFile =
		// readFile("D:\\installer\\12.4_RETRO_FROM_12.3\\12.5_core_supp_merging\\INFRA\\FCJNeoWeb\\WebContent\\WEB-INF\\scheduler_standalone_web.xml");
		// System.out.println(readFile);

		// String destination = "D:/Destination/26658798";
		// System.out.println(destination.substring(destination.lastIndexOf("/")+1,
		// destination.length()));

		// copyFolder(new File("D:\\Destination\\28738064_APP"), new
		// File("D:\\Destination\\28728550_APP"));
		// String jobFileName = "ROFCEarRun.bat";
		// System.out.println(jobFileName.substring(0,jobFileName.indexOf(".")));
		// ArrayList<String> bugTypeArray = new ArrayList<>();
		// bugTypeArray.add("U");
		//// bugTypeArray.add("D");
		//
		// String bugType = "";
		// for(String s : bugTypeArray){
		// bugType = bugType+s+"~";
		// }
		// mergeAllLogFiles("D:\\installer\\12.4_RETRO_FROM_12.3\\12.5_core_supp_merging\\SOFT\\logs\\ROFC\\MAIN\\objects\\DBCompileutility_objects_20181015_04-10-28.028_PMODT121DEV.log");
		// mergeAllLogFiles("D:\\installer\\12.4_RETRO_FROM_12.3\\12.5_core_supp_merging\\SOFT\\logs\\ROFC\\MAIN\\objects\\DBCompileutility_objects_20181015_04-10-28.028_PMODT121DEV.log");

		// File file = new
		// File("Y:\\Scripts\\CFPM-ANALYSIS-SERVICES_SMS\\MAIN\\DATABASE\\HOST\\CONSOL\\INC");
		// try {
		// System.out.println(file.exists());
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// System.out.println(new StringBuffer("SELECT A.OBJECT_TYPE,
		// A.OBJECT_NAME, A.VERSION_NO, A.CODE_FIELD, TO_CHAR(A.MAKER_DT_STAMP,
		// 'RRRRMMDD_HH24MISS') "
		// + "MAKER_DATE, A.SUBPROJECT_PATH, A.CHECKIN_STATUS, A.MAKER_ID,
		// A.LATEST_ACTION, A.SFR_NO, B.BASE_LINK_OBJECT, A.REMARKS FROM"
		// + " DLTB_OPERATION_MASTER A, DLTB_OBJECT_MASTER B WHERE A.STREAM_NAME
		// = B.STREAM_NAME AND A.PROJECT_NAME = B.PROJECT_NAME AND"
		// + " A.OBJECT_NAME = B.OBJECT_NAME AND A.STREAM_NAME =
		// '").append("KERNEL").append("' AND A.PROJECT_NAME =
		// '").append("14.2ROFC").append("' AND A.MAKER_ID IS NOT NULL AND
		// A.MAKER_ID <> 'SYSTEM'"
		// + " AND TRIM(A.FILE_CREATED) IS NULL AND A.OBJECT_TYPE <>
		// 'CONVERSION_SCRIPT' AND (B.BASE_LINK_OBJECT <> 'Y' OR
		// B.BASE_LINK_OBJECT IS NULL)"
		// + " ORDER BY A.MAKER_DT_STAMP, A.VERSION_NO"));
		// HashSet<String> sfrset = new HashSet<>();
		//
		//
		// sfrset.add("25645");
		// sfrset.add("255");
		// sfrset.add("2564jfv");
		// sfrset.add("256djvkj5");
		// System.out.println(convertListToCSVwithSingleQuotes(new
		// ArrayList<>(sfrset)));

		// renameFolder(new File("D:\\dump\\SOFT_SingleFix\\soft_new"));
		StringBuffer opSql = new StringBuffer();
		StringBuffer staticOpSql = new StringBuffer();
		ArrayList<String> tempArray = new ArrayList<>();
		tempArray.add("HOST");
		tempArray.add("EL");
		opSql.append(
				"SELECT OBJECT_NAME,       VERSION_NO,       LATEST_ACTION,       SFR_NO,       SITE_CODE,       CHECKIN_STATUS, "
						+ " MAKER_ID,       MAKER_DT_STAMP,       TO_CHAR(MAKER_DT_STAMP, 'RRRRMMDD_HH24MISSFF3') MAKER_DATE,      "
						+ " OBJECT_TYPE  FROM DLTB_OPERATION_MASTER"
						+ " WHERE STREAM_NAME = 'KERNEL'   AND PROJECT_NAME = '")
				.append("DDLJ_TRAINING").append("'").append(" AND SUBPROJECT_PATH IN (")
				.append(convertListToCSVwithSingleQuotes(tempArray))
				.append(") AND MAKER_ID <> 'SYSTEM'   AND trim(SFR_NO) = '").append("123456").append("'");
		staticOpSql
				.append("SELECT OBJECT_NAME,       VERSION_NO,       LATEST_ACTION,       SFR_NO,       SITE_CODE,       CHECKIN_STATUS, "
						+ " MAKER_ID,       MAKER_DT_STAMP,       TO_CHAR(MAKER_DT_STAMP, 'RRRRMMDD_HH24MISSFF3') MAKER_DATE     "
						+ "   FROM DLTB_STATIC_OPERATION_MASTER"
						+ " WHERE STREAM_NAME = 'KERNEL'   AND PROJECT_NAME = '")
				.append("DDLJ_TRAINING").append("'").append(" AND SUBPROJECT_PATH IN (")
				.append(convertListToCSVwithSingleQuotes(tempArray))
				.append(") AND MAKER_ID <> 'SYSTEM'   AND trim(SFR_NO) = '").append("123456").append("'");

		System.out.println(opSql.toString());
		System.out.println(staticOpSql.toString());
	}

	public static String convertListToCSVwithSingleQuotes(List<String> listOfString) {
		StringBuffer sbf = new StringBuffer();
		int count = 0;
		for (String s : listOfString) {
			if (count == 0) {
				sbf.append("'").append(s).append("'");
			} else {

				sbf.append(",").append("'").append(s).append("'");
			}
			++count;
		}
		return sbf.toString();
	}

	private static void mergeAllLogFiles(String logFileName) {

		byte[] readAllBytes;
		try {
			readAllBytes = Files.readAllBytes(Paths.get(logFileName));

			Files.write(Paths.get("D:/consleLog.txt"), readAllBytes, StandardOpenOption.CREATE,
					StandardOpenOption.APPEND);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void renameFolder(File file) {

		File currFile = file;
		if (file.isDirectory()) {
			if (file.getName().equals("FILES")) {
				File newFile = new File(file.getParentFile(), "CONSOL");
				file.renameTo(newFile);
				currFile = newFile;
			}

			File[] listFiles = currFile.listFiles();
			if (listFiles != null && listFiles.length > 0) {
				for (File ddlFile : listFiles) {
					if (ddlFile.isDirectory()) {
						renameFolder(ddlFile);
					} else if (ddlFile.getParentFile().getName().equals("DDL")) {
						File newParentFile = new File(ddlFile.getParentFile(), "TABLE");
						newParentFile.mkdirs();
						File newFile = new File(newParentFile, ddlFile.getName());
						ddlFile.renameTo(newFile);

					}
				}
			}

		}
	}

	public static void copyFolder(File src, File dest) {

		if (src.isDirectory()) {
			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdir();
				// LOG.log(Level.FINE, "Directory copied from " + src + " to " +
				// dest);
			}
			// list all the directory contents
			String files[] = src.list();
			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// recursive copy
				copyFolder(srcFile, destFile);
			}

		} else {
			// if file, then copy it Use bytes stream to support all file types
			try {
				Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		// LOG.log(Level.FINE, "Directory copied from " + src + " to " + dest);

	}

	public static void copyBugToBug(String sourceBug, String destBug) {
		Path sourceParentFolder = Paths.get(sourceBug);
		Path destinationParentFolder = Paths.get(destBug);
		Stream<Path> allFilesPathStream = null;
		try {
			allFilesPathStream = Files.walk(sourceParentFolder);
			Consumer<? super Path> action = new Consumer<Path>() {

				@Override
				public void accept(Path t) {
					try {
						String destinationPath = t.toString().replaceAll(sourceParentFolder.toString(),
								destinationParentFolder.toString());
						Files.copy(t, Paths.get(destinationPath));
					} catch (FileAlreadyExistsException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

			};
			allFilesPathStream.forEach(action);

		} catch (FileAlreadyExistsException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
	}

	public static String[] readBugListFile(String bugListPath) {
		ArrayList<String> bugList = new ArrayList<>();
		File bugListFile = new File(bugListPath);
		BufferedReader reader = null;
		int count = 0;
		try {
			reader = new BufferedReader(new FileReader(bugListFile));
			String line = null;
			while ((line = reader.readLine()) != null) {
				bugList.add(line.trim());
				count++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bugList.toArray(new String[count]);
	}

	public static String readFile(String inputFile) {
		File file = new File(inputFile);
		String xmlName = file.getName();
		String fileName = xmlName.substring(0, xmlName.indexOf("."));
		String line;
		StringBuilder xml = new StringBuilder();
		FileReader fileReader = null;
		BufferedReader reader = null;

		try {
			fileReader = new FileReader(file);
			reader = new BufferedReader(fileReader);
			while ((line = reader.readLine()) != null) {
				if (!((line.trim().equalsIgnoreCase("<" + fileName.trim() + ">"))
						|| (line.trim().equalsIgnoreCase("</" + fileName.trim() + ">")))) {
					xml.append(line).append("\n");
				}
			}
		} catch (Exception ex) {
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (fileReader != null) {
					fileReader.close();
				}
			} catch (Exception ex) {
			}
		}
		return xml.toString();
	}
	// public static void main(String[] args) {
	// // TODO Auto-generated method stub
	//// String userdetails =
	// "#RU#,#INCD#,#SD#,#FN#,#SP#,#N#,#R#,#SV#,#A#,#SA#,";
	//// userdetails = userdetails.substring(0, userdetails.length()-1);
	////
	//// System.out.println(userdetails);
	//
	// int cutoffend = 63;
	// int cutoffstart = 0;
	// String toFormat = "Indicate the Unique Sequence Number for the event
	// posted in Foreign Exchange contract";
	//
	// System.out.println(toFormat.substring(cutoffstart,cutoffend));
	//
	// while(!toFormat.substring(cutoffstart,cutoffend).endsWith(" ")){
	//
	// cutoffend = cutoffend -1;
	// }
	//
	// cutoffend = cutoffend-1;
	//
	// System.out.println("FCUBS_14.1.0.0.0_SUPPORT".substring(0,
	// "FCUBS_14.1.0.0.0_SUPPORT".indexOf("_SUPPORT")));
	// File dir1 = new File("D:\\ddl_temp\\");
	// File[] filesInDir1= new File[15000];
	// if(dir1.exists()){
	// filesInDir1=dir1.listFiles();
	//
	// for(File f1:filesInDir1){
	// String project= f1.getName();
	//
	// System.out.println(project);
	// }
	// }
	// }

	public static String baseFirstThreeCommon(String str) {
		char c = '.';
		int pos = str.indexOf(c, 0);
		int n = 2;
		while (n-- > 0 && pos != -1)
			pos = str.indexOf(c, pos + 1);
		return str.substring(0, str.indexOf(c, pos));
	}

}
