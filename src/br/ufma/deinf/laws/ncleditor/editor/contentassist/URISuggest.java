package br.ufma.deinf.laws.ncleditor.editor.contentassist;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Vector;
import br.ufma.deinf.laws.util.UrlUtils;


/**
 * @author Roberto Azevedo
 *
 * @since October 15 2008
 * 
 * This class implements a mechanism to suggest URI of files based on pathRoot and a qualifier 
 * that is a substring of the final URI that the users want write.
 */

public class URISuggest {
	String rootPath;
	URI uriRoot;
	boolean isRelative = false;
	
	public URISuggest(String rootPath) throws URISyntaxException {
		this.rootPath = UrlUtils.encodeURL(rootPath);
		uriRoot = new URI(this.rootPath);
		System.out.println(this.rootPath);
	}
	
	private File computeParentFile(String qualifier) throws URISyntaxException{
		isRelative = !qualifier.startsWith("file://"); // true if qualifier is relative
		File file; // file Path
		
		if(isRelative){
			if(qualifier.contains("/")){
				file = new File(uriRoot.getPath() + "/" + 
						qualifier.substring(0, qualifier.lastIndexOf("/"))); // is relative
			}
			else file = new File(uriRoot.getPath());
		}
		else{
			String parentPath = qualifier;
			if(qualifier.contains("/")){
				parentPath = parentPath.substring(0, parentPath.lastIndexOf("/"))+"/";
			}
			System.out.print(parentPath);
			uriRoot = new URI(UrlUtils.encodeURL(parentPath));
			file = new File(uriRoot); // not relative
		}
		return file;
	} 
	
	private Vector <String> getResource(String qualifier, boolean isFile){
		Vector<String> resources = new Vector<String>();
		File f;
		String strSuggest;
		try {
			f = computeParentFile(qualifier);
			System.out.println("Absolute Path -> " + f.getAbsolutePath());
			String [] children = f.list();
			if(children == null) return resources;
			System.out.println(children.length);
			for(int i = 0; i < children.length; i++){
					File suggest = new File(f.getAbsoluteFile()+"/"+children[i]);
					if((suggest.isDirectory() && !isFile) || (suggest.isFile() && isFile)){
						if(!isRelative){
							strSuggest = "file://"+uriRoot.getPath()+children[i];
							if(!isFile) strSuggest += "/";
							resources.add(strSuggest);
						}
						else {
							if(qualifier.contains("/"))
								strSuggest = qualifier.substring(0, qualifier.lastIndexOf("/"))+"/"+children[i];
							else strSuggest = children[i];
							if(!isFile) strSuggest += "/";
							resources.add(strSuggest);
						}
						System.out.println(strSuggest);
					}
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resources;
	}
	/**
	 * 
	 * @param qualifier
	 * @return A vector of directories computed
	 */
	public Vector <String> getDirectories(String qualifier){
		System.out.println("Computing Directories");
		return getResource(qualifier, false);		
	}
	
	/**
	 * 
	 * @param qualifier
	 * @return A Vector of files computed
	 */
	public Vector <String> getFiles(String qualifier){
		System.out.println("Computing Files");
		return getResource(qualifier, true);
	}
	
	/**
	 * just for tests
	 * @param args
	 * @throws URISyntaxException
	 */
	public static void main(String [] args) throws URISyntaxException{
		URISuggest fs = new URISuggest("file:///C:/");
		Vector <String> v = fs.getDirectories("file:///C:/Users/usuario/Program Files (x86)/");
		System.out.println("## DIRECTORIES ##");
		for(int i = 0; i < v.size(); i++){
			System.out.println(v.get(i));
		}
		
		System.out.println("## FILES ##");
		v = fs.getFiles("file:///C:/Users/usuario/");
		for(int i = 0; i < v.size(); i++){
			System.out.println(v.get(i));
		}
	}
	
}
