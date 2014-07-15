package com.surge.engine.protocol.sms.smgp.pmsg;

import java.io.UnsupportedEncodingException;

import org.apache.mina.core.buffer.IoBuffer;

import com.surge.engine.util.SubtractTools;
import com.surge.engine.util.Tools;

public class SmgpDeliver extends SmgpSendObject {

    private static final long serialVersionUID = 9036278482708144895L;

    private int ismg_id;

    private String msg_id;

    private String dest_id;

    private String service_id;

    private byte tp_pid;

    private byte tp_udhi;

    private byte msg_fmt;

    private String src_id;

    private byte registered_delivery;

    private byte[] msg_content = null;

    private String reserved;

    private String content = "";

    private int totalCout = 0;

    private int currCout = 0;

    private long currTime;

    private String[] longContent;

    public SmgpDeliver(IoBuffer bys) {
        content = "";
        try {
            this.head = new SmgpMsgHead(bys);
            msg_id = Tools.bcd2Str(bys.getString(10, decoder).getBytes());
            registered_delivery = bys.get();
            msg_fmt = bys.get();
            bys.getString(14, decoder);
            src_id = bys.getString(21, decoder).trim();
            dest_id = bys.getString(21, decoder).trim();
            int msgLength = bys.get();
            if (msgLength < 0) {
                msgLength = msgLength + 256;
            }
            msg_content = new byte[msgLength];
            getContent(bys, msg_content);
            if (src_id.startsWith("+86")) {
                src_id = src_id.substring(3);
            } else if (src_id.startsWith("86")) {
                src_id = src_id.substring(2);
            }
            byte[] buff = null;

            if (msg_content != null && msg_content.length >= 3) {
                if (msg_content[0] == 5 && msg_content[1] == 0 && msg_content[2] == 3) {
                    totalCout = msg_content[4]; // 总条数
                    currCout = msg_content[5]; // 当前数
                    buff = new byte[msg_content.length - 6];
                    System.arraycopy(msg_content, 6, buff, 0, msg_content.length - 6);
                    msg_content = buff;
                }
            }
            if (msg_fmt == 8 || msg_fmt == 25) {
                if (msg_content == null) {
                    content = "";
                } else {
                    content = new String(msg_content, "UTF-16BE");
                }
            } else if (msg_fmt == 15) {
                if (msg_content == null) {
                    content = "";
                } else {
                    content = new String(msg_content, "GBK");
                }
            } else if (msg_fmt == 4) {
                if (msg_content == null) {
                    content = "";
                } else {
                    content = binary2Hex2(msg_content);
                }
            } else {
                if (msg_content == null) {
                    content = "";
                } else {
                    content = new String(msg_content);
                }
            }
        } catch (Exception exception) {
            content = "" + exception;
        }

    }

    public SmgpDeliver(byte[] body, int seq_id) {
        this.head = new SmgpMsgHead();
        head.setSeqID(seq_id);
        content = "";
        try {
            SubtractTools tool = new SubtractTools(body, 0);
            msg_id = Tools.bcd2Str(tool.getBytes(10));
            registered_delivery = tool.getByte();
            msg_fmt = tool.getByte();
            tool.getString(14);
            src_id = tool.getString(21).trim();
            dest_id = tool.getString(21).trim();
            int msgLength = tool.getByte();
            if (msgLength < 0) {
                msgLength = msgLength + 256;
            }
            msg_content = new byte[msgLength];
            msg_content = tool.getBytes(msg_content.length);
            if (src_id.startsWith("+86")) {
                src_id = src_id.substring(3);
            } else if (src_id.startsWith("86")) {
                src_id = src_id.substring(2);
            }
            byte[] buff = null;

            if (msg_content != null && msg_content.length >= 3) {
                if (msg_content[0] == 5 && msg_content[1] == 0 && msg_content[2] == 3) {
                    totalCout = msg_content[4]; // 总条数
                    currCout = msg_content[5]; // 当前数
                    buff = new byte[msg_content.length - 6];
                    System.arraycopy(msg_content, 6, buff, 0, msg_content.length - 6);
                    msg_content = buff;
                }
            }
            if (msg_fmt == 8 || msg_fmt == 25) {
                if (msg_content == null) {
                    content = "";
                } else {
                    content = new String(msg_content, "UTF-16BE").trim();
                }
            } else if (msg_fmt == 15) {
                if (msg_content == null) {
                    content = "";
                } else {
                    content = new String(msg_content, "GBK").trim();
                }
            } else if (msg_fmt == 4) {
                if (msg_content == null) {
                    content = "";
                } else {
                    content = binary2Hex2(msg_content);
                }
            } else if (msg_fmt == 0) {
                content = new String(msg_content, "ISO8859_1").trim();
            }

            else {
                if (msg_content == null) {
                    content = "";
                } else {
                    content = new String(msg_content, "GBK").trim();
                }
            }
        } catch (Exception exception) {
            content = "" + exception;
        }

    }

    public boolean isReport() {
        return (registered_delivery == 1);
    }

    public SmgpReport getReport() {
        if (registered_delivery != 1 || msg_content == null) {
            return null;
        } else {
            int pos = content.indexOf("id:");
            String tmp_report_msg_id = content.substring(pos + 3, content.length());
            byte[] report_content = null;
            try {
                if (msg_fmt == 0) {
                    report_content = tmp_report_msg_id.getBytes("ISO8859_1");
                } else if (msg_fmt == 8 || msg_fmt == 25) {
                    report_content = tmp_report_msg_id.getBytes("UTF-16BE");
                } else {
                    report_content = tmp_report_msg_id.getBytes("GBK");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            SmgpReport StatRpt = new SmgpReport(src_id, report_content);
            return StatRpt;
        }
    }

    public String getSmsContent() {
        return content;
    }

    public String getDestId() {
        return dest_id;
    }

    public String getMsgId() {
        return msg_id;
    }

    public String getSrcTerminalId() {
        return src_id;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(200);
        sb.append(" SmgpDeliver[");
        sb.append(" msg_id:");
        sb.append(msg_id);
        sb.append(" dest:");
        sb.append(dest_id);
        sb.append(" src:");
        sb.append(src_id);
        sb.append(" content:");
        sb.append(content);
        sb.append("]");
        return sb.toString();
    }

    public static String binary2Hex2(byte[] bys) {
        if (bys == null || bys.length < 1) {
            return null;
        }

        StringBuffer sb = new StringBuffer(100);

        for (int i = 0; i < bys.length; i++) {
            if (bys[i] >= 16) {
                sb.append(Integer.toHexString(bys[i]));
            } else if (bys[i] >= 0) {
                sb.append("0" + Integer.toHexString(bys[i]));
            } else {
                sb.append(Integer.toHexString(bys[i]).substring(6, 8));
            }
        }

        return sb.toString();
    }

    public int getIsmg_id() {
        return ismg_id;
    }

    public void setIsmg_id(int ismg_id) {
        this.ismg_id = ismg_id;
    }

    public int getTotalCout() {
        return totalCout;
    }

    public void setTotalCout(int totalCout) {
        this.totalCout = totalCout;
    }

    public int getCurrCout() {
        return currCout;
    }

    public void setCurrCout(int currCout) {
        this.currCout = currCout;
    }

    public long getCurrTime() {
        return currTime;
    }

    public void setCurrTime(long currTime) {
        this.currTime = currTime;
    }

    public int addCurrCout() {
        return ++currCout;
    }

    public void setSmsContent(String content) {
        this.content = content;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public byte getMsg_fmt() {
        return msg_fmt;
    }

    public void setMsg_fmt(byte msg_fmt) {
        this.msg_fmt = msg_fmt;
    }

    public String getDest_id() {
        return dest_id;
    }

    public void setDest_id(String dest_id) {
        this.dest_id = dest_id;
    }

    private void getContent(IoBuffer buffer, byte[] conents) {
        for (int i = 0; i < conents.length; i++) {
            conents[i] = buffer.get();
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getLongContent() {
        return longContent;
    }

    public void setLongContent(String[] longContent) {
        this.longContent = longContent;
    }

}