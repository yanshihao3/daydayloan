package com.day.loan.net;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/5/23 下午3:18
 * - @Email whynightcode@gmail.com
 */
public interface HttpUrl {
     String HOST="http://api.anwenqianbao.com/";

    /**banner **/
    String BANNER="v2/vest/banner";
    /**产品 **/
    String PRODUCT_LSIT="v2/vest/product";
    String PRODUCT_HOT="v2/vest/hotProduct";
    String PRODUCT_TOP="v2/vest/topProduct";

    /**福利 **/
    String WELFARE="v2/vest/welfare";

    interface   LOGIN{
        /** 新or老用户**/
        String isOldUser="quick/isOldUser";
        /** 验证码获取**/
        String CODE="sms/getcode";
        /** 验证码效验**/
        String CHECKCODE="sms/checkCode";
        /** 登陆**/
        String QUICKLOGIN="quick/login";
        /** 完善信息**/
        String IDENTITY ="quick/addBasicIdentity";

    }

    interface  STATUS{
        /** 状态**/
        String getStatus="vest/getStatus";
        /**版本更新**/
        String UPDATE="vest/version";
    }}
