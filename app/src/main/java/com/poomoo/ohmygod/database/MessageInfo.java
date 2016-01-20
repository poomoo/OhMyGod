package com.poomoo.ohmygod.database;

import org.litepal.crud.DataSupport;

/**
 * @author 李苜菲
 * @ClassName AreaInfo
 * @Description TODO 消息模型
 * @date 2015年8月16日 下午10:45:03
 */
public class MessageInfo extends DataSupport {
    private int statementId;
    private boolean status = false;
    private int flag;//1-公共消息 2-站内信息

    public int getStatementId() {
        return statementId;
    }

    public void setStatementId(int statementId) {
        this.statementId = statementId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "statementId=" + statementId +
                ", status=" + status +
                ", flag=" + flag +
                '}';
    }
}
