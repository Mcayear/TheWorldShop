package org.badfish.theworldshop.items;

import cn.nukkit.item.Item;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.TextFormat;
import org.badfish.theworldshop.TheWorldShopMainClass;
import org.badfish.theworldshop.utils.Tool;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author BadFish
 */
public class ShopItem  {

    private final Item shopItem;

    /**
     * 商品唯一标识
     * */
    public UUID uuid;

    private String sellPlayer;

    public final static String TAG = "Shop_Item_";

    private double sellMoney;

    private final Item defaultItem;

    private final MoneySellItem.MoneyType moneyType;

    /**
     * 限购
     * 0代表不限
     * */
    public int limit;

    private boolean isRemove = false;


    private ShopItem(UUID uuid, Item defaultItem, String sellPlayer, MoneySellItem.MoneyType moneyType, double sellMoney) {
        this.defaultItem = defaultItem.clone();
        this.shopItem = defaultItem.clone();
        this.moneyType = moneyType;
        this.sellPlayer = sellPlayer;
        this.sellMoney = sellMoney;
        this.uuid = uuid;
    }

    private static ArrayList<String> asList(String[] s){
        ArrayList<String> strings = new ArrayList<>();
        if(s.length > 0){
            for(String s1: s){
                strings.add(s1+"");
            }
        }
        return strings;
    }

    public boolean isRemove() {
        return isRemove;
    }

    public MoneySellItem.MoneyType getMoneyType() {
        return moneyType;
    }


    public void setRemove(boolean remove) {
        isRemove = remove;
    }

    public static ShopItem formMap(Map<?,?> map){
        try {
            Item i;
            if(map.containsKey("item")){
                i = NBTIO.getItemHelper(NBTIO.read(Base64.getDecoder().decode((String) map.get("item"))));
            }else{
                i = Item.fromString(map.get("id").toString());
                i.setCount(Integer.parseInt(map.get("count").toString()));
                String tag = map.get("tag").toString();

                if (!"not".equalsIgnoreCase(tag)) {
                    byte[] tagForm = Tool.hexStringToBytes(tag);
                    if(tagForm != null){
                        i.setCompoundTag(Item.parseCompoundTag(tagForm));
                    }
                }
            }
            //过滤空气
            if(i.getId() == 0){
                return null;
            }

            UUID uuid = UUID.randomUUID();
            if(map.containsKey("uuid")){
                uuid = UUID.fromString(map.get("uuid").toString());
            }
            int limit = -1;
            if(map.containsKey("limit")) {
                limit = Integer.parseInt(map.get("limit").toString());
            }
            String sellPlayer = map.get("sellPlayer").toString();
            boolean remove = false;
            if(map.containsKey("isRemove")){
                remove = (Boolean) map.get("isRemove");
            }
            double sellMoney = Double.parseDouble(map.get("sellMoney").toString());
            MoneySellItem.MoneyType type = MoneySellItem.MoneyType.EconomyAPI;
            if(map.containsKey("moneyType")){
                type = MoneySellItem.MoneyType.valueOf(map.get("moneyType").toString());
            }
            return cloneTo(uuid,i, sellPlayer,type, sellMoney,remove,limit);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static ShopItem getShopItemByItem(Item item){
        CompoundTag tag = item.getNamedTag();
        if(tag != null){
            if(tag.contains(TAG+"tag") && tag.getString(TAG+"type").equalsIgnoreCase(ItemType.SELL.getName())){
                Item def = Item.get(item.getId(),item.getDamage());
                def.setCount(item.getCount());
                if(tag.contains(TAG+"defaultItem")){
                    byte[] tagForm = Tool.hexStringToBytes(tag.getString(TAG+"defaultItem"));
                    if(tagForm != null){
                        def.setNamedTag(Item.parseCompoundTag(tagForm));
                    }
                }
                UUID uuid = UUID.randomUUID();
                if(tag.contains(TAG+"uuid")){
                    uuid = UUID.fromString(tag.getString(TAG+"uuid"));
                }
                int limit = -1;
                if(tag.contains(TAG+"limit")) {
                    limit = tag.getInt(TAG+"limit");
                }

                ShopItem item1 = new ShopItem(uuid,def,tag.getString(TAG+"player"), MoneySellItem.MoneyType.valueOf(tag.getString(TAG+"moneyType")),tag.getDouble(TAG+"money"));
                item1.setRemove(tag.getBoolean(TAG+"isRemove"));
                item1.limit = limit;
                return item1;
            }
        }
        return null;
    }

    public Item toItem(String displayPlayerName){
        ArrayList<String> lore = new ArrayList<>(Arrays.asList(shopItem.getLore()));
        for(int i = 0;i < lore.size();i++){
            lore.set(i,lore.get(i).replace("${playerLimitCount}",TheWorldShopMainClass.PLAYER_DATA.getBuyCount(displayPlayerName,this)+""));
        }
        shopItem.setLore(lore.toArray(new String[0]));
        return shopItem;
    }

    public static ShopItem cloneTo(UUID uuid,Item defaultItem, String sellPlayer, MoneySellItem.MoneyType moneyType, double sellMoney, boolean isRemove,int limit){
        ShopItem item = new ShopItem(uuid,defaultItem,null,moneyType,0);
        item.setSellMoney(sellMoney);
        item.limit = limit;
        item.setSellPlayer(sellPlayer);
        item.setRemove(isRemove);
        String[] loreItem =  item.shopItem.getLore();
        ArrayList<String> lore = new ArrayList<>();
        if(loreItem.length > 0){
            lore = asList(loreItem);
        }
        double m = 0;

        lore.add(TextFormat.colorize('&',"&r&7■■■■■■■■■■■■■■■■■■■■"));

        lore.add(TextFormat.colorize('&',"&r&e"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.shopItemLore1,sellPlayer)));
        if(isRemove){
            lore.add(TextFormat.colorize('&',"&r&d"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.shopItemLore2)));
            lore.add(TextFormat.colorize('&',"&r&e"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.shopItemLore3NoTax,sellMoney,TheWorldShopMainClass.WORLD_CONFIG.getMoneyTypeName(moneyType))));
        }else{
            if(TheWorldShopMainClass.WORLD_CONFIG.getTax() > 0){
                lore.add(TextFormat.colorize('&',"&r&e"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.shopItemLore3,sellMoney)));
            }else{
                lore.add(TextFormat.colorize('&',"&r&e"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.shopItemLore3NoTax,sellMoney,TheWorldShopMainClass.WORLD_CONFIG.getMoneyTypeName(moneyType))));
            }

        }
        if(item.limit > 0){
            lore.add(TextFormat.colorize('&',"&r&e"+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.shopItemLore5,"${playerLimitCount}",limit)));
        }

        lore.add(TextFormat.colorize('&',""));
        lore.add(TextFormat.colorize('&',"&r&l&a      "+TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.shopItemLore6)));
        lore.add(TextFormat.colorize('&',"&r&7■■■■■■■■■■■■■■■■■■■■"));

        item.shopItem.setLore(lore.toArray(new String[0]));
        CompoundTag tag =  item.shopItem.getNamedTag();

        tag.putString(TAG+"tag","ShopItem");
        tag.putString(TAG+"type","sell");
        tag.putString(TAG+"uuid",uuid.toString());
        tag.putInt(TAG+"limit",limit);
        tag.putString(TAG+"player",sellPlayer);
        tag.putDouble(TAG+"money",sellMoney);
        tag.putString(TAG+"moneyType",moneyType.name());
        tag.putBoolean(TAG+"isRemove",isRemove);
        if(defaultItem.hasCompoundTag()) {
            String b = Tool.bytesToHexString(defaultItem.getCompoundTag());
            if(b != null) {
                tag.putString(TAG + "defaultItem", b);
            }
        }
        item.shopItem.setCompoundTag(tag);
        return item;
    }

    private void setSellPlayer(String sellPlayer) {
        this.sellPlayer = sellPlayer;
    }

    private void setSellMoney(double sellMoney) {
        this.sellMoney = sellMoney;
    }

    public String getSellPlayer() {
        return sellPlayer;
    }

    public double getSellMoney() {
        return sellMoney;
    }

    public Item getDefaultItem() {
        return defaultItem;
    }

    public int getId(){
        return defaultItem.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ShopItem){
            if(((ShopItem) obj).defaultItem.equals(defaultItem,true,true)){
                if(sellPlayer.equalsIgnoreCase(((ShopItem) obj).sellPlayer)){
                    return (int)Math.ceil(sellMoney) == (int)Math.ceil(((ShopItem) obj).sellMoney);
                }
            }
        }
        return false;
    }
}
