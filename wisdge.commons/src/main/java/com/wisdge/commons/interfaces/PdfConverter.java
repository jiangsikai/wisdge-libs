package com.wisdge.commons.interfaces;

public interface PdfConverter {
	public byte[] convert(byte[] data) throws Exception;
	public byte[] convert(String source) throws Exception;
}
