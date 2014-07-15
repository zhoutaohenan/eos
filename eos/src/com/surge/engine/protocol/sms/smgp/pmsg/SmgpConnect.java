/*
 * Title: mas20
 * @author Administrator
 * Created on 2008-5-19 
 */

package com.surge.engine.protocol.sms.smgp.pmsg;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import com.surge.engine.protocol.sms.cmpp.pmsg.Common;
import com.surge.engine.util.AppendUtils;
import com.surge.engine.util.StringUtils;
import com.surge.engine.util.Tools;
///
public class SmgpConnect extends SmgpSendObject {
	private static final long serialVersionUID = 8578077277359333385L;
	private String source_addr;
    private byte[] md5sp;
    private byte version = (byte) 0x13;
    private int timestamp;
    private byte result = 0;

    public SmgpConnect(String source_addr, String pwd, byte version) throws NoSuchAlgorithmException {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        timestamp = month * 100000000 + day * 1000000 + hour * 10000 +
            minute * 100 + second;
        String datetime = "" + timestamp;
        if (month < 10) {
            datetime = "0" + datetime;
        }

//       // AppendTools appendTools = new AppendTools(100);
//        IoBuffer buffer=IoBuffer.allocate(100);
//        //appendTools.appendString(source_addr);
//        buffer.putString(source_addr, encoder);
//        //appendTools.appendString("", 7);
//        buffer.putString("", 7, encoder);
//      //  appendTools.appendString(StringUtils.decodeBase64(pwd));
//        buffer.putString(StringUtils.decodeBase64(pwd), encoder);
//       // appendTools.appendString(datetime);
//        md5sp = Tools.md5Encode(getBytesFromBuffer(buffer));
//
//        //appendTools.reset();
//        buffer.flip();
//       // appendTools.appendString(source_addr, 8);
//        buffer.
//        appendTools.appendBytes(md5sp, 16);
//        appendTools.appendByte( (byte) 2);
//        appendTools.appendInt( (int) timestamp);
//        appendTools.appendByte(this.version);
//
//        body = appendTools.getOutBytes();
//
//        head = new SmgpMsgHead(body.length, Common.SMGP_CONNECT, Tools.getSeqId());
        
        
        
    	AppendUtils appendUtils = new AppendUtils(100);
		appendUtils.appendString(source_addr);
		appendUtils.appendString("", 7);
		appendUtils.appendString(pwd);
		appendUtils.appendString(datetime);

		md5sp = Tools.md5Encode(appendUtils.getOutBytes());

		appendUtils.reset();
		appendUtils.appendString(source_addr, 8);
		appendUtils.appendBytes(md5sp, 16);
		appendUtils.appendByte((byte)2);
		appendUtils.appendInt((int) timestamp);
		appendUtils.appendByte(version);
		body = appendUtils.getOutBytes();

		head = new SmgpMsgHead(body.length, Common.SMGP_CONNECT, Tools.getSeqId());
    }

//    public SmgpConnect(byte[] bys) {
//        SubtractTools t2 = new SubtractTools(bys, 0);
//        source_addr = t2.getString(6).trim();
//        md5sp = t2.getBytes(16);
//        version = t2.getByte();
//        timestamp = t2.getInt();
//    }
//    public byte getResult(byte[] pwd) throws NoSuchAlgorithmException{
//        //md5验证
//         String tt = "" + timestamp;
//         if (tt.length() < 10) {
//             tt = "0" + tt;
//         }
////         byte[] pwd = SmgpServer.getCorpPwd(source_addr);
//         MessageDigest md5 = MessageDigest.getInstance("MD5");
//         md5.update(source_addr.getBytes());
//         md5.update(new byte[9]);
//         if (pwd != null) {
//             md5.update(pwd);
//         }
//         md5.update(tt.getBytes());
//         byte[] md5out = md5.digest();
//
//         for (int i = 0; i < 16; i++) {
//             if (md5sp[i] != md5out[i]) {
//                 result = (byte) 3;
//                 break;
//             }
//         }
//
//        return result;
//   }
//
//    public String getSourceAddr() {
//        return source_addr;
//    }
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append("source_addr:");
        sb.append(source_addr);
        sb.append(" md5:");
        sb.append(new String(md5sp));
        sb.append(" version:");
        sb.append(version);
        sb.append(" timestamp:");
        sb.append(timestamp);
        sb.append(" result:");
        sb.append(result);

        return sb.toString();
    }
}