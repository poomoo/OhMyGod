package com.poomoo.ohmygod.database;

import org.litepal.crud.DataSupport;

/**
 * @author 李苜菲
 * @ClassName AreaInfo
 * @Description TODO 活动模型
 * @date 2015年8月16日 下午10:45:03
 */
public class ActivityInfo extends DataSupport {
    private int activeId;
    private boolean flag = false;//是否设置提醒

    public int getActiveId() {
        return activeId;
    }

    public void setActiveId(int activeId) {
        this.activeId = activeId;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
