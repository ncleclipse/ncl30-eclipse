/*******************************************************************************
 * This file is part of the authoring environment in Nested Context Language -
 * NCL Eclipse.
 * 
 * Copyright: 2007-2009 UFMA/LAWS (Laboratory of Advanced Web Systems), All Rights Reserved.
 * 
 * This program is free software; you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU General Public License version 2 for more 
 * details.
 * 
 * You should have received a copy of the GNU General Public License version 2
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 * For further information contact:
 * 		ncleclipse@laws.deinf.ufma.br
 * 		http://www.laws.deinf.ufma.br/ncleclipse
 * 		http://www.laws.deinf.ufma.br
 ********************************************************************************/
package br.ufma.deinf.laws.ncleclipse.launch.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.ui.console.MessageConsoleStream;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.SFTPv3Client;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class RemoteUtility {
	private String hostName;
	
	private String userName;
	private String userPassword;
	
	private Connection connection;
	
	private SCPClient scp;
	private SFTPv3Client sftp;
	
	private boolean verboseMode;
	
	private MessageConsoleStream consoleStream;
	
	public RemoteUtility(
			String hostName, 
			String userName, 
			String userPassword,
			MessageConsoleStream consoleStream) {
		
		// Setting attributes
		setHostName(hostName);
		setUserName(userName);
		setUserPassword(userPassword);
		setConsoleStream(consoleStream);
		
		setConnection(new Connection(getHostName()));
		
		setVerboseMode(false);
	}
	
	public void connect() 
		throws IOException{
		
		// Connecting to host
		connection.connect();
	
		// Authenticating with password
		if (!connection.authenticateWithPassword(userName, userPassword)){
			throw new IOException();
		}
		
		// Setting clients services
		setSCP(new SCPClient(connection));
		setSFTP(new SFTPv3Client(connection));
	}
	
	public void put(String localFile, String remoteDirectory) 
		throws IOException{
		
		scp.put(localFile, remoteDirectory);
	}
	
	public void get(String remoteFile, String localDirectory) 
		throws IOException{
		
		scp.get(remoteFile,localDirectory);
	}
	
	public void sync(String localPath, String remotePath){
		// TODO: public void sync(String localPath, String remotePath)
	}
	
	public void update(String remotePath, String localDirectory){
		// TODO: public void update(String remotePath, String localDirectory)
	}
	
	public String format(String path){
		return path.replace(' ', '_');
	}

	public void commit(String localPath, String remoteDirectory) 
		throws IOException{
			
		// Setting values
		File localFile = new File(localPath);	
			
		String localFileName = localFile.getName(); 
		String localFilePath = localFile.getAbsolutePath();	
		String localSeparator = System.getProperty("file.separator");
		
		// Getting remoteDirectory without last separator
		if (remoteDirectory.charAt(remoteDirectory.length()-1) 
				== localSeparator.charAt(0)){
			
			remoteDirectory = remoteDirectory
				.substring(0, remoteDirectory.length()-1);
		}
		
		if (localFile.isDirectory()){
			// Verifying if directory exist on server
			try {
				// If exist, do nothing
				sftp.lstat(
						format(
							remoteDirectory +
							localSeparator +
							localFileName
						));
				
			} catch (IOException e) {
				// If doesnt exist, create directory on server
				if (verboseMode == true){
					consoleStream.println(
							"Copying " +
							"'" +
							localFilePath +
							"'" +
							" " + 
							"to" +
							" " +
							"'" +
							format(remoteDirectory) +
 							"'" +
							".");
				}
			
				sftp.mkdir(
						format(
							remoteDirectory +
							localSeparator +
							localFileName
						), 
						644);
			}
			
			// Commit sub directories and files
			String[] subFilesNames = localFile.list();
			
			for (String subFileName : subFilesNames) {
				commit(localFilePath+localSeparator+subFileName, 
						remoteDirectory+localSeparator+localFileName);
			}	
		}else{
			// Verifying if file exist on server
			try {
				// If exist and is old, copy file to server
				long localFileLastModified =
					localFile.lastModified()/1000;
				
				long remoteFileLastModified = 
					sftp.lstat(
							format(
								remoteDirectory +
								localSeparator +
								localFileName
							)).atime;
								
				if (localFileLastModified > remoteFileLastModified){
					if (verboseMode == true){
						consoleStream.println(
								"Copying " +
								"'" +
								localFilePath +
								"'" +
								" " +
								"to" +
								" " +
								"'" +
								format(remoteDirectory) +
								"/" +
								format(localFileName) +
								"'" +
								".");
					}
					
					scp.put(localFilePath,
							format(localFileName),
							format(remoteDirectory),
							"0644");
				}
			} catch (IOException e) {
				// If doesnt exist, copy file to server
				if (verboseMode == true){
					consoleStream.println(
							"Copying " +
							"'" +
							localFilePath +
 							"'" +
							" " +
							"to" +
							" " +
							"'" +
							format(remoteDirectory) +
							"/" +
							format(localFileName) +
							"'" +
							".");
				}
				
				scp.put(localFilePath,
						format(localFileName),
						format(remoteDirectory),
						"0644");
			}
		}
	}
	
	public void exec(String command) 
		throws IOException{
		
		// Sending command to server 
		if (verboseMode == true){
			consoleStream.println(
					"Executing" +
					" " +
					"'" + 
					command +
 					"'" +
					".");			
		}
		
		Session session = connection.openSession();
		session.execCommand(command);
		
		// Copying server stdout on local stdout
		InputStream remoteStdout = 
			new StreamGobbler(session.getStdout());

		BufferedReader remoteStdoutReader = 
			new BufferedReader(new InputStreamReader(remoteStdout));

		String line;
		
		while ((line = remoteStdoutReader.readLine()) != null){
			consoleStream.println(line);
		}
		
		// Close session
		session.close();
	}
	
	public void close(){
		connection.close();
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public SCPClient getSCP() {
		return scp;
	}

	public void setSCP(SCPClient scp) {
		this.scp = scp;
	}

	public SFTPv3Client getSFTP() {
		return sftp;
	}

	public void setSFTP(SFTPv3Client sftp) {
		this.sftp = sftp;
	}
	
	public MessageConsoleStream getConsoleStream() {
		return consoleStream;
	}

	public void setConsoleStream(MessageConsoleStream consoleStream) {
		this.consoleStream = consoleStream;
	}
	
	public void setVerboseMode(boolean verboseMode) {
		this.verboseMode = verboseMode;
	}	
}
