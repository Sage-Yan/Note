# 项目：个人网站（综合型：展示 + 博客 + 作品集 + 交互）

> 作者：Yan Shijie（Yan / YanShijie）  
> 版本：v1.0  
> 目标读者：产品、设计、前后端开发、运维

---

## 1. 项目概述
**宗旨**：打造一个兼具个人品牌展示、持续内容输出、作品集陈列与互动体验的统一门户，成为对外形象与技术影响力的核心阵地。

**核心价值**：
- 一站式个人品牌主页（简介、技能、履历、项目）。
- 高效的技术博客平台（文章、笔记、专题）。
- 作品集展示（设计/程序/摄影等多类型媒介）。
- 互动增强（留言/评论、AI 助手、在线工具）。

**成功判据（KPI）**：
- 月活访客（MAU）≥ 1,000；平均停留时长 ≥ 2:30。
- 博客月更 ≥ 4 篇；RSS 订阅数 ≥ 100。
- 项目/简历页面跳转至 GitHub/简历下载转化率 ≥ 5%。
- 互动：评论/留言回复时效 ≤ 24h；AI 对话满意度（点赞/有用）≥ 80%。

---

## 2. 目标用户与场景
**用户角色**：
1) 招聘方/合作方：快速了解背景、项目与实力。  
2) 技术同好/读者：订阅博客，检索技术笔记。  
3) 普通访客：浏览作品图集、与 AI 互动、留言。  

**核心场景**：
- PC/移动端访问个人主页；10 秒内建立清晰印象（头像、标语、核心技能、项目入口）。
- 搜索/分类快速定位文章；移动端阅读无障碍；代码高亮、Latex、目录跳转。
- 作品集以卡片或瀑布流展示；支持图片放大、视频播放、源码/仓库跳转。
- 留言/评论与防垃圾；AI 助手解答站内 FAQ/技术问答；在线小工具（如 JSON 格式化）。

---

## 3. 信息架构（IA）与站点地图
**主导航**：
- 首页 / About / 博客 / 作品集 / 项目 / 工具 / 联系

**站点地图**：
- **首页**：英雄区（头像+Slogan）、技能雷达/标签、最新文章、精选作品、热门项目、AI 入口、社交链接。
- **About**：个人简介、核心技能树、教育/经历时间线、证书/奖项、简历下载。
- **博客**：列表（标签/分类/搜索/归档）、文章详情（TOC、代码高亮、评论、相关文章、点赞/收藏、RSS）。
- **作品集（Portfolio）**：多类型过滤（设计/程序/摄影…）、作品详情（媒体、描述、技术栈/EXIF、过程笔记）。
- **项目（Projects）**：代表项目（含 NFTurbo）卡片，详情页（架构图、特性、演示、版本、GitHub/文档）。
- **工具（Tools）**：在线小工具集合；可逐步扩展（JSON/YAML 格式化、图片压缩、正则测试等）。
- **互动**：评论/留言面板；**AI 助手**（站内知识库 + 通用问答）。
- **联系（Contact）**：表单、社交、商业合作入口。
- **管理后台（Admin）**：内容/作品/项目/评论/用户/工具配置。

---

## 4. 功能清单（按模块）
### 4.1 个人展示模块
- 个人简介：头像、Slogan、短简介（140 字）、详细简介。
- 技能体系：技能标签 + 熟练度（星级/进度条/雷达图）。
- 履历时间线：教育/实习/工作/证书/获奖。
- 简历：PDF 下载/在线阅读（多语言可选）。
- 项目概览：精选 3–6 个项目卡片（含 NFTurbo）。

**用户故事 & 验收**：
- 作为招聘者，我能 30 秒内看到核心技能、近期项目与联系方式（首页首屏完成）。
- 点击简历按钮，2 秒内开始下载 PDF；或在新标签页预览。

### 4.2 博客模块
- 文章：Markdown/MDX、代码高亮、Latex、TOC、封面图。
- 分类/标签/归档、全文搜索、系列专题。
- 评论：站内评论（含审核/屏蔽）、第三方（可选：Giscus/Disqus）。
- 订阅：RSS/Atom、邮件订阅（可选）。
- 草稿/定时发布、文章版本/修订记录。

**用户故事 & 验收**：
- 我能按标签/关键词快速找到文章；搜索响应 < 300ms（首屏）。
- 文章页图片懒加载；移动端排版无水平滚动。

### 4.3 作品集模块（Portfolio）
- 多类型作品：设计图/插画、程序项目 Demo、摄影图集、视频。
- 过滤与排序：按类型/年份/标签；按热度/时间排序。
- 作品详情：媒体查看、背景介绍、技术栈/EXIF、相关链接。

**用户故事 & 验收**：
- 我能在 3 次点击内从首页到达任意作品详情；图片原图支持放大查看。

### 4.4 交互模块
- 留言板：游客/登录用户留言，验证码/风控，站内邮件通知。
- 文章评论：嵌入式，支持表情/代码块/图片（可选）。
- AI 助手：
  - 模式 A：站内知识库（FAQ、关于我、项目文档）。
  - 模式 B：技术问答（调用后端代理，带速率限制与内容审核）。
- 在线工具：从 1–3 个轻工具起步（如 JSON/UUID 生成、Base64 编解码）。

**用户故事 & 验收**：
- 留言后收到成功反馈；后台可审核、删除、拉黑 IP/账号。
- AI 聊天窗口在移动端不遮挡正文；历史会话本地保存（可选登录云同步）。

### 4.5 管理后台（CMS）
- 登录/权限：管理员、作者、访客（只读）。
- 内容管理：文章/分类/标签/系列；作品/项目；留言/评论；页面配置（导航、页脚、社交）。
- 媒体库：图片/视频上传与压缩，CDN 配置。
- 数据面板：PV/UV/来源、热门文章、搜索词。
- 审核工作流：草稿 → 待审 → 发布；支持定时发布。

---

## 5. 非功能需求
- **性能**：
  - 首屏 LCP < 2.0s（4G/中端机）；图片懒加载；SSR/SSG。
  - 列表分页；静态资源 gzip/br；路由级代码分割。
- **安全**：
  - 登录/管理接口鉴权（JWT + 刷新）；CSRF、XSS、SQL 注入防护；速率限制。
  - 评论/留言防垃圾（Akismet/自研规则 + 验证码 + IP 节流）。
- **SEO**：
  - 每页唯一 title/desc、OG/Twitter 卡片；sitemap.xml、robots.txt；结构化数据（Article/Person/Project）。
- **可用性**：
  - 响应式（≥ 360px）；键盘可达；暗黑模式。
- **可观测性**：
  - 前端埋点（PageView、Click、Search、Share）、后端链路（可对接 SkyWalking）。

---

## 6. 技术方案（建议）
- **前端**：Vite + React（Next.js 可选）/Vue3；TailwindCSS；Framer Motion；代码高亮（Shiki/Prism）；MDX 支持。
- **后端**：Spring Boot 3.x + JDK 21；MySQL + Redis；Spring Security；JPA/MyBatis；OpenAPI（Swagger）。
- **存储/媒体**：对象存储（阿里云 OSS/七牛）；CDN。
- **搜索**：本地倒排（初期）→ Elasticsearch/Meilisearch（进阶）。
- **评论**：自建表 + 审核流；或接入第三方（Giscus）作为备选。
- **AI 助手**：后端代理（带风控/缓存）；站内知识库（Markdown + Embedding 索引）。
- **部署**：Docker Compose；Nginx 反代；HTTPS（Let’s Encrypt）；CI/CD（GitHub Actions）。
- **监控**：SkyWalking/Prometheus + Grafana；日志（ELK/EFK）。

> 备注：可与 **NFTurbo** 生态共享基础设施（日志、监控、网关、用户中心等）。

---

## 7. 数据模型（简表）
### 7.1 内容域
- **post**：id，slug，title，summary，content_md，cover_url，status，publish_time，author_id，view_count，like_count。
- **category**：id，name，slug。
- **tag**：id，name，slug。**post_tag**（post_id, tag_id）。
- **comment**：id，entity_type(post/article/project)，entity_id，parent_id，user_id，content_html，status，ip，ua，created_at。
- **work**（作品）：id，title，type(design/code/photo/video)，desc，media(list)，tech_stack(list)，exif(json)，links(json)，year，tags。
- **project**：id，name，slug，intro，stack，repo_url，demo_url，version，changelog_md，arch_img。
- **page**：id，slug，title，content_md，meta(json)。

### 7.2 账户域
- **user**：id，username，email，role（admin/author/guest），password_hash，avatar，bio。
- **session/token**：id，user_id，refresh_token，expired_at，ip。

### 7.3 交互/工具域
- **message**（留言）：id，nickname/email，可匿名，content，status，ip，ua。
- **tool_history**：id，tool_key，input，output，user_id/anon_id，created_at。
- **ai_chat**：id，conversation_id，role，content，tokens，latency，rating。

---

## 8. 关键 API 设计（示例）
- `GET /api/posts?kw=&tag=&cat=&page=&size=`
- `GET /api/posts/{slug}`
- `POST /api/posts`（auth: author）
- `POST /api/comments`（节流 + 审核）
- `GET /api/works?type=&tag=&year=` / `GET /api/projects`
- `POST /api/messages`（验证码 + 频控）
- `POST /api/ai/chat`（后端转发，带限流/敏感词与异常兜底）
- `GET /api/metrics/hot`（热门文章/作品）
- `GET /api/sitemap.xml`、`GET /rss.xml`

**鉴权与风控**：
- JWT（短期） + 刷新令牌（长期）；关键写接口开启速率限制（如 5 req/min/IP）。
- 管理端所有接口要求 `role in (admin, author)`；操作日志表记录。

---

## 9. 页面与组件（选摘）
- Hero(头像/标语/CTA)、SkillRadar、Timeline、PostList、PostCard、MDXRenderer、Tag/CategoryChips、WorkGrid、Lightbox、ProjectCard、ToolWidget、ChatDock、CommentThread、Paginator、ShareBar、SearchBox、ThemeToggle。

---

## 10. 交互与视觉要点
- 风格：极简、留白、卡片化、圆角 16–20、柔和阴影。
- 动效：进入/悬停微动效（Framer Motion）；滚动显隐（IntersectionObserver）。
- 可达性：表单 label/aria、键盘导航、色彩对比 ≥ 4.5:1。

---

## 11. 运维与发布
- 分支策略：`main`（稳定） / `dev`（开发） / `feat/*`。语义化提交（conventional commits）。
- CI：lint/test/build；Preview 环境（Vercel/自建 preview）。
- 发布：Tags + Release Notes；自动生成 `sitemap.xml`、`rss.xml`。
- 备份：数据库每日快照 + OSS 媒体生命周期策略。

---

## 12. 里程碑与范围（MVP → 扩展）
**MVP（2–3 周）**：
1) 首页（英雄区、最新文章、精选作品/项目、社交）。
2) 博客：文章展示、分类/标签、搜索（本地）、评论（审核）。
3) 作品集：设计/程序/摄影 3 类，详情页 + Lightbox。
4) 项目页：NFTurbo + 2–3 个代表项目卡片与详情。
5) 联系表单 + 留言板（验证码）。
6) 管理后台：文章/标签/作品/评论的基本 CRUD。

**扩展（按需迭代）**：
- AI 助手（知识库 + 技术问答）、在线工具集合、Meilisearch、RSS/邮件订阅、CDN、图片压缩、PWA、阅读进度与字数/预计时长。

---

## 13. 风险与对策
- **内容维护负担**：编辑体验优化（MDX、图片一键压缩/外链、批量上传）。
- **垃圾评论**：多级防护 + 白名单 + 首条人工审。
- **成本控制**：初期采用开源 + 轻量部署，按访问量再上 CDN/搜索。
- **AI 合规**：接口限流、提示词与回复审计、错误兜底（降级为站内搜索/FAQ）。

---

## 14. 附：样例数据与规范
- Slug 规则：`/blog/{yyyy}/{mm}/{slug}`；作品：`/w/{slug}`；项目：`/p/{slug}`。
- 命名：文件/组件使用小驼峰；API 使用 RESTful；提交规范 `feat/fix/docs/chore`。
- 图片：首图 ≤ 300KB，WebP 优先；懒加载 + 占位模糊。

---

> 下一步建议：确认视觉风格与首页线框 → 搭建仓库与分支 → 初始化前后端脚手架 → 定义内容模型与后台表单 → 拉通首屏与博客详情页。



## 推荐顺序（实战版）

1. **信息架构 & 路由表**（纸上/文档）
    列出页面与 URL：`/`、`/about`、`/blog`、`/blog/:slug`、`/works`、`/works/:id`、`/projects`、`/tools`、`/contact`、`/admin`.
2. **脚手架与基础配置**
    Vite 初始化、TS、ESLint+Prettier、路径别名、环境变量（`.env`）。
3. **App Shell & 布局**
    顶栏/侧栏/面包屑/页脚、响应式容器、主题切换（暗黑）。这是全站“骨架”。
4. **路由骨架**
    只放**空页面**（Placeholder）先把导航跑通，确保页面跳转/404/懒加载OK。
5. **设计系统 / UI 基件**
    TailwindCSS 或 UI 库；按钮、标签、卡片、栅格、分页等“可复用小砖块”。
6. **数据契约 & 请求层**
    定义接口类型（TypeScript `types`）、封装 `request`（拦截器、错误处理、鉴权头）。
    **服务端状态**建议用 **TanStack Query（React Query / Vue Query）** 管理：缓存、请求态、重试、分页都省心。
7. **页面实现（自外向内）**
    先列表页后详情页；先静态数据跑通，再接真接口；边做边抽出复用组件。
8. **是否需要 Store？按需添加**
   - **需要 store 的场景**：用户会话、主题/语言偏好、购物车式的临时数据、跨多页面的 UI 状态等。
   - **不需要 store 的场景**：文章列表/详情等“来自服务器的内容”（交给 Query 系列）。
      React 选 **Zustand/Redux Toolkit**；Vue 选 **Pinia**。只放**纯客户端状态**。
9. **可用性 / 性能**
    路由级代码分割、图片懒加载、SEO 元信息、骨架屏/占位、错误边界。
10. **测试 & 发布**
     关键组件单测、E2E（可选），CI/CD，预览环境。
