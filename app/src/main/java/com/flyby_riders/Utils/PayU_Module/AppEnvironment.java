package com.flyby_riders.Utils.PayU_Module;

import com.flyby_riders.BuildConfig;

/**
 * Created by KRISHNENDU MANNA  00/00/2020
 */
public enum AppEnvironment {


    SANDBOX {
        @Override
        public String merchant_Key() {
            return BuildConfig.MerchantKey_PayU;
        }

        @Override
        public String merchant_ID() {
            return BuildConfig.MerchantID;
        }

        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            return BuildConfig.MerchantSalt_PayU;
        }

        @Override
        public boolean debug() {
            return true;
        }
    },
    PRODUCTION {
        @Override
        public String merchant_Key() {
            return BuildConfig.MerchantKey_PayU;
        }
        @Override
        public String merchant_ID() {
            return BuildConfig.MerchantID;
        }
        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            return BuildConfig.MerchantSalt_PayU;
        }

        @Override
        public boolean debug() {
            return false;
        }
    };

    AppEnvironment() {
    }

    public abstract String merchant_Key();

    public abstract String merchant_ID();

    public abstract String furl();

    public abstract String surl();

    public abstract String salt();

    public abstract boolean debug();


}
