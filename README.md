# TheWorldShop

基于 Nukkit 的全球市场插件
箱子 UI 全球市场 Win10 玩家体验更佳

## 引言

还在为 GUI 商店而烦恼吗 还在为 GUI 商店繁琐的操作而感到不适吗 不如尝试一下这款插件吧。
本插件拥有便捷的操作，简洁的 UI 界面会让你爱上这款插件!

> 关键字: 上架 出售 筛选

## 简介 - Readme

### 物品出售

1. 仅需要执行 `/tw add` 就可以将物品出售到 全球市场！ 是不是非常方便
1. 仅需要执行 `/tw` 便可打开交易市场界面当物品太多的时候会自动的分页

### 市场筛选

1. 进入交易市场界面后 可设置筛选条件,物品排序
2. 利用筛选和排序可以快捷的得到需要的物品

### 物品回收

1. 仅需要执行 `/tw set <金钱>` 就可以设置物品的回收价格（当物品为nbt物品时 指令为`/tw set <金钱> <名称>`）
2. 仅需要执行 `/tw sell` 就可以检测出售背包内的物品玩家可以利用箱子 UI 进行出售

## 插件事件 - Plugin Event

| 事件名称                | 事件描述          |
|---------------------|---------------|
| PlayerSellItemEvent | 玩家出售物品事件      |
| PlayerBuyItemEvent  | 物品被玩家购买事件     |
| ChoseByItemEvent    | 玩家在全球市场选择物品事件 |
| SellItemEvent       | 可回收物品被售出事件    |