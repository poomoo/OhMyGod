package com.poomoo.api;

import com.poomoo.model.AdBO;
import com.poomoo.model.CityBO;
import com.poomoo.model.CommodityBO;
import com.poomoo.model.FileBO;
import com.poomoo.model.GrabBO;
import com.poomoo.model.GrabResultBO;
import com.poomoo.model.MessageBO;
import com.poomoo.model.MessageInfoBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.ShowBO;
import com.poomoo.model.SignedBO;
import com.poomoo.model.StatementBO;
import com.poomoo.model.UserBO;
import com.poomoo.model.WinInformationBO;
import com.poomoo.model.WinnerBO;
import com.poomoo.model.WinningRecordsBO;
import com.poomoo.model.WithdrawDepositBO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Api实现类
 * 作者: 李苜菲
 * 日期: 2015/11/11 11:06.
 */
public class ApiImpl implements Api {

    private HttpEngine httpEngine;

    public ApiImpl() {
        httpEngine = HttpEngine.getInstance();
    }

    @Override
    public ResponseBO login(String phoneNum, String passWord) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.USERACTION);
        paramMap.put("method", Config.LOGIN);
        paramMap.put("tel", phoneNum);
        paramMap.put("password", passWord);

//        Type type = new TypeToken<ResponseBO<Void>>() {
//        }.getType();
        try {
            return httpEngine.postHandle(paramMap, UserBO.class);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO getCode(String phoneNum) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.USERACTION);
        paramMap.put("method", Config.CODE);
        paramMap.put("tel", phoneNum);


        try {
            return httpEngine.postHandle(paramMap, null);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO checkCode(String tel, String code) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.USERACTION);
        paramMap.put("method", Config.CHECK);
        paramMap.put("tel", tel);
        paramMap.put("code", code);

        try {
            return httpEngine.postHandle(paramMap, null);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO register(String phoneNum, String passWord, String code, String age, String sex, String channelId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.USERACTION);
        paramMap.put("method", Config.REGISTER);
        paramMap.put("tel", phoneNum);
        paramMap.put("password", passWord);
        paramMap.put("code", code);
        paramMap.put("age", age);
        paramMap.put("sex", sex);
        paramMap.put("channelId", channelId);
        paramMap.put("deviceType", "1");//--设备类型，1android，2ios，3PC(参数非空)

        try {
            return httpEngine.postHandle(paramMap, null);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO<AdBO> getAdvertisement(String cityName) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.PUBACTION);
        paramMap.put("method", Config.AD);
        paramMap.put("cityName", cityName);

        try {
            return httpEngine.postHandle(paramMap, AdBO.class);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO<GrabBO> getGrabList(String cityName) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.ACTIVITYACTION);
        paramMap.put("method", Config.ACTIVITYLIST);
        paramMap.put("cityName", cityName);

        try {
            return httpEngine.postHandle(paramMap, GrabBO.class);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO<CommodityBO> getCommodityInformation(String userId, String activeId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.ACTIVITYACTION);
        paramMap.put("method", Config.ACTIVITY);
        paramMap.put("userId", userId);
        paramMap.put("activeId", activeId);

        try {
            return httpEngine.postHandle(paramMap, CommodityBO.class);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO<GrabResultBO> putGrabInfo(String activeId, String userId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.ACTIVITYACTION);
        paramMap.put("method", Config.SUBMIT);
        paramMap.put("activeId", activeId);
        paramMap.put("userId", userId);

        try {
            return httpEngine.postHandle(paramMap, GrabResultBO.class);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO<WinnerBO> getWinnerList(String cityName) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.ACTIVITYACTION);
        paramMap.put("method", Config.WINNERLIST);
        paramMap.put("cityName", cityName);

        try {
            return httpEngine.postHandle(paramMap, WinnerBO.class);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO<Void> uploadPics(FileBO fileBO) {
        try {
            return httpEngine.uploadFile(fileBO);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO<Void> putPersonalInfo(String userId, String realName, String idCardNum, String address) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.USERACTION);
        paramMap.put("method", Config.PERSONALINFO);
        paramMap.put("userId", userId);
        paramMap.put("realName", realName);
        paramMap.put("idCardNum", idCardNum);
        paramMap.put("address", address);
//        paramMap.put("idFrontPic", idFrontPic);
//        paramMap.put("idOpsitePic", idOpsitePic);
        try {
            return httpEngine.postHandle(paramMap, null);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO changePersonalInfo(String userId, String key, String value) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.USERACTION);
        paramMap.put("method", Config.CHANGE);
        paramMap.put("userId", userId);
        paramMap.put("key", key);
        paramMap.put(key, value);

        try {
            return httpEngine.postHandle(paramMap, null);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO<Void> toSigned(String userId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.USERACTION);
        paramMap.put("method", Config.SIGNED);
        paramMap.put("userId", userId);

        try {
            return httpEngine.postHandle(paramMap, null);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO<SignedBO> getSignedList(String userId, String year, String month) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.USERACTION);
        paramMap.put("method", Config.SIGNEDLIST);
        paramMap.put("userId", userId);
        paramMap.put("year", year);
        paramMap.put("month", month);

        try {
            return httpEngine.postHandle(paramMap, SignedBO.class);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO<StatementBO> getStatement(String type) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.PUBACTION);
        paramMap.put("method", Config.STATEMENT);
        paramMap.put("type", type);

        try {
            return httpEngine.postHandle(paramMap, StatementBO.class);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO getWinningList(String userId, String flag, int currPage, int pageSize) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.ACTIVITYACTION);
        paramMap.put("method", Config.RECORDS);
        paramMap.put("userId", userId);
        paramMap.put("flag", flag);
        paramMap.put("currPage", currPage + "");
        paramMap.put("pageSize", pageSize + "");

        try {
            return httpEngine.postHandle(paramMap, WinningRecordsBO.class);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO getMyWithdrawDepositList(String userId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.USERACTION);
        paramMap.put("method", Config.WITHDRAWDEPOSITLIST);
        paramMap.put("userId", userId);

        try {
            return httpEngine.postHandle(paramMap, WithdrawDepositBO.class);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO putShow(String userId, String activeId, String content, String pictures) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.SHOWANDSHARE);
        paramMap.put("method", Config.SHOW);
        paramMap.put("userId", userId);
        paramMap.put("activeId", activeId);
        paramMap.put("content", content);
        paramMap.put("pictures", pictures);

        try {
            return httpEngine.postHandle(paramMap, WithdrawDepositBO.class);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO getShowList(int flag, String userId, int currPage, int pageSize) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.SHOWANDSHARE);
        paramMap.put("method", Config.SHOWLIST);
        paramMap.put("userId", userId);
        paramMap.put("flag", flag + "");
        paramMap.put("currPage", currPage + "");
        paramMap.put("pageSize", pageSize + "");

        try {
            return httpEngine.postHandle(paramMap, ShowBO.class);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO putComment(String userId, String content, String dynamicId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.SHOWANDSHARE);
        paramMap.put("method", Config.COMMENT);
        paramMap.put("userId", userId);
        paramMap.put("content", content);
        paramMap.put("dynamicId", dynamicId);

        try {
            return httpEngine.postHandle(paramMap, null);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO putReply(String fromUserId, String toUserId, String content, String commentId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.SHOWANDSHARE);
        paramMap.put("method", Config.REPLY);
        paramMap.put("fromUserId", fromUserId);
        paramMap.put("toUserId", toUserId);
        paramMap.put("content", content);
        paramMap.put("commentId", commentId);

        try {
            return httpEngine.postHandle(paramMap, null);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO withDrawDeposit(String userId, String drawFee) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.USERACTION);
        paramMap.put("method", Config.WITHDRAWDEPOSIT);
        paramMap.put("userId", userId);
        paramMap.put("drawFee", drawFee);


        try {
            return httpEngine.postHandle(paramMap, null);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO getUserInfo(String userId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.USERACTION);
        paramMap.put("method", Config.SYNC);
        paramMap.put("userId", userId);

        try {
            return httpEngine.postHandle(paramMap, UserBO.class);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO getWithDrawDepositFee(String userId, String drawFee) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.USERACTION);
        paramMap.put("method", Config.FEE);
        paramMap.put("userId", userId);
        paramMap.put("drawFee", drawFee);

        try {
            return httpEngine.postHandle(paramMap, null);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO getMessages(String type, int currPage, int pageSize) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.PUBACTION);
        paramMap.put("method", Config.STATEMENT);
        paramMap.put("type", type);
        paramMap.put("currPage", currPage + "");
        paramMap.put("pageSize", pageSize + "");

        try {
            return httpEngine.postHandle(paramMap, MessageBO.class);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO getMessageInfo(String statementId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.PUBACTION);
        paramMap.put("method", Config.INFO);
        paramMap.put("statementId", statementId);

        try {
            return httpEngine.postHandle(paramMap, MessageInfoBO.class);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO changePassWord(String tel, String password) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.USERACTION);
        paramMap.put("method", Config.PASSWORD);
        paramMap.put("tel", tel);
        paramMap.put("password", password);

        try {
            return httpEngine.postHandle(paramMap, null);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO putFeedBack(String userId, String content, String contact) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.PUBACTION);
        paramMap.put("method", Config.FEEDBACK);
        paramMap.put("userId", userId);
        paramMap.put("content", content);
        paramMap.put("contact", contact);

        try {
            return httpEngine.postHandle(paramMap, null);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO getCitys() {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.PUBACTION);
        paramMap.put("method", Config.CITYS);

        try {
            return httpEngine.postHandle(paramMap, CityBO.class);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO getWinningInfo(String cityName, int currPage, int pageSize) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.ACTIVITYACTION);
        paramMap.put("method", Config.WININFOLIST);
        paramMap.put("cityName", cityName);
        paramMap.put("currPage", currPage + "");
        paramMap.put("pageSize", pageSize + "");
        try {
            return httpEngine.postHandle(paramMap, WinInformationBO.class);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }
}
