### 一个能在Kook里使用的Osu std 机器人 插件
> 想做一个在Kook里用的白菜

current state: WIP \
当前进度: 咕咕咕

### 特点
- 支持多服务器成绩(目前是bancho 和[ppysb](htpps://osu.ppy.sb))
- 离线pp计算 ~~但是下载地图还是要网~~
- 可以设置API源

### 大饼
- 个人信息历史
- API v2(lazer)支持
- sb服的额外api实现

### 食用教程
首先你需要java 21的运行环境,这里推荐用zulu : https://www.azul.com/downloads/#zulu
- 去KOOK开发中心申请一个Bot : https://developer.kookapp.cn/app/index
- 下载KooKBC 作为插件的运行容器 : https://github.com/SNWCreations/KookBC
- 选择一个目录作为机器人的运行目录,然后塞入kookbc并运行
> 目前插件仅支持0.31.0及以上的kookbc
```shell
java -jar kookbc-0.31.0.jar
```
- 生成完相关文件即可关闭,将插件放入生成出来的plugins文件夹
- 打开kbc.yml文件填入从开发者中心获取到的token
```yaml
token: "tokennnnn"
```
- 保存并重新运行kookbc
- 在插件目录当中生成了与插件同名的数据文件,点进去,修改`config.yml`文件,申请Osu LegacyAPI key后填入(用于bancho的成绩查询和相关功能)
- 敬请享用
- 使用/help可以查看所有命令

注意: **机器人会在插件目录生成同名的数据文件,如非必要,请勿修改**

### 命令进度
- [X] 最近成绩
- [X] 最佳成绩
- [X] 账号管理
- [X] API查看
- [ ] API管理
- [X] 默认模式和默认服务器设置
- [ ] bancho v2 api和lazer支持
- [ ] 个人信息
- [ ] 打印指定铺面成绩
- [X] 铺面搜索
- [X] 任意pp计算

### 小演示
> 一个简单的成绩打印
![一个简单的成绩打印](docs/demo.png)
~~跟白菜大差不差了~~

### 鸣谢（不分先后）
- [白菜图片渲染代码和思路](https://github.com/Mother-Ship/cabbageWeb)
- [原作者皮肤](https://tieba.baidu.com/p/4399134680)
- [pp算法 rosu-pp(sb服)](https://github.com/ppy-sb/rosu-pp)
  - [上游](https://github.com/MaxOhn/rosu-pp)
- [KooK机器人框架](https://github.com/SNWCreations/KookBC)