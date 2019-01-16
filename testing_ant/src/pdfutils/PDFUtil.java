package pdfutils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.stream.Stream;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfEncryptor;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFUtil {

	public static void main(String[] args) {
		String inFile = "D:\\dump\\MUTUAL_FUNDS\\ICICI_33_NOV2018.pdf";
		bulkSaveAsUnprotected(inFile, "dand0206");
		// bulkSplit(inFile, 6, 7);
//		merge(inFile);
		// rotate(inFile);
	}

	public static void bulkSaveAsUnprotected(String filePath, String password) {
		File file = new File(filePath);
		if (file.isFile()) {
			saveAsUnprotected(file.getAbsolutePath(), password);
		} else if (file.isDirectory()) {
			Stream.of(file.listFiles()).sorted().parallel().forEach(x -> saveAsUnprotected(x.getAbsolutePath(), password));
		}
	}

	public static void bulkSplit(String filePath, int start, int end) {
		File file = new File(filePath);
		if (file.isFile()) {
			split(file.getAbsolutePath(), start, end);
		} else if (file.isDirectory()) {
			Stream.of(file.listFiles()).parallel().forEach(x -> split(x.getAbsolutePath(), start, end));
		}
	}

	private static void saveAsUnprotected(String inFile, String password) {
		try {

			// Password in second argument
			PdfReader reader = new PdfReader(inFile, password.getBytes());

			String outFile = inFile.substring(0, inFile.lastIndexOf(".pdf")) + "_BB.pdf";

			PdfReader.unethicalreading = true;
			// reader.removeUsageRights();
			PdfEncryptor.encrypt(reader, new FileOutputStream(outFile), null, null,
					PdfWriter.ALLOW_ASSEMBLY | PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_DEGRADED_PRINTING
							| PdfWriter.ALLOW_FILL_IN | PdfWriter.ALLOW_MODIFY_ANNOTATIONS
							| PdfWriter.ALLOW_MODIFY_CONTENTS | PdfWriter.ALLOW_PRINTING
							| PdfWriter.ALLOW_SCREENREADERS,
					false);

			System.out.println("Writing " + outFile);
			Document document = new Document(reader.getPageSizeWithRotation(1));
			PdfCopy writer = new PdfCopy(document, new FileOutputStream(outFile));
			document.open();

			int n = reader.getNumberOfPages();
			for (int i = 1; i <= n; i++) {
				PdfImportedPage page = writer.getImportedPage(reader, i);
				writer.addPage(page);
			}

			document.setJavaScript_onLoad("");
			document.close();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void merge(String filePath) {
		try {

			File folder = new File(filePath);
			File[] files = folder.listFiles();

			String outFile = new File(folder, folder.getName() + "_Merged.pdf").getAbsolutePath();
			PdfReader reader1 = new PdfReader(files[0].getAbsolutePath());
			Document document = new Document(reader1.getPageSizeWithRotation(1));
			PdfCopy writer = new PdfCopy(document, new FileOutputStream(outFile));
			System.out.println("Writing " + outFile);
			document.open();
			for (File file : files) {
				reader1 = new PdfReader(file.getAbsolutePath());
				int n = reader1.getNumberOfPages();
				for (int i = 1; i <= n; i++) {
					PdfImportedPage page = writer.getImportedPage(reader1, i);
					writer.addPage(page);
				}
			}
			document.close();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void rotate(String inFile) {
		try {

			PdfReader reader = new PdfReader(inFile);
			int n = reader.getNumberOfPages();
			PdfDictionary page;
			PdfNumber rotate;
			for (int p = 1; p <= n; p++) {
				page = reader.getPageN(p);
				rotate = page.getAsNumber(PdfName.ROTATE);
				if (rotate == null) {
					page.put(PdfName.ROTATE, new PdfNumber(90));
				} else {
					page.put(PdfName.ROTATE, new PdfNumber((rotate.intValue() + 90) % 360));
				}
			}
			PdfStamper stamper = new PdfStamper(reader,
					new FileOutputStream(inFile.substring(0, inFile.lastIndexOf(".pdf")) + "_rotated.pdf"));
			stamper.close();
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void split(String inFile, int start, int end) {
		try {

			System.out.println("Reading " + inFile);
			PdfReader reader = new PdfReader(inFile);
			int n = reader.getNumberOfPages();
			System.out.println("Number of pages : " + n);

			String outFile = inFile.substring(0, inFile.lastIndexOf(".pdf")) + "_Split [" + start + "-" + end + "].pdf";
			// + String.format("%03d", 1) + ".pdf";
			System.out.println("Writing " + outFile);
			Document document = new Document();
			PdfCopy writer = new PdfCopy(document, new FileOutputStream(outFile));
			document.open();

			for (int i = start; i <= end && i <= n; i++) {
				PdfImportedPage page = writer.getImportedPage(reader, i);
				writer.addPage(page);
			}

			document.close();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
