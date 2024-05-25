package org.badfish.theworldshop.configs;

import cn.nukkit.utils.Config;
import org.badfish.theworldshop.items.MoneySellItem;
import org.badfish.theworldshop.language.BaseLanguage;
import org.badfish.theworldshop.language.LanguageManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author BadFish
 */
public class TheWorldShopConfig {
    private double tax;
    private BaseLanguage language;
    private String title;
    private double moneyMin;
    private double moneyMax;
    private int playerSellMax;
    private Map<String, String> moneyName;
    private boolean limitTradableEnable = false;
    private boolean limitTradableAllowNoTag = false;
    private boolean limitTradableAllowTaskbook = false;
    private ArrayList<String> limitTradableBanItemList = new ArrayList();

    private TheWorldShopConfig(String title, double tax, int playerSellMax, double moneyMin, double moneyMax, Map<String, String> moneyName, Map<String, Object> limitTradable, String lang) {
        this.title = title;
        this.moneyMax = moneyMax;
        this.moneyMin = moneyMin;
        this.playerSellMax = playerSellMax;
        this.tax = tax;
        this.moneyName = moneyName;
        this.limitTradableEnable = (Boolean)limitTradable.getOrDefault("enable", false);
        this.limitTradableAllowNoTag = (Boolean)limitTradable.getOrDefault("allow-no-tag", false);
        this.limitTradableAllowTaskbook = (Boolean)limitTradable.getOrDefault("allow-taskbook", false);
        this.limitTradableBanItemList = (ArrayList)limitTradable.get("ban-item-list");
        this.language = LanguageManager.getLanguage(lang);
    }

    public double getMoneyMax() {
        return this.moneyMax;
    }

    public double getMoneyMin() {
        return this.moneyMin;
    }

    public static TheWorldShopConfig load(Config config) {
        return new TheWorldShopConfig(config.getString("title", "交易行"), config.getDouble("tax", 0.2), config.getInt("player-sell-max", 10), config.getDouble("sell-money-min", 0.1), config.getDouble("sell-money-max", 10000.0), (Map)config.get("money-name", new LinkedHashMap<String, String>() {
            {
                this.put("EconomyAPI", "&e金币");
                this.put("PlayerPoints", "&d点券");
                this.put("Money", "&2金钱");
            }
        }), (Map)config.get("limit-tradable", new LinkedHashMap<String, Object>() {
            {
                this.put("enable", false);
                this.put("allow-no-tag", false);
                this.put("allow-taskbook", false);
                this.put("ban-item-list", new ArrayList());
            }
        }), config.getString("lang", "chs"));
    }

    public String getMoneyTypeName(MoneySellItem.MoneyType moneyType) {
        return this.moneyName.containsKey(moneyType.name()) ? (String)this.moneyName.get(moneyType.name()) : "&e金币";
    }

    public double getTax() {
        return this.tax;
    }

    public BaseLanguage getLanguage() {
        return this.language;
    }

    public String getTitle() {
        return this.title;
    }

    public int getPlayerSellMax() {
        return this.playerSellMax;
    }

    public boolean isLimitTradableEnable() {
        return this.limitTradableEnable;
    }

    public boolean isLimitTradableAllowNoTag() {
        return limitTradableAllowNoTag;
    }

    public boolean isLimitTradableAllowTaskbook() {
        return limitTradableAllowTaskbook;
    }

    public ArrayList<String> getLimitTradableBanItemList() {
        return this.limitTradableBanItemList;
    }
}
