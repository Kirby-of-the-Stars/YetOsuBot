### 一个能在Kook里使用的Osu std 机器人 插件
> 想做一个在Kook里用的白菜

current state: WIP \
当前进度: 重构中

### 特点
- 支持多服务器成绩(目前是bancho 和[ppysb](htpps://osu.ppy.sb))
- 离线pp计算 ~~但是下载地图还是要网~~
- 可以设置API源 ~~不过成绩就没法用镜像了~~

### 大饼
- 个人信息历史
- API v2(lazer)支持
- sb服的额外api实现

### 食用教程
首先你需要java 21的运行环境,这里推荐用zulu : https://www.azul.com/downloads/#zulu
- 去KOOK开发中心申请一个Bot : https://developer.kookapp.cn/app/index
- 下载KooKBC 作为插件的运行容器 : https://github.com/SNWCreations/KookBC
- 选择一个目录作为机器人的运行目录,然后塞入kookbc并运行
```shell
java -jar kookbc-0.30.2.jar
```
- 生成完相关文件即可关闭,将插件放入生成出来的plugins文件夹
- 打开kbc.yml文件填入从开发者中心获取到的token
```yaml
token: "tokennnnn"
```
- 保存并重新运行kookbc
- 敬请享用
- 目前kookbc正式版对于高版本JDK支持有严重问题，可能需要自行编译其dev分支下的kookbc才可正常运行

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
- [ ] 铺面搜索
- [ ] 任意pp计算

### 小演示
![一个简单的成绩打印](docs/demo.png)
~~跟白菜大差不差了~~

### 鸣谢（不分先后）
- [白菜图片渲染代码和思路](https://github.com/Mother-Ship/cabbageWeb)
- [原作者皮肤](https://tieba.baidu.com/p/4399134680)
- [pp算法 rosu-pp(sb服)](https://github.com/ppy-sb/rosu-pp)
  - [上游](https://github.com/MaxOhn/rosu-pp)
- [KooK机器人框架](https://github.com/SNWCreations/KookBC)