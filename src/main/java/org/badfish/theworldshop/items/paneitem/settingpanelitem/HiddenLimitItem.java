package org.badfish.theworldshop.items.paneitem.settingpanelitem;

import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;
import org.badfish.theworldshop.TheWorldShopMainClass;
import org.badfish.theworldshop.items.ItemType;
import org.badfish.theworldshop.items.paneitem.BasePanelItem;

import java.util.ArrayList;

/**
 * @author Sobadfish
 * @date 2022/11/19
 */
public class HiddenLimitItem extends BasePanelItem {
    //289

    public static Item toItem(String name, boolean isChose) {
        ArrayList<String> lore = new ArrayList<>();
        if(isChose){
            lore.add(TextFormat.colorize('&',"&r&l&a"+ TheWorldShopMainClass.language.getLang(TheWorldShopMainClass.language.itemHasChose)));
        }
        return toItem(Item.get(289), name, lore, ItemType.HIDE_LIMIT);

    }

    public static int getIndex(){
        return 37;
    }
}