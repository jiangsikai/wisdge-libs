package com.wisdge.web.filetypes;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.wisdge.utils.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Description: file types class Copyright:(c)2005 Wisdge.com
 * 
 * @author Kevin MOU
 * @version 1.2
 */
public class FileExt {
	private static Logger logger = LoggerFactory.getLogger(FileExt.class);
	private static java.util.Vector<Map<String, String>> extVT = null;

	private static synchronized void initialize() {
		buildConfig("FileExtType.xml");
	}

	private static void buildConfig(String source) {
		if (extVT != null) {
			return;
		}

		extVT = new java.util.Vector<>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			InputStream in = FileExt.class.getResourceAsStream(source);
			Document doc = builder.parse(in);
			Element root = doc.getDocumentElement();

			// get attachment property
			NodeList nl = root.getElementsByTagName("suffix");
			for (int i = 0; i < nl.getLength(); i++) {
				Map<String, String> suffix = new HashMap<String, String>();
				suffix.put("ext", ((Element) nl.item(i)).getAttribute("ext"));
				suffix.put("image", ((Element) nl.item(i)).getAttribute("image"));
				suffix.put("contentType", ((Element) nl.item(i)).getAttribute("contentType"));
				extVT.add(suffix);
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 根据文件后缀，取得相应的ContentType
	 * 
	 * @param ext
	 *            文件后缀
	 * @return ContentType的String对象
	 */
	public static String getContentTypeByExt(String ext) {
		if (ext == null) {
			return null;
		}

		if (extVT == null) {
			initialize();
		}
		for (int i = 0; i < extVT.size(); i++) {
			Map<String, String> suffix = extVT.get(i);
			if (suffix.get("ext").equals(ext)) {
				return suffix.get("contentType");
			}
		}
		return null;
	}

	/**
	 * 根据文件名，取得相应的ContentType
	 * 
	 * @param filename
	 *            文件名
	 * @return ContentType的String对象
	 */
	public static String getContentTypeByFilename(String filename) {
		if (filename == null) {
			return "application/x-msdownload";
		}
		String contenttype = getContentTypeByExt(FilenameUtils.getExtension(filename));
		if (contenttype == null) {
			contenttype = "application/x-msdownload";
		}
		return contenttype;
	}

	/**
	 * 根据文件后缀，取得对应的LOGO图片的文件名
	 * 
	 * @param ext
	 *            文件名后缀
	 * @return String对象，LOGO图片文件名
	 */
	public static String getImgByExt(String ext) {
		if (StringUtils.isEmpty(ext)) {
			return "shb.gif";
		}

		if (extVT == null) {
			initialize();
		}
		for (int i = 0; i < extVT.size(); i++) {
			Map<String, String> suffix = extVT.get(i);
			if (suffix.get("ext").equals(ext)) {
				return suffix.get("image");
			}
		}
		return "shb.gif";
	}

	/**
	 * 根据文件名，取得对应的LOGO图片文件名
	 * 
	 * @param filename
	 *            文件名
	 * @return String对象， LOGO文件名
	 */
	public static String getImgByFilename(String filename) {
		if (filename == null) {
			return "shb.gif";
		}
		return getImgByExt(FilenameUtils.getExtension(filename));
	}

}