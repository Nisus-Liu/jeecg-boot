

文档: http://jeecg-boot.mydoc.io
http://doc.jeecg.com/1273752

```
jeecg-boot 从v2.0版本，重构成maven多模块项目，启动项目：jeecg-boot-module-system

项目结构说明：
├─jeecg-boot-parent（父POM： 项目依赖、modules组织）
│  ├─jeecg-boot-base-common（共通Common模块： 底层工具类、注解、接口）
│  ├─jeecg-boot-module-system （系统管理模块： 系统管理、权限等功能） -- 默认作为启动项目
│  ├─jeecg-boot-module-{?} （自己扩展新模块项目，启动的时候，在system里面引用即可）
```