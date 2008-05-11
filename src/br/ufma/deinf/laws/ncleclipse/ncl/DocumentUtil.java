package br.ufma.deinf.laws.ncleclipse.ncl;

import java.io.File;

public class DocumentUtil {
	public static String getAbsoluteFileName(String path, String fileName) {
		try {
			File f = new File(path);
			return f.getParent()+"/"+fileName; 
		}
		catch(Exception e) {
			return "";
		}
	}
}
