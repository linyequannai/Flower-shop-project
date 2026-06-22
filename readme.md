# 🌸 FlowerShop 花卉商城系统

前后端分离的花卉电商平台，课程设计项目。

## 开发环境

| 工具 | 版本 |
|------|------|
| JDK | 21.0.11 |
| Maven | 3.9.15 |
| MySQL | 9.7.0 |
| Node.js | 24.16.0 |
| npm | 11.13.0 |

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Spring Boot 3.3.5 |
| ORM | MyBatis-Plus 3.5.7 |
| 安全认证 | Spring Security + JWT (jjwt) |
| 数据库 | MySQL |
| API文档 | Swagger / SpringDoc OpenAPI |
| 前端框架 | Vue 3 (Composition API) + Vite |
| UI 组件库 | Element Plus |
| 状态管理 | Pinia |
| 图表可视化 | ECharts 5 (vue-echarts) |
| HTTP 客户端 | Axios |

## 项目结构

```
├── sql/                          # 数据库脚本
│   └── init.sql                  # 建表 + 种子数据
├── backend/                      # Spring Boot 后端
│   ├── src/main/java/com/flowershop/
│   │   ├── common/               # 统一响应、异常处理
│   │   ├── config/               # Spring 配置
│   │   ├── security/             # JWT 认证
│   │   └── module/               # 业务模块
│   │       ├── user/             # 用户 & 登录
│   │       ├── category/         # 分类管理
│   │       ├── flower/           # 花卉管理
│   │       ├── cart/             # 购物车
│   │       ├── order/            # 订单
│   │       ├── favorite/         # 收藏
│   │       ├── review/           # 评价
│   │       ├── coupon/           # 优惠券
│   │       ├── file/             # 图片上传
│   │       └── statistics/       # 销量统计
│   └── backend-start.cmd         # 一键启动脚本
└── frontend/                     # Vue 3 前端
    ├── src/
    │   ├── api/                  # 接口封装
    │   ├── components/           # 公共组件
    │   ├── router/               # 路由
    │   ├── stores/               # Pinia 状态
    │   ├── utils/                # 工具函数
    │   └── views/                # 页面
    │       └── admin/            # 管理端页面
    └── fronted-start.cmd         # 一键启动脚本
```

## 快速启动

### 1. 创建数据库

```bash
mysql -u root -p < sql/init.sql
```

### 2. 配置数据库连接

将 `backend/src/main/resources/application.yml.example` 改为 `application.yml`，修改用户名密码。

### 3. 启动后端

```bash
cd backend
backend-start.cmd
```

### 4. 启动前端

```bash
cd frontend
frontend-start.cmd
```

浏览器访问 `http://localhost:5173`

## 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 用户 | testuser | admin123 |

## 功能清单

**用户端**

- 用户注册 / 登录（JWT 认证）
- 花卉浏览（分页、搜索、分类筛选、排序）
- 花卉详情 + 用户评价（星级评分）
- 购物车（加入、修改数量、选中/取消）
- 下单（库存扣减 + 优惠券核销）
- 订单管理（查看、取消、确认收货）
- 收藏夹
- 优惠券领取与使用
- 个人资料修改、密码修改

**管理端**

- 数据仪表盘（ECharts 图表：销售趋势、分类占比、畅销排行）
- 花卉管理（CRUD + 上下架 + 图片上传）
- 分类管理
- 订单管理（发货、状态变更）
- 用户管理
- 优惠券管理
- 评价管理

## 作者

- 凛叶泉奈 (linyequannai)
- GitHub：[linyequannai](https://github.com/linyequannai)

## 许可证

- 本项目采用 [MIT License](LICENSE) 开源协议
