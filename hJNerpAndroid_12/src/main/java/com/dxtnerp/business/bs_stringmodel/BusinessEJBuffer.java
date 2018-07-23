package com.dxtnerp.business.bs_stringmodel;

import com.dxtnerp.common.Constant;

/**
 * Created by Admin on 2017/1/12.
 * 生成的字符串都存放在这里
 */

public class BusinessEJBuffer {

//    模板中数据上传到基础表的设置，必须要有如下红色字体的设置（其中id_clerk处为基础表在和交谈了中的主键字段）
//    var jss='{"tableid":"ctlm7162",
//    "tabletype":"B",
//    "opr":"SS",
//    "no":
//    "id_clerk",
//    "userid":"'+formData.id_user+'",
//    "comid":"'+formData.id_com+'",
//    "data":[{"table": "ctlm7162", "where": "", "data": [';
//    jss += '{"column":"flag_checkin","value":"4","datatype":"varchar"},' ;
//    jss += '{"column":"date_checkin","value":"'+formData.date_location+'","datatype":"varchar"},' ;
//    jss += '{"column":"date_opr","value":"'+formData.date_location+'","datatype":"varchar"},' ;
//    jss += '{"column":"id_recorder","value":"'+formData.id_user+'","datatype":"varchar"},' ;
//    jss += '{"column":"var_equno","value":"'+formData.var_location+'","datatype":"varchar"},' ;
//    jss += '{"column":"card_no","value":"'+var_signin+'","datatype":"varchar"}' ;
//    jss += ']  }  ] }';

    public static String getSginBuffer(String userID, String companyID, String date_location,
                                       String flag_sginin, String id_clerk, String id_com, String id_recorder,
                                       String var_equno, String var_location, String name_photo) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{\"tableid\":\"ctlm7162\"," +
                "\"opr\":\"SS\"," +
                "\"no\":\"\"," +
                "\"userid\":\"" + userID + "\"," +
                "\"menuid\":\"" + Constant.ID_MENU + "\"," +
                "\"dealtype\":\"save\"," +
                "\"comid\":\"" + companyID + "\",");
        stringBuffer.append("\"data\":[{\"table\": \"ctlm7162\",\"oprdetail\":\"N\",\"where\":\" \",");
        stringBuffer.append("\"data\":[{\"column\":\"card_no\",\"value\":\"\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"date_checkin\",\"value\":\"" + date_location + "\",\"datatype\":\"datetime\"}, ");
        stringBuffer.append("{\"column\":\"date_opr\",\"value\":\"" + date_location + "\",\"datatype\":\"datetime\"}, ");
        stringBuffer.append("{\"column\":\"flag_checkin\",\"value\":\"" + 4 + "\",\"datatype\":\"varchar\"}, ");
        stringBuffer.append("{\"column\":\"flag_signin\",\"value\":\"" + flag_sginin + "\",\"datatype\":\"varchar\"}, ");
        stringBuffer.append("{\"column\":\"id_clerk\",\"value\":\"" + id_clerk + "\",\"datatype\":\"varchar\"}, ");
        stringBuffer.append("{\"column\":\"id_com\",\"value\":\"" + id_com + "\",\"datatype\":\"varchar\"}, ");
        stringBuffer.append("{\"column\":\"id_recorder\",\"value\":\"" + id_recorder + "\",\"datatype\":\"varchar\"}, ");
        stringBuffer.append("{\"column\":\"var_equno\",\"value\":\"" + var_equno + "\",\"datatype\":\"varchar\"}, ");
        stringBuffer.append("{\"column\":\"var_location\",\"value\":\"" + var_location + "\",\"datatype\":\"varchar\"}, ");
        stringBuffer.append("{\"column\":\"name_photo\",\"value\":\"" + name_photo + "\",\"datatype\":\"varchar\"}]}]}");
        String str = stringBuffer.toString().trim();
        return str;
    }

    /**
     * @param userID
     * @param companyID
     * @param date_task
     * @param id_dept
     * @param flag_wadd
     * @param id_wtype
     * @param id_wproj
     * @param dec_wtime
     * @param var_wtitle
     * @param var_remark
     * @return 工作日志
     */
    public static String getDgtdrecBuffer(String menuid, String id_recorder, String userID,
                                          String companyID, String date_task,
                                          String id_dept, String flag_wadd, String id_wtype,
                                          String id_wproj, String dec_wtime, String var_wtitle,
                                          String var_remark, String name_wproj,
                                          String date_opr, int fiscal_period, int fiscal_year,
                                          String id_corr) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{\"tableid\":\"dgtdrec\",\"opr\":\"SS\",\"no\": \"\"," +
                "\"menuid\":\"" + menuid + "\",\"dealtype\":\"save\",\"userid\":\"" + userID + "\"," +
                "\"comid\":\"" + companyID + "\"," + "\"data\":[{\"table\": \"dgtdrec_03\"," +
                "\"oprdetail\":\"N\",\"where\": \"\", \"data\": [");
        stringBuffer.append("{\"column\":\"flag_sts\",\"value\":\"L\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"id_flow\",\"value\":\"FBdis\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"line_no\",\"value\":\"1\",\"datatype\":\"int\"},");
        stringBuffer.append("{\"column\":\"date_task\",\"value\":\"" + date_task + "\",\"datatype\":\"datetime\"},");
        stringBuffer.append("{\"column\":\"id_dept\",\"value\":\"" + id_dept + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"flag_wadd\",\"value\":\"" + flag_wadd + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"id_wtype\",\"value\":\"" + id_wtype + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"id_wproj\",\"value\":\"" + id_wproj + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"dec_wtime\",\"value\":\"" + dec_wtime + "\",\"datatype\":\"int\"},");
        stringBuffer.append("{\"column\":\"id_recorder\",\"value\":\"" + id_recorder + "\",\"datatype\":\"varchar\"}, ");
        stringBuffer.append("{\"column\":\"var_wtitle\",\"value\":\"" + var_wtitle + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"var_remark\",\"value\":\"" + var_remark + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"date_opr\",\"value\":\"" + date_opr + "\",\"datatype\":\"datetime\"},");
        stringBuffer.append("{\"column\":\"fiscal_period\",\"value\":\"" + fiscal_period + "\",\"datatype\":\"int\"},");
        stringBuffer.append("{\"column\":\"fiscal_year\",\"value\":\"" + fiscal_year + "\",\"datatype\":\"int\"},");
        stringBuffer.append("{\"column\":\"id_corr\",\"value\":\"" + id_corr + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"name_wproj\",\"value\":\"" + name_wproj + "\",\"datatype\":\"varchar\"}");
        stringBuffer.append("]}]}");
        String str = stringBuffer.toString().trim();
        return str;
    }
    
    
// ------------------------------------ 东兴堂 --------------------------------------
// business{
//  "tableid":"ddister","opr":"SS","no": "","userid":"test2","comid":"CM1092-0001",
//      "data":[
//          {"table": "ddister_03", "where": "",
//              "data": [
//                  {"column":"id_terminal","value":"","datatype":"varchar"},
////                  {"column":"name_terminal","value":"哈哈哈","datatype":"varchar"},
////                  {"column":"id_tertype","value":"20","datatype":"varchar"},
////                  {"column":"id_terattr","value":"02","datatype":"varchar"},
////                  {"column":"id_terlvl","value":"B","datatype":"varchar"},
////                  {"column":"flagdirect","value":"N","datatype":"varchar"},
////                  {"column":"id_dept","value":"01143","datatype":"varchar"},
////                  {"column":"id_zone","value":"1005","datatype":"varchar"},
////                  {"column":"id_corr","value":"","datatype":"varchar"},
////                  {"column":"id_salesman","value":"test2","datatype":"varchar"},
////                  {"column":"var_addr","value":"看见了咯","datatype":"varchar"},
////                  {"column":"var_contact","value":"哦哦哦","datatype":"varchar"},
////                  {"column":"var_tel","value":"摸JJ","datatype":"varchar"},
////                  {"column":"flag_sts","value":"L","datatype":"varchar"},
////                  {"column":"id_flow","value":"FBinv","datatype":"varchar"},
////                  {"column":"line_no","value":"1","datatype":"int"},
////                  {"column":"var_location","value":"北京市海淀区中关村大街11","datatype":"varchar"},
////                  {"column":"var_longi","value":"116.321861","datatype":"varchar"},
////                  {"column":"var_lati","value":"39.987346","datatype":"varchar"}
//              ]
//          }
//     ]
// }

    /**
     * 新增终端
     * @param userid
     * @param comid
     * @param id_terminal
     * @param name_terminal
     * @param id_tertype
     * @param id_terattr
     * @param id_terlvl
     * @param flagdirect
     * @param id_dept
     * @param id_zone
     * @param id_corr
     * @param id_salesman
     * @param var_addr
     * @param var_contact
     * @param var_tel
     * @param flag_sts
     * @param id_flow
     * @param line_no
     * @param var_location
     * @param var_longi
     * @param var_lati
     * @return
     */
    public static String getAddTerminalBuffer(String userid, String comid, String id_terminal,
                                              String name_terminal, String id_tertype,String id_terattr, 
                                              String id_terlvl, String flagdirect, String id_dept, 
                                              String id_zone, String id_corr, String id_salesman, 
                                              String var_addr, String var_contact, int var_tel, 
                                              String flag_sts, String id_flow,String line_no,
                                              String var_location,String var_longi,String var_lati) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{\"tableid\":\"ddister\",\"opr\":\"SS\",\"no\": \" \"," +
                "\"userid\":\" " + userid + " \",\"comid\":\" " + comid +" \"," +
                "\"data\":[" +
                "{\"table\": \"dgtdrec_03\",\"where\": \" \", " +
                "\"data\": [");
        stringBuffer.append("{\"column\":\"id_terminal\",\"value\":\" "+ id_terminal +" \",\"datatype\":\" varchar \"},");
        stringBuffer.append("{\"column\":\"name_terminal\",\"value\":\" "+ name_terminal +" \",\"datatype\":\" varchar \"},");
        stringBuffer.append("{\"column\":\"id_tertype\",\"value\":\" "+ id_tertype +"  \",\"datatype\":\" varchar \"},");
        stringBuffer.append("{\"column\":\"id_terattr\",\"value\":\" "+ id_terattr +"  \",\"datatype\":\" varchar \"},");
        stringBuffer.append("{\"column\":\"id_terlvl\",\"value\":\" "+ id_terlvl +"  \",\"datatype\":\" varchar \"},");
        stringBuffer.append("{\"column\":\"flagdirect\",\"value\":\" "+ flagdirect +"  \",\"datatype\":\" varchar \"},");
        stringBuffer.append("{\"column\":\"id_dept\",\"value\":\" "+ id_dept +"  \",\"datatype\":\" varchar \"},");
        stringBuffer.append("{\"column\":\"id_zone\",\"value\":\" "+ id_zone +"  \",\"datatype\":\" varchar \"},");
        stringBuffer.append("{\"column\":\"id_corr\",\"value\":\" "+ id_corr +"  \",\"datatype\":\" varchar \"},");
        stringBuffer.append("{\"column\":\"id_salesman \",\"value\":\" "+ id_salesman +"  \",\"datatype\":\" varchar \"},");
        stringBuffer.append("{\"column\":\"var_addr\",\"value\":\" "+ var_addr +"  \",\"datatype\":\" varchar \"},");
        stringBuffer.append("{\"column\":\"var_contact \",\"value\":\" "+ var_contact +"  \",\"datatype\":\" varchar \"},");
        stringBuffer.append("{\"column\":\"var_tel\",\"value\":\" "+ var_tel +" \",\"datatype\":\" varchar \"},");
        stringBuffer.append("{\"column\":\"flag_sts\",\"value\":\" "+ flag_sts +"  \",\"datatype\":\" varchar \"},");
        stringBuffer.append("{\"column\":\"id_flow\",\"value\":\" "+ id_flow +"  \",\"datatype\":\" varchar \"},");
        stringBuffer.append("{\"column\":\"line_no\",\"value\":\" "+ line_no +"  \",\"datatype\":\" varchar \"},");
        stringBuffer.append("{\"column\":\"var_location\",\"value\":\" "+ var_location +"  \",\"datatype\":\" varchar \"},");
        stringBuffer.append("{\"column\":\"var_longi\",\"value\":\" "+ var_longi +"  \",\"datatype\":\" varchar \"},");
        stringBuffer.append("{\"column\":\"var_lati\",\"value\":\" "+ var_lati +"  \",\"datatype\":\" varchar \"}");
        stringBuffer.append("]}]}");
      
        String str = stringBuffer.toString().trim();
        return str;
    }


    //business{"tableid":"dfeeoth","opr":"SS","no": "2018072318ONF3JS84","userid":"test2","comid":"CM1092-0001",
    // "data":[{"table": "dfeeoth_03", "where": "", "data": [
    // {"column":"id_fee","value":"10","datatype":"varchar"},
    // {"column":"date_app","value":"2018-07-23","datatype":"varchar"},
    // {"column":"var_remark","value":"测试","datatype":"varchar"},
    // {"column":"dec_buamt","value":"66","datatype":"varchar"},
    // {"column":"dec_amt","value":"66","datatype":"varchar"},
    // {"column":"int_attcount","value":"","datatype":"varchar"},
    // {"column":"id_employee","value":"test2","datatype":"varchar"},
    // {"column":"flag_sts","value":"L","datatype":"varchar"},
    // {"column":"id_flow","value":"FBinv","datatype":"varchar"},
    // {"column":"line_no","value":"1","datatype":"int"}
    // ]}]}

    /**
     * 报销单
     * @param noid
     * @param userID
     * @param companyID
     * @param id_fee
     * @param date_app
     * @param var_remark
     * @param dec_buamt
     * @param dec_amt
     * @param int_attcount
     * @param fiscal_period
     * @param fiscal_year
     * @param id_flow
     * @param line_no
     * @return
     */
    public static String getConsumSpendeBuffer(String noid, String userID, String companyID,
                                          String id_fee,String date_app, String var_remark,
                                          String dec_buamt, String dec_amt,String int_attcount,
                                          int fiscal_period, int fiscal_year, String id_flow,
                                          String line_no) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{\"tableid\":\"dfeeoth\",\"opr\":\"SS\",\"no\": \" " + noid + " \"," +
                "\"userid\":\"" + userID + "\"," + "\"comid\":\"" + companyID + " \"," +
                "\"data\":[{\"table\": \"dfeeoth_03\",\"where\": \"\", \"data\": [");
        stringBuffer.append("{\"column\":\" id_fee \",\"value\":\" " + id_fee + " \",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\" date_app \",\"value\":\" " + date_app + " \",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\" var_remark \",\"value\":\" " + var_remark + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\" dec_buamt \",\"value\":\"" + dec_buamt + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\" dec_amt \",\"value\":\"" + dec_amt + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\" int_attcount \",\"value\":\"" + int_attcount + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\" fiscal_period \",\"value\":\"" + fiscal_period + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\" fiscal_year \",\"value\":\"" + fiscal_year + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\" id_flow \",\"value\":\"" + id_flow + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\" line_no \",\"value\":\"" + line_no + "\",\"datatype\":\"varchar\"}");
        stringBuffer.append("]}]}");
        String str = stringBuffer.toString().trim();
        return str;
    }

}
