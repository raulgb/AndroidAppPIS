package edu.ub.pis2016.pis16.strikecom.engine.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileIO {
	public InputStream readAsset(String fileName);

	public InputStream readFile(String fileName) throws IOException;

	public OutputStream writeFile(String fileName) throws IOException;
}