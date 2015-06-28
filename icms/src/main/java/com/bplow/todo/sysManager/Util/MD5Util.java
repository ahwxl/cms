package com.bplow.todo.sysManager.Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

/**
* 通过MD5加密算法返回加密后的字符串
* 
* @param text 明文（要加密的字符串）
* @return
*/
public static String createMD5(String text) {
MessageDigest md = null;
try {
md = MessageDigest.getInstance("MD5");
} catch (NoSuchAlgorithmException e) {// 理论上不会有这个异常
throw new IllegalStateException("System doesn't support MD5 algorithm.");
}
md.update(text.getBytes());
byte b[] = md.digest();
StringBuffer buf = new StringBuffer("");
for (int offset = 0; offset < b.length; offset++) {
int i = b[offset];
if (i < 0) {
i += 256;
}
if (i < 16) {
buf.append("0");// 不足两位，补0
}
buf.append(Integer.toHexString(i));
}
return buf.toString();
}

 
 public static String JM(String inStr) {   
 char[] a = inStr.toCharArray();   
  for (int i = 0; i < a.length; i++) {   
  a[i] = (char) (a[i] ^ 't');   
  }   
  String k = new String(a);   
  return k;   
 } 
public static void main(String[] args) {
	String a = MD5Util.createMD5("123456");
	String b = MD5Util.JM("e10adc3949ba59abbe56e057f20f883e");
	System.out.println(a);
	System.out.println(b);
}

}