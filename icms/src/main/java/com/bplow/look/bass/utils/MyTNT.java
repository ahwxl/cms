package com.bplow.look.bass.utils;


import java.text.ParseException;
import java.util.Date;
import java.io.IOException;


import com.bplow.look.bass.DateUtil;

public class MyTNT {
    
    public static void main (String args[]) throws IOException, ParseException {

//        Console c = System.console();
//        if (c == null) {
//            System.err.println("No console.");
//            System.exit(1);
//        }
//
//        String login = c.readLine("Enter your login: ");
//        char [] oldPassword = c.readPassword("Enter your old password: ");
//
//        if (verify(login, oldPassword)) {
//            boolean noMatch;
//            do {
//                char [] newPassword1 =
//                    c.readPassword("Enter your new password: ");
//                char [] newPassword2 =
//                    c.readPassword("Enter new password again: ");
//                noMatch = ! Arrays.equals(newPassword1, newPassword2);
//                if (noMatch) {
//                    c.format("Passwords don't match. Try again.%n");
//                } else {
//                    change(login, newPassword1);
//                    c.format("Password for %s changed.%n", login);
//                }
//                Arrays.fill(newPassword1, ' ');
//                Arrays.fill(newPassword2, ' ');
//            } while (noMatch);
//        }
//
//        Arrays.fill(oldPassword, ' ');
          //System.out.println(java.net.URLEncoder.encode("http://218.206.200.139:9898/cms"));
          //System.out.println(java.net.URLEncoder.encode("http://api.t.sina.com.cn/oauth/request_token&oauth_consumer_key=1360815075&oauth_nonce=QP70eNmVz8jvdPevU3oJD2AfF7R7odC2XJcn4XlZJqk&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1272323042&oauth_version=1.0"));
          
          //short i = (short)40000;
          //System.out.println(i);
    	System.out.println(System.currentTimeMillis());
    	System.out.println(new Date().getTime());
    	System.out.println(DateUtil.strToDate("2013-1-11", "yyyy-mm-dd").getTime());
    	/*
          int i = 0;
          System.out.println(++i);
          Double db = Math.random()*1000;
          System.out.println(db.longValue());
          
          //System.out.print("abc\r\nabc2");
          
          HSSFWorkbook wb = new HSSFWorkbook();
          HSSFSheet sheet = wb.createSheet("new sheet");

          HSSFRow row = sheet.createRow(1);
          HSSFCell cell0 = row.createCell(0);
          HSSFCell cell1 = row.createCell(1);
          HSSFCell cell2 = row.createCell(2);
          
          cell0.setCellValue("属性1");
          cell1.setCellValue("属性2");
          cell2.setCellValue("属性3");
          HSSFCell cell = row.createCell(3);

          cell.setCellValue(new String("This is a test of merging"));

          sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));
          sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
          sheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
          sheet.addMergedRegion(new CellRangeAddress(1, 1, 3, 5));//行 列
          
          HSSFRow row2 = sheet.createRow((short) 2);
          cell = row2.createCell((short) 3);
          
          cell.setCellValue("属性1");
          
          cell = row2.createCell((short) 4);
          cell.setCellValue("属性2");
          
          HSSFRow row3 = sheet.createRow((short) 3);
          
          // Write the output to a file
          FileOutputStream fileOut = new FileOutputStream("d:/merging_cells.xls");
          wb.write(fileOut);
          fileOut.close();
          */
          
          
    
    }

    //Dummy verify method. 
    static boolean verify(String login, char[] password) {
        return true;
    }

    //Dummy change method.
    static void change(String login, char[] password) {}
}

