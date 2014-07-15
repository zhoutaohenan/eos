/*
 * Title: mas20
 * @author Administrator
 * Created on 2008-5-19 
 */

package com.surge.engine.protocol.sms.smgp.pmsg;

import com.surge.engine.protocol.sms.util.SmsTools;
import com.surge.engine.util.SubtractTools;
import com.surge.engine.util.Tools;

public class SmgpReport extends SmgpSendObject {
    private static final long serialVersionUID = 7069323513166604164L;

    private String msg_id;

    private String stat;

    private String submit_time;

    /**
     * YYMMDDHHMM
     */
    private String done_time;

    private String src_id;

    private String dest_id;

    private int result;

    private String err;

    public SmgpReport(String src_id, byte[] bys) {
        this.dest_id = src_id;

        SubtractTools tool = new SubtractTools(bys, 0);
        byte[] msgidA = tool.getBytes(10);
        // System.arraycopy(tool.getBytes(13), 3, msgidA, 0, 10);
        // msg_id = Tools.bcd2Str(tool.getString(13).substring(3).getBytes());

        msg_id = Tools.bcd2Str(msgidA);
        tool.getString(18);
        submit_time = tool.getString(23).substring(13).trim();
        done_time = tool.getString(21).substring(11).trim();
        stat = tool.getString(13).substring(5).trim();
        err = tool.getString(8).substring(5);
        result = SmsTools.getSmgpReportResult(stat);
    }

    public String getDoneTime() {
        return done_time;
    }

    public String getMsgId() {
        return msg_id;
    }

    public int getResult() {
        return result;
    }

    public String getSrcId() {
        return src_id;
    }

    public String getStat() {
        return stat;
    }

    public String getSubmitTime() {
        return submit_time;
    }

    public String getErr() {
        return err;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(" SmgpReport[");
        sb.append(" msg_id:");
        sb.append(msg_id);
        sb.append(" result:");
        sb.append(result);
        sb.append(" stat:");
        sb.append(stat);
        sb.append("]");
        sb.append(" destid:");
        sb.append(dest_id);
        sb.append("]");
        return sb.toString();
    }

    public String getDest_id() {
        return dest_id;
    }

    public void setDest_id(String dest_id) {
        this.dest_id = dest_id;
    }

}
