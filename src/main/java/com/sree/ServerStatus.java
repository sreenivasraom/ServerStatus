/**
 * 
 */
package com.sree;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;
import com.sree.Mailer;

/**
 * @author sreenivasrao.m
 *
 */
public class ServerStatus {
	
	public static boolean isServerUp(String serverName, String port) {
	    boolean isUp = false;
	    Socket socket;
		try {
			socket = new Socket(serverName, Integer.parseInt(port));
			isUp = true;
	        socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return isUp;
	}
	
	public static void checkServersStatus(){
		Properties serverprops = new Properties();
		try {
			serverprops.load(ServerStatus.class.getResourceAsStream("/servers.properties"));
			String serverName = serverprops.getProperty("server.name");
			Mailer mailer = new Mailer();
			Boolean rabitServerState = isServerUp(serverName, serverprops.getProperty("rabitserver.httpport"));
			Boolean buildAgentState = isServerUp(serverName, serverprops.getProperty("buidagent.port"));
			Boolean testAgentState = isServerUp(serverName, serverprops.getProperty("testagent.port"));
			String rabitServerStatus = "RabitServer is running.";
			String buildAgentStatus = "BuildAgent is running.";
			String testAgentStatus = "TestAgent is running";
			if(rabitServerState){
				rabitServerStatus = "RabitServer is running.";
			} else {
				rabitServerStatus = "RabitServer is down. Please connect to the server and start the RabitServer.";
			}
			if(buildAgentState){
				buildAgentStatus = "BuildAgent is running.";
			} else {
				buildAgentStatus = "BuildAgent is down. Please connect to the server and start the BuildAgent.";
			}
			if(testAgentState){
				testAgentStatus = "TestAgent is running";
			} else {
				testAgentStatus = "TestAgent is down. Please connect to the server and start the TestAgent.";
			}
			if(rabitServerState && buildAgentState && testAgentState){
				System.out.println("All services are running.");
			} else {
				String commonMsg = "Please start the AutoRABIT server following the steps below :<br/>AutoRabit Server and Agents installation location : /opt/newhd/rabitinstall<br/>To start AutoRabit Server run the command /opt/newhd/rabitinstall/apache-tomcat-7.0.50/bin/startup.sh<br/>To start build agent change to <b>/opt/newhd/rabitinstall/rba/buildagent/buildagent.sh</b><br/>To start test agent change to <b>/opt/newhd/rabitinstall/rta/start-rta.sh</b><br/>";
				mailer.sendMail(rabitServerStatus + "<br/>" + buildAgentStatus + "<br/>" + testAgentStatus + "<br/>" + commonMsg);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		checkServersStatus();

	}

}
