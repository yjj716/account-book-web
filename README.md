# 记账本 Account Book

一个多用户的个人记账系统，支持注册登录、账目增删查、分页筛选与月度统计。
后端 Spring Boot 3.5 + MyBatis + MySQL 8，前端原生 HTML + fetch，随 jar 一起部署。

## 功能

- 用户注册 / 登录，密码 BCrypt 加密存储
- 账目新增、删除、按 id 查询
- 分页 + 多条件筛选（日期区间、收支类型、分类）
- 月度收支统计（收入 / 支出 / 结余）
- **数据按用户隔离**：每个用户只能看到自己的账目

## 技术栈

| 层 | 选型 |
|---|---|
| 后端 | Java 21 / Spring Boot 3.5 / MyBatis 3.0 |
| 数据库 | MySQL 8.0 |
| 认证 | JWT（HS384）+ HandlerInterceptor |
| 前端 | 原生 HTML + fetch，无框架 |

## 本地运行

```bash
# 1. 建库
mysql -u root -p account_book < src/main/resources/db/schema.sql

# 2. 配置数据库密码（或用环境变量 DB_PASS 覆盖）
#    修改 src/main/resources/application.yml

# 3. 启动
mvn spring-boot:run

# 4. 浏览器打开 http://localhost:8080/login.html
```

测试账号：`caiye` / `123456`

## 接口

| 方法 | 路径 | 说明 | 需 token |
|---|---|---|---|
| POST | `/user/register` | 注册 | ✗ |
| POST | `/user/login` | 登录，返回 JWT | ✗ |
| GET | `/record` | 当前用户全部账目 | ✓ |
| GET | `/record/{id}` | 查单条 | ✓ |
| POST | `/record` | 新增 | ✓ |
| DELETE | `/record/{id}` | 删除 | ✓ |
| GET | `/record/page` | 分页 + 筛选 | ✓ |
| GET | `/record/statistics?month=2026-10` | 月度统计 | ✓ |

除注册登录外，所有接口需在请求头带 `Authorization: Bearer <token>`。

## 设计说明

**1. 认证与授权是两件事**

JWT 只回答"你是谁"。如果查询时不带 `user_id` 条件，任何登录用户都能查到别人的账目。所以每条业务 SQL 都带 `and user_id = #{userId}`。

**2. token 不落库**

签名可以用密钥重新计算，服务端验签即可，不需要存储 token。代价是签发后无法主动失效，只能靠较短的过期时间（本项目 120 分钟）。

**3. 越权返回 404 而非 403**

返回 403 等于告诉请求方"这条数据存在，只是不给你看"，可被用来枚举数据量。返回 404 让"不存在"和"不属于你"表现一致。

**4. 隔离条件写进 SQL，且不加 `<if>`**

`and user_id = #{userId}` 用固定拼接。若包在 `<if>` 里，参数为 null 时条件会整条消失，退化成查全部数据；固定拼接时查不到任何行，属于"默认拒绝"。

**5. 认证由拦截器承担**

`LoginInterceptor` 统一解析 `Authorization` 头并校验签名，通过后把 `userId` 放进 request 属性，Controller 直接取用。业务代码不重复处理认证。

## 已知限制

- token 存 localStorage，存在 XSS 风险；生产环境更适合 httpOnly cookie + CSRF 防护
- 错误码风格未统一：拦截器返回真实 HTTP 401，业务错误返回 HTTP 200 + body.code
- 未实现登出后立即失效（JWT 无状态，需引入黑名单或改用短过期 + refresh token）
- 未实现修改密码
- 前端仅覆盖登录与列表，新增/删除暂用接口工具测试
