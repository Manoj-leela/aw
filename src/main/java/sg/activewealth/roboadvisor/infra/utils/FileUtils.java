package sg.activewealth.roboadvisor.infra.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import sg.activewealth.roboadvisor.infra.exception.SystemException;

public class FileUtils {
	
	private Logger logger = Logger.getLogger(FileUtils.class);
		
	private static FileUtils me;
	
	private static final String JPEG_CONTENT_TYPE = "image/jpeg";
	private static final String JPG_CONTENT_TYPE = "image/pjpeg";
	private static final String PNG_CONTENT_TYPE = "image/png";
	private static final String PDF_CONTENT_TYPE = "application/pdf";

	public static FileUtils getInstance() {
		if (me == null) me = new FileUtils();

		return me;
	}

	public byte[] getBytes(File file) {
		InputStream fis = null;
		byte[] buff = null;

		try {
			fis = new BufferedInputStream(new FileInputStream(file));
			buff = new byte[fis.available()];
			fis.read(buff);
		}
		catch (IOException e) {
			throw new SystemException(e);
		}
		finally {
			if (fis != null) try {
				fis.close();
			}
			catch (IOException e) {
				throw new SystemException(e);
			}
		}

		return buff;
	}

	public byte[] getBytes(InputStream inputStream) {
		byte[] buff = null;

		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(inputStream);
			buff = new byte[bis.available()];
			bis.read(buff);
		}
		catch (IOException e) {
			throw new SystemException(e);
		}
		finally {
			if (bis != null) try {
				bis.close();
			}
			catch (IOException e) {
				throw new SystemException(e);
			}
		}

		return buff;
	}

	public void buildFolders(String startFolder, String filePathAfterFromStartFolder) throws IOException {
		String fileSeparator = System.getProperty("file.separator");
		
		// get individual folders
		StringTokenizer st = new StringTokenizer(filePathAfterFromStartFolder, fileSeparator);
		StringBuffer builtSoFar = new StringBuffer(startFolder);
		while (st.hasMoreTokens()) {
			String indivFolder = st.nextToken();

			String fileName = builtSoFar.append(fileSeparator + indivFolder).toString();
			File file = new File(fileName);
			// if is folder
			if (fileName.indexOf(".") == -1) {
				file.mkdir();
			}
			// if is file
			else {
				file.createNewFile();
			}
		}
	}
	
	public String setFileExtensionUsingContentType(String fileName,String contentType){
		if (JPEG_CONTENT_TYPE.equalsIgnoreCase(contentType)) {
			fileName = fileName + ".jpeg";
		} else if (JPG_CONTENT_TYPE.equalsIgnoreCase(contentType)) {
			fileName = fileName + ".jpg";
		} else if (PNG_CONTENT_TYPE.equalsIgnoreCase(contentType)) {
			fileName = fileName + ".png";
		} else if (PDF_CONTENT_TYPE.equalsIgnoreCase(contentType)) {
			fileName = fileName + ".pdf";
		} 
		return fileName;
	}
	
	public MultipartFile convertBase64ToMultipartFile(String encodedFile, String fileName, String contentType, String encodedFilePath,String filePath) {
		FileOutputStream out = null;
		MockMultipartFile mockMultipartFile = null;
		try {
			byte[] decodeFileToByte = Base64Utils.getInstance().decode(encodedFile);
			out = new FileOutputStream(encodedFilePath);
			out.write(decodeFileToByte);
			mockMultipartFile = new MockMultipartFile(fileName, filePath, contentType, decodeFileToByte);
		} catch (IOException e) {
			logger.error("File not found:" + e.getMessage());
			return null;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
			}
		}
		return mockMultipartFile;
	}
	
}
