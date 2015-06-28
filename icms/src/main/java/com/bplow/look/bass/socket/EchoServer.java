package com.bplow.look.bass.socket;

import java.io.*;
import java.net.*;

/****************************************************
 * 作者：孙卫琴                                     *
 * 来源：<<Java网络编程精解>>                       *
 * 技术支持网址：www.javathinker.org                *
 ***************************************************/

public class EchoServer {
  private int port=8801;
  private ServerSocket serverSocket;

  public EchoServer() throws IOException {
    serverSocket = new ServerSocket(port);
    System.out.println("服务器启动");
  }

  public String echo(String msg) {
    return "echo:" + msg;
  }

  private PrintWriter getWriter(Socket socket)throws IOException{
    OutputStream socketOut = socket.getOutputStream();
    return new PrintWriter(socketOut,true);
  }
  private BufferedReader getReader(Socket socket)throws IOException{
    InputStream socketIn = socket.getInputStream();
    return new BufferedReader(new InputStreamReader(socketIn));
  }

  public void service() {
    //while (true) {
      Socket socket=null;
      try {
        socket = serverSocket.accept();  //等待客户连接
        System.out.println("New connection accepted " 
                        +socket.getInetAddress() + ":" +socket.getPort());
        BufferedReader br =getReader(socket);
        PrintWriter pw = getWriter(socket);

        String msg = null;
        //socket.
        while ((msg = br.readLine()) != null) {
          System.out.println("server msg=="+msg); 
          pw.println(echo(msg));
          if (msg.equals("bye")) //如果客户发送的消息为“bye”，就结束通信
            break;
        }
      }catch (IOException e) {
         e.printStackTrace();
      }finally {
         try{
           if(socket!=null)socket.close();  //断开连接
         }catch (IOException e) {e.printStackTrace();}
      }
    //}
  }

  public static void main(String args[])throws IOException {
    new EchoServer().service();
  }
}



