package com.flyby_riders.Constants;

import com.flyby_riders.BuildConfig;

/**
 * Created by KRISHNENDU MANNA on 24,May,2020
 */
public interface StringUtils {
    String ACCOUNT_TYPE = "ACCOUNT TYPE", PHONE_NO = "PHONE NO", USER_ID = "USER ID", USER_DETAILS = "USER DETAULS",BASIC="BASIC" ,PREMIUM="PREMIUM";

    public int REQUEST_LOCATION = 199;
    String RIDE_STARTED = "RIDE_STARTED",RIDE_ENDED="RIDE_ENDED",RIDE_NOT_STARTED="RIDE_NOT_STARTED";

    String RIDE_PLAY="RIDE_PLAY",RIDE_PAUSE="RIDE_PAUSE";

    String RIDE_IS_BACKGROUND="IS_BACKGROUND",RIDE_IS_FORGROUND="IS_FORGROUND",RIDE_IS_CLOSE="IS_FORGROUND";

    String TASK_SEEN="Seen",TASK_CLICK="Click",TASK_PURCHASE="Purchase";

    String TASK_CONTACT_CLICK="Contact",TASK_VISIT="Visit";

    String WhatsappNumberBus="+919548582725";
    String PayKeyID= BuildConfig.PayKeyID;
    String LiveHeader=BuildConfig.PaymentHeader;
}