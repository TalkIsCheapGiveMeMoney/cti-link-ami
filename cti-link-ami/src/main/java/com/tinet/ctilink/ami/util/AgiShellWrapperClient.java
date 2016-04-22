package com.tinet.ctilink.ami.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AgiShellWrapperClient {
	public static String execute(String ip, Integer port, String cmd, String params){
		String res="";
		try{
			Socket socket=new Socket(ip, port);
			PrintWriter os=new PrintWriter(socket.getOutputStream());
			BufferedReader is=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			os.println("agi_network_script: " + cmd);
			os.println("agi_arg_1: " + params);
			os.println("\n");
			os.flush();
			
			res=is.readLine();
			os.close(); //关闭Socket输出流
			is.close(); //关闭Socket输入流
			socket.close(); //关闭Socket
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(res!=null && res.contains("SET VARIABLE AGI_RESULT ")){
			res = res.substring(24);
		}
		return res;
	}

	public static void main(String[] argv){
		System.out.println("agi shell wrapper test...");
		String result = AgiShellWrapperClient.execute("172.16.203.129", 4574, "ttsc_break_send.sh", "1234 大家好");
		System.out.println("result="+result);
	}
}
