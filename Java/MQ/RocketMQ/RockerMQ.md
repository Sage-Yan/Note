# 1. 领域模型概述

本文为您介绍 Apache RocketMQ 的领域模型。

Apache RocketMQ 是一款典型的分布式架构下的中间件产品，使用异步通信方式和发布订阅的消息传输模型。通信方式和传输模型的具体说明，请参见下文**通信方式介绍**和**消息传输模型**介绍。 Apache RocketMQ 产品具备异步通信的优势，系统拓扑简单、上下游耦合较弱，主要应用于异步解耦，流量削峰填谷等场景。

## 1.1 Apache RocketMQ领域模型

![领域模型](https://rocketmq.apache.org/zh/assets/images/mainarchi-9b036e7ff5133d050950f25838367a17.png)

如上图所示，Apache RocketMQ 中消息的生命周期主要分为消息生产、消息存储、消息消费这三部分。

生产者生产消息并发送至 Apache RocketMQ 服务端，消息被存储在服务端的主题中，消费者通过订阅主题消费消息。

**消息生产**

[生产者（Producer）](https://rocketmq.apache.org/zh/docs/domainModel/04producer)：

Apache RocketMQ 中用于产生消息的运行实体，一般集成于业务调用链路的上游。生产者是轻量级匿名无身份的。

**消息存储**

- [主题（Topic）](https://rocketmq.apache.org/zh/docs/domainModel/02topic)：

  Apache RocketMQ 消息传输和存储的分组容器，主题内部由多个队列组成，消息的存储和水平扩展实际是通过主题内的队列实现的。

- [队列（MessageQueue）](https://rocketmq.apache.org/zh/docs/domainModel/03messagequeue)：

  Apache RocketMQ 消息传输和存储的实际单元容器，类比于其他消息队列中的分区。 Apache RocketMQ 通过流式特性的无限队列结构来存储消息，消息在队列内具备顺序性存储特征。

- [消息（Message）](https://rocketmq.apache.org/zh/docs/domainModel/04message)：

  Apache RocketMQ 的最小传输单元。消息具备不可变性，在初始化发送和完成存储后即不可变。

**消息消费**

- [消费者分组（ConsumerGroup）](https://rocketmq.apache.org/zh/docs/domainModel/07consumergroup)：

  Apache RocketMQ 发布订阅模型中定义的独立的消费身份分组，用于统一管理底层运行的多个消费者（Consumer）。同一个消费组的多个消费者必须保持消费逻辑和配置一致，共同分担该消费组订阅的消息，实现消费能力的水平扩展。

- [消费者（Consumer）](https://rocketmq.apache.org/zh/docs/domainModel/08consumer)：

  Apache RocketMQ 消费消息的运行实体，一般集成在业务调用链路的下游。消费者必须被指定到某一个消费组中。

- [订阅关系（Subscription）](https://rocketmq.apache.org/zh/docs/domainModel/09subscription)：

  Apache RocketMQ 发布订阅模型中消息过滤、重试、消费进度的规则配置。订阅关系以消费组粒度进行管理，消费组通过定义订阅关系控制指定消费组下的消费者如何实现消息过滤、消费重试及消费进度恢复等。

  Apache RocketMQ 的订阅关系除过滤表达式之外都是持久化的，即服务端重启或请求断开，订阅关系依然保留。

## 1.2 通信方式介绍

分布式系统架构思想下，将复杂系统拆分为多个独立的子模块，例如微服务模块。此时就需要考虑子模块间的远程通信，典型的通信模式分为以下两种，一种是同步的RPC远程调用；一种是基于中间件代理的异步通信方式。

同步RPC调用模型 ![同步调用](https://rocketmq.apache.org/zh/assets/images/syncarchi-ebbd41e1afd6adf432792ee2d7a91748.png)

同步RPC调用模型下，不同系统之间直接进行调用通信，每个请求直接从调用方发送到被调用方，然后要求被调用方立即返回响应结果给调用方，以确定本次调用结果是否成功。 **注意** 此处的同步并不代表RPC的编程接口方式，RPC也可以有异步非阻塞调用的编程方式，但本质上仍然是需要在指定时间内得到目标端的直接响应。

异步通信模型 ![异步调用](https://rocketmq.apache.org/zh/assets/images/asyncarchi-e7ee18dd77aca472fb80bb2238d9528b.png)

异步消息通信模式下，各子系统之间无需强耦合直接连接，调用方只需要将请求转化成异步事件（消息）发送给中间代理，发送成功即可认为该异步链路调用完成，剩下的工作中间代理会负责将事件可靠通知到下游的调用系统，确保任务执行完成。该中间代理一般就是消息中间件。

异步通信的优势如下：

- 系统拓扑简单。由于调用方和被调用方统一和中间代理通信，系统是星型结构，易于维护和管理。

- 上下游耦合性弱。上下游系统之间弱耦合，结构更灵活，由中间代理负责缓冲和异步恢复。 上下游系统间可以独立升级和变更，不会互相影响。

- 容量削峰填谷。基于消息的中间代理往往具备很强的流量缓冲和整形能力，业务流量高峰到来时不会击垮下游。

## 1.3 消息传输模型介绍

主流的消息中间件的传输模型主要为点对点模型和发布订阅模型。

点对点模型 ![点对点模型](https://rocketmq.apache.org/zh/assets/images/p2pmode-fefdc2fbe4792e757e26befc0b3acbff.png)

点对点模型也叫队列模型，具有如下特点：

- 消费匿名：消息上下游沟通的唯一的身份就是队列，下游消费者从队列获取消息无法申明独立身份。
- 一对一通信：基于消费匿名特点，下游消费者即使有多个，但都没有自己独立的身份，因此共享队列中的消息，每一条消息都只会被唯一一个消费者处理。因此点对点模型只能实现一对一通信。

发布订阅模型 ![发布订阅模型](https://rocketmq.apache.org/zh/assets/images/pubsub-042a4e5e5d76806943bd7dcfb730c5d5.png)

发布订阅模型具有如下特点：

- 消费独立：相比队列模型的匿名消费方式，发布订阅模型中消费方都会具备的身份，一般叫做订阅组（订阅关系），不同订阅组之间相互独立不会相互影响。
- 一对多通信：基于独立身份的设计，同一个主题内的消息可以被多个订阅组处理，每个订阅组都可以拿到全量消息。因此发布订阅模型可以实现一对多通信。

传输模型对比

点对点模型和发布订阅模型各有优势，点对点模型更为简单，而发布订阅模型的扩展性更高。 Apache RocketMQ 使用的传输模型为发布订阅模型，因此也具有发布订阅模型的特点。

# 2. 主题（Topic）

本文介绍 Apache RocketMQ 中主题（Topic）的定义、模型关系、内部属性、行为约束、版本兼容性及使用建议。

## 2.1 定义

主题是 Apache RocketMQ 中消息传输和存储的顶层容器，用于标识同一类业务逻辑的消息。 主题的作用主要如下：

- **定义数据的分类隔离：** 在 Apache RocketMQ 的方案设计中，建议将不同业务类型的数据拆分到不同的主题中管理，通过主题实现存储的隔离性和订阅隔离性。
- **定义数据的身份和权限：** Apache RocketMQ 的消息本身是匿名无身份的，同一分类的消息使用相同的主题来做身份识别和权限管理。

## 2.2 模型关系

在整个 Apache RocketMQ 的领域模型中，主题所处的流程和位置如下：

![主题](https://rocketmq.apache.org/zh/assets/images/archifortopic-ef512066703a22865613ea9216c4c300.png)

主题是 Apache RocketMQ 的顶层存储，所有消息资源的定义都在主题内部完成，但主题是一个逻辑概念，并不是实际的消息容器。

主题内部由多个队列组成，消息的存储和水平扩展能力最终是由队列实现的；并且针对主题的所有约束和属性设置，最终也是通过主题内部的队列来实现。

## 2.3 内部属性

**主题名称**

- 定义：主题的名称，用于标识主题，主题名称集群内全局唯一。
- 取值：由用户创建主题时定义。
- 约束：请参见[参数限制](https://rocketmq.apache.org/zh/docs/introduction/03limits)。

**队列列表**

- 定义：队列作为主题的组成单元，是消息存储的实际容器，一个主题内包含一个或多个队列，消息实际存储在主题的各队列内。更多信息，请参见[队列（MessageQueue）](https://rocketmq.apache.org/zh/docs/domainModel/03messagequeue)。
- 取值：系统根据队列数量给主题分配队列，队列数量创建主题时定义。
- 约束：一个主题内至少包含一个队列。

**消息类型**

- 定义：主题所支持的消息类型。
- 取值：创建主题时选择消息类型。Apache RocketMQ 支持的主题类型如下：
  - Normal：[普通消息](https://rocketmq.apache.org/zh/docs/featureBehavior/01normalmessage)，消息本身无特殊语义，消息之间也没有任何关联。
  - FIFO：[顺序消息](https://rocketmq.apache.org/zh/docs/featureBehavior/03fifomessage)，Apache RocketMQ 通过消息分组MessageGroup标记一组特定消息的先后顺序，可以保证消息的投递顺序严格按照消息发送时的顺序。
  - Delay：[定时/延时消息](https://rocketmq.apache.org/zh/docs/featureBehavior/02delaymessage)，通过指定延时时间控制消息生产后不要立即投递，而是在延时间隔后才对消费者可见。
  - Transaction：[事务消息](https://rocketmq.apache.org/zh/docs/featureBehavior/04transactionmessage)，Apache RocketMQ 支持分布式事务消息，支持应用数据库更新和消息调用的事务一致性保障。
- 约束：Apache RocketMQ 从5.0版本开始，支持强制校验消息类型，即每个主题只允许发送一种消息类型的消息，这样可以更好的运维和管理生产系统，避免混乱。为保证向下兼容4.x版本行为，强制校验功能默认开启。

## 2.4 行为约束

**消息类型强制校验**

Apache RocketMQ 5.x版本支持将消息类型拆分到主题中进行独立运维和处理，因此系统会对发送的消息类型和主题定的消息类型进行强制校验，若校验不通过，则消息发送请求会被拒绝，并返回类型不匹配异常。校验原则如下：

- 消息类型必须一致：发送的消息的类型，必须和目标主题定义的消息类型一致。
- 主题类型必须单一：每个主题只支持一种消息类型，不允许将多种类型的消息发送到同一个主题中。

信息

为保证向下兼容4.x版本行为，上述强制校验功能默认开启。

**常见错误使用场景**

- 发送的消息类型不匹配。例如：创建主题时消息类型定义为顺序消息，发送消息时发送事务消息到该主题中，此时消息发送请求会被拒绝，并返回类型不匹配异常。
- 单一消息主题混用。例如：创建主题时消息类型定义为普通消息，发送消息时同时发送普通消息和顺序消息到该主题中，则顺序消息的发送请求会被拒绝，并返回类型不匹配异常。

## 2.5 版本兼容性

消息类型的强制校验，仅 Apache RocketMQ 服务端5.x版本支持，且默认开启，推荐部署时打开配置。 Apache RocketMQ 服务端4.x和3.x历史版本的SDK不支持强制校验，您需要自己保证消息类型一致。 如果您使用的服务端版本为历史版本，建议您升级到 Apache RocketMQ 服务端5.x版本。

## 2.6 使用示例

Apache RocketMQ 5.0版本下创建主题操作，推荐使用mqadmin工具，需要注意的是，对于消息类型需要通过属性参数添加。示例如下：

```shell
sh mqadmin updateTopic -n <nameserver_address> -t <topic_name> -c <cluster_name> -a +message.type=<message_type>
```



其中message_type根据消息类型设置成Normal/FIFO/Delay/Transaction。如果不设置，默认为Normal类型。

## 2.7 使用建议

**按照业务分类合理拆分主题**

Apache RocketMQ 的主题拆分设计应遵循大类统一原则，即将相同业务域内同一功能属性的消息划分为同一主题。拆分主题时，您可以从以下角度考虑拆分粒度：

- 消息类型是否一致：不同类型的消息，如顺序消息和普通消息需要使用不同的主题。
- 消息业务是否关联：如果业务没有直接关联，比如，淘宝交易消息和盒马物流消息没有业务交集，需要使用不同的消息主题；同样是淘宝交易消息，女装类订单和男装类订单可以使用同一个订单。当然，如果业务量较大或其他子模块应用处理业务时需要进一步拆分订单类型，您也可以将男装订单和女装订单的消息拆分到两个主题中。
- 消息量级是否一样：数量级不同或时效性不同的业务消息建议使用不同的主题，例如某些业务消息量很小但是时效性要求很强，如果跟某些万亿级消息量的业务使用同一个主题，会增加消息的等待时长。

**正确拆分示例：** 线上商品购买场景下，订单交易如订单创建、支付、取消等流程消息使用一个主题，物流相关消息使用一个主题，积分管理相关消息使用一个主题。

**错误拆分示例：**

- 拆分粒度过粗：会导致业务隔离性差，不利于独立运维和故障处理。例如，所有交易消息和物流消息都共用一个主题。
- 拆分粒度过细：会消耗大量主题资源，造成系统负载过重。例如，按照用户ID区分，每个用户ID使用一个主题。

**单一主题只收发一种类型消息，避免混用**

Apache RocketMQ 主题的设计原则为通过主题隔离业务，不同业务逻辑的消息建议使用不同的主题。同一业务逻辑消息的类型都相同，因此，对于指定主题，应该只收发同一种类型的消息。

**主题管理尽量避免自动化机制**

在 Apache RocketMQ 架构中，主题属于顶层资源和容器，拥有独立的权限管理、可观测性指标采集和监控等能力，创建和管理主题会占用一定的系统资源。因此，生产环境需要严格管理主题资源，请勿随意进行增、删、改、查操作。

Apache RocketMQ 虽然提供了自动创建主题的功能，但是建议仅在测试环境使用，生产环境请勿打开，避免产生大量垃圾主题，无法管理和回收并浪费系统资源。

# 3. 队列（MessageQueue）

本文介绍 Apache RocketMQ 中队列（MessageQueue）的定义、模型关系、内部属性、版本兼容性及使用建议。

## 3.1 定义

队列是 Apache RocketMQ 中消息存储和传输的实际容器，也是 Apache RocketMQ 消息的最小存储单元。 Apache RocketMQ 的所有主题都是由多个队列组成，以此实现队列数量的水平拆分和队列内部的流式存储。

队列的主要作用如下：

- 存储顺序性

  队列天然具备顺序性，即消息按照进入队列的顺序写入存储，同一队列间的消息天然存在顺序关系，队列头部为最早写入的消息，队列尾部为最新写入的消息。消息在队列中的位置和消息之间的顺序通过位点（Offset）进行标记管理。

- 流式操作语义

  Apache RocketMQ 基于队列的存储模型可确保消息从任意位点读取任意数量的消息，以此实现类似聚合读取、回溯读取等特性，这些特性是RabbitMQ、ActiveMQ等非队列存储模型不具备的。

## 3.2 模型关系

在整个 Apache RocketMQ 的领域模型中，队列所处的流程和位置如下：![队列](https://rocketmq.apache.org/zh/assets/images/archiforqueue-dd6788b33bf2fc96b4a1dab83a1b0d71.png)

Apache RocketMQ 默认提供消息可靠存储机制，所有发送成功的消息都被持久化存储到队列中，配合生产者和消费者客户端的调用可实现至少投递一次的可靠性语义。

Apache RocketMQ 队列模型和Kafka的分区（Partition）模型类似。在 Apache RocketMQ 消息收发模型中，队列属于主题的一部分，虽然所有的消息资源以主题粒度管理，但实际的操作实现是面向队列。例如，生产者指定某个主题，向主题内发送消息，但实际消息发送到该主题下的某个队列中。

Apache RocketMQ 中通过修改队列数量，以此实现横向的水平扩容和缩容。

## 3.3 内部属性

读写权限

- 定义：当前队列是否可以读写数据。
- 取值：由服务端定义，枚举值如下
  - 6：读写状态，当前队列允许读取消息和写入消息。
  - 4：只读状态，当前队列只允许读取消息，不允许写入消息。
  - 2：只写状态，当前队列只允许写入消息，不允许读取消息。
  - 0：不可读写状态，当前队列不允许读取消息和写入消息。

- 约束：队列的读写权限属于运维侧操作，不建议频繁修改。

## 3.4 行为约束

每个主题下会由一到多个队列来存储消息，每个主题对应的队列数与消息类型以及实例所处地域（Region）相关。

## 3.5 版本兼容性

队列的名称属性在 Apache RocketMQ 服务端的不同版本中有如下差异：

- 服务端3.x/4.x版本：队列名称由{主题名称}+{BrokerID}+{QueueID}三元组组成，和物理节点绑定。
- 服务端5.x版本：队列名称为一个集群分配的全局唯一的字符串组成，和物理节点解耦。

因此，在开发过程中，建议不要对队列名称做任何假设和绑定。如果您在代码中自定义拼接队列名称并和其他操作进行绑定，一旦服务端版本升级，可能会出现队列名称无法解析的兼容性问题。

## 3.6 使用建议

**按照实际业务消耗设置队列数**

Apache RocketMQ 的队列数可在创建主题或变更主题时设置修改，队列数量的设置应遵循少用够用原则，避免随意增加队列数量。

主题内队列数过多可能对导致如下问题：

- 集群元数据膨胀

  Apache RocketMQ 会以队列粒度采集指标和监控数据，队列过多容易造成管控元数据膨胀。

- 客户端压力过大

  Apache RocketMQ 的消息读写都是针对队列进行操作，队列过多对应更多的轮询请求，增加系统负荷。

**常见队列增加场景**

- 需要增加队列实现物理节点负载均衡

  Apache RocketMQ 每个主题的多个队列可以分布在不同的服务节点上，在集群水平扩容增加节点后，为了保证集群流量的负载均衡，建议在新的服务节点上新增队列，或将旧的队列迁移到新的服务节点上。

- 需要增加队列实现顺序消息性能扩展

  在 Apache RocketMQ 服务端4.x版本中，顺序消息的顺序性在队列内生效的，因此顺序消息的并发度会在一定程度上受队列数量的影响，因此建议仅在系统性能瓶颈时再增加队列。

# 4. 消息（Message）

本文介绍 Apache RocketMQ 中消息（Message）的定义、模型关系、内部属性、行为约束及使用建议。

## 4.1 定义

消息是 Apache RocketMQ 中的最小数据传输单元。生产者将业务数据的负载和拓展属性包装成消息发送到 Apache RocketMQ 服务端，服务端按照相关语义将消息投递到消费端进行消费。

Apache RocketMQ 的消息模型具备如下特点：

- **消息不可变性**

  消息本质上是已经产生并确定的事件，一旦产生后，消息的内容不会发生改变。即使经过传输链路的控制也不会发生变化，消费端获取的消息都是只读消息视图。

- **消息持久化**

  Apache RocketMQ 会默认对消息进行持久化，即将接收到的消息存储到 Apache RocketMQ 服务端的存储文件中，保证消息的可回溯性和系统故障场景下的可恢复性。

## 4.2 模型关系

在整个 Apache RocketMQ 的领域模型中，消息所处的流程和位置如下：![消息](https://rocketmq.apache.org/zh/assets/images/archiforqueue-dd6788b33bf2fc96b4a1dab83a1b0d71.png)

1. 消息由生产者初始化并发送到Apache RocketMQ 服务端。
2. 消息按照到达Apache RocketMQ 服务端的顺序存储到队列中。
3. 消费者按照指定的订阅关系从Apache RocketMQ 服务端中获取消息并消费。

## 4.3 消息内部属性

**系统保留属性**

**主题名称**

- 定义：当前消息所属的主题的名称。集群内全局唯一。更多信息，请参见[主题（Topic）](https://rocketmq.apache.org/zh/docs/domainModel/02topic)。
- 取值：从客户端SDK接口获取。

**消息类型**

- 定义：当前消息的类型。
- 取值：从客户端SDK接口获取。Apache RocketMQ 支持的消息类型如下：
  - Normal：[普通消息](https://rocketmq.apache.org/zh/docs/featureBehavior/01normalmessage)，消息本身无特殊语义，消息之间也没有任何关联。
  - FIFO：[顺序消息](https://rocketmq.apache.org/zh/docs/featureBehavior/03fifomessage)，Apache RocketMQ 通过消息分组MessageGroup标记一组特定消息的先后顺序，可以保证消息的投递顺序严格按照消息发送时的顺序。
  - Delay：[定时/延时消息](https://rocketmq.apache.org/zh/docs/featureBehavior/02delaymessage)，通过指定延时时间控制消息生产后不要立即投递，而是在延时间隔后才对消费者可见。
  - Transaction：[事务消息](https://rocketmq.apache.org/zh/docs/featureBehavior/04transactionmessage)，Apache RocketMQ 支持分布式事务消息，支持应用数据库更新和消息调用的事务一致性保障。

**消息队列**

- 定义：实际存储当前消息的队列。更多信息，请参见[队列（MessageQueue）](https://rocketmq.apache.org/zh/docs/domainModel/03messagequeue)。
- 取值：**由服务端指定并填充。**

**消息位点**

- 定义：当前消息存储在队列中的位置。更多信息，请参见[消费进度原理](https://rocketmq.apache.org/zh/docs/featureBehavior/09consumerprogress)。
- 取值：由服务端指定并填充。取值范围：0~long.Max。

**消息ID**

- 定义：消息的唯一标识，集群内每条消息的ID全局唯一。
- 取值：生产者客户端系统自动生成。固定为数字和大写字母组成的32位字符串。

**索引Key列表（可选）**

- 定义：消息的索引键，可通过设置不同的Key区分消息和快速查找消息。
- 取值：由生产者客户端定义。

**过滤标签Tag（可选）**

- 定义：消息的过滤标签。消费者可通过Tag对消息进行**过滤**，仅接收指定标签的消息。
- 取值：由生产者客户端定义。
- 约束：一条消息仅支持设置一个标签。

**定时时间（可选）**

- 定义：定时场景下，消息触发延时投递的毫秒级时间戳。更多信息，请参见[定时/延时消息](https://rocketmq.apache.org/zh/docs/featureBehavior/02delaymessage)。
- 取值：由消息生产者定义。
- 约束：最大可设置定时时长为40天。

**消息发送时间**

- 定义：消息发送时，生产者客户端系统的本地毫秒级时间戳。
- 取值：由生产者客户端系统填充。
- 说明：客户端系统时钟和服务端系统时钟可能存在偏差，消息发送时间是以客户端系统时钟为准。

**消息保存时间戳**

- 定义：消息在Apache RocketMQ 服务端完成存储时，服务端系统的本地毫秒级时间戳。 对于定时消息和事务消息，消息保存时间指的是消息生效对消费方可见的服务端系统时间。

- 取值：由服务端系统填充。
- 说明：客户端系统时钟和服务端系统时钟可能存在偏差，消息保留时间是以服务端系统时钟为准。

**消费重试次数**

- 定义：消息消费失败后，Apache RocketMQ 服务端重新投递的次数。每次重试后，重试次数加1。更多信息，请参见[消费重试](https://rocketmq.apache.org/zh/docs/featureBehavior/10consumerretrypolicy)。
- 取值：由服务端系统标记。首次消费，重试次数为0；消费失败首次重试时，重试次数为1。

**业务自定义属性**

- 定义：生产者可以自定义设置的扩展信息。
- 取值：由消息生产者自定义，按照字符串键值对设置。

**消息负载**

- 定义：业务消息的实际报文数据。
- 取值：由生产者负责序列化编码，按照二进制字节传输。
- 约束：请参见[参数限制](https://rocketmq.apache.org/zh/docs/introduction/03limits)。

## 4.4 行为约束

消息大小不得超过其类型所对应的限制，否则消息会发送失败。

系统默认的消息最大限制如下：

- 普通和顺序消息：**4 MB**
- 事务和定时或延时消息：**64 KB**

## 4.5 使用建议

**单条消息不建议传输超大负载**

作为一款消息中间件产品，Apache RocketMQ 一般传输的是都是业务事件数据。单个原子消息事件的数据大小需要严格控制，如果单条消息过大容易造成网络传输层压力，不利于异常重试和流量控制。

生产环境中如果需要传输超大负载，**建议按照固定大小做报文拆分**，或者结合文件存储等方法进行传输。

**消息中转时做好不可变设计**

Apache RocketMQ 服务端5.x版本中，消息本身不可编辑，消费端获取的消息都是只读消息视图。 但在历史版本3.x和4.x版本中消息不可变性没有强约束，因此如果您需要在使用过程中对消息进行中转操作，务必将消息重新初始化。

- 正确使用示例如下：

  ```java
  Message m = Consumer.receive();
  Message m2= MessageBuilder.buildFrom(m);
  Producer.send(m2);
  ```

  

- 错误使用示例如下：

  ```java
  Message m = Consumer.receive();
  m.update()；
  Producer.send(m);
  ```

# 5. 生产者（Producer）

本文介绍 Apache RocketMQ 中生产者（Producer）的定义、模型关系、内部属性、版本兼容性及使用建议。

## 5.1 定义

生产者是 Apache RocketMQ 系统中用来**构建**并**传输**消息到服务端的运行实体。

生产者通常被集成在业务系统中，将业务消息按照要求封装成 Apache RocketMQ 的[消息（Message）](https://rocketmq.apache.org/zh/docs/domainModel/04message)并发送至服务端。

在消息生产者中，可以定义如下传输行为：

- 发送方式：生产者可通过API接口设置消息发送的方式。Apache RocketMQ 支持同步传输和异步传输。
- 批量发送：生产者可通过API接口设置消息批量传输的方式。例如，批量发送的消息条数或消息大小。
- 事务行为：Apache RocketMQ 支持事务消息，对于**事务消息需要生产者配合进行事务检查等行为保障事务的最终一致性**。具体信息，请参见[事务消息](https://rocketmq.apache.org/zh/docs/featureBehavior/04transactionmessage)。

**生产者和主题的关系为多对多关系**，即同一个生产者可以向多个主题发送消息，对于平台类场景如果需要发送消息到多个主题，并不需要创建多个生产者；同一个主题也可以接收多个生产者的消息，以此可以实现生产者性能的水平扩展和容灾。 ![生产者主题关联](https://rocketmq.apache.org/zh/assets/images/producer_topic-f9a6348396228a2976e34a5ad0774314.png)

## 5.2 模型关系

在 Apache RocketMQ 的领域模型中，生产者的位置和流程如下：![生产者](https://rocketmq.apache.org/zh/assets/images/archiforproducer-ebb8ff832f6e857cbebc2c17c2044a3b.png)

1. 消息由生产者初始化并发送到Apache RocketMQ 服务端。
2. 消息按照到达Apache RocketMQ 服务端的顺序存储到主题的指定队列中。
3. 消费者按照指定的订阅关系从Apache RocketMQ 服务端中获取消息并消费。

## 5.3 内部属性

**客户端ID**

- 定义：生产者客户端的标识，用于区分不同的生产者。集群内全局唯一。
- 取值：客户端ID由Apache RocketMQ 的SDK自动生成，主要用于日志查看、问题定位等运维场景，不支持修改。

**通信参数**

- 接入点信息 **（必选）** ：连接服务端的接入地址，用于识别服务端集群。 接入点必须按格式配置，建议使用域名，避免使用IP地址，防止节点变更无法进行热点迁移。
- 身份认证信息 **（可选）** ：客户端用于身份验证的凭证信息。 仅在服务端开启身份识别和认证时需要传输。
- 请求超时时间 **（可选）** ：客户端网络请求调用的超时时间。取值范围和默认值，请参见[参数限制](https://rocketmq.apache.org/zh/docs/introduction/03limits)。

**预绑定主题列表**

- 定义：Apache RocketMQ 的生产者需要将消息发送到的目标主题列表，主要作用如下：

  - 事务消息 **（必须设置）** ：事务消息场景下，生产者在故障、重启恢复时，需要检查事务消息的主题中是否有未提交的事务消息。避免生产者发送新消息后，主题中的旧事务消息一直处于未提交状态，造成业务延迟。

  - 非事务消息 **（建议设置）** ：服务端会在生产者初始化时根据预绑定主题列表，检查目标主题的访问权限和合法性，而不需要等到应用启动后再检查。

    若未设置，或后续消息发送的目标主题动态变更， Apache RocketMQ 会对目标主题进行动态补充检验。

- 约束：对于事务消息，预绑定列表必须设置，且需要和事务检查器一起配合使用。

**事务检查器**

- 定义：Apache RocketMQ 的事务消息机制中，为保证异常场景下事务的最终一致性，生产者需要主动实现事务检查器的接口。具体信息，请参见[事务消息](https://rocketmq.apache.org/zh/docs/featureBehavior/04transactionmessage)。
- 发送事务消息时，事务检查器必须设置，且需要和预绑定主题列表一起配合使用。

**发送重试策略**：

- 定义: 生产者在消息发送失败时的重试策略。具体信息，请参见[消息发送重试机制](https://rocketmq.apache.org/zh/docs/featureBehavior/05sendretrypolicy)。

## 5.4 版本兼容性

Apache RocketMQ 服务端5.x版本开始，生产者是匿名的，无需管理生产者分组（ProducerGroup）；对于历史版本服务端3.x和4.x版本，已经使用的生产者分组可以废弃无需再设置，且不会对当前业务产生影响。

## 5.5 使用建议

**不建议单一进程创建大量生产者**

Apache RocketMQ 的生产者和主题是多对多的关系，支持同一个生产者向多个主题发送消息。对于生产者的创建和初始化，建议遵循够用即可、最大化复用原则，如果有需要发送消息到多个主题的场景，无需为每个主题都创建一个生产者。

**不建议频繁创建和销毁生产者**

Apache RocketMQ 的生产者是可以重复利用的底层资源，类似数据库的连接池。因此不需要在每次发送消息时动态创建生产者，且在发送结束后销毁生产者。这样频繁的创建销毁会在服务端产生大量短连接请求，严重影响系统性能。

- 正确示例

  ```java
  Producer p = ProducerBuilder.build();
  for (int i =0;i<n;i++){
      Message m= MessageBuilder.build();
      p.send(m);
   }
  p.shutdown();
  ```

  

- 典型错误示例

  ```java
  for (int i =0;i<n;i++){
      Producer p = ProducerBuilder.build();
      Message m= MessageBuilder.build();
      p.send(m);
      p.shutdown();
    }
  ```

# 6. 消费者分组（ConsumerGroup）

本文介绍 Apache RocketMQ 中消费者分组（ConsumerGroup）的定义、模型关系、内部属性、行为约束、版本兼容性及使用建议。

## 6.1 定义

消费者分组是 Apache RocketMQ 系统中承载多个**消费行为一致**的消费者的**负载均衡**分组。

和消费者不同，消费者分组并不是运行实体，而是一个逻辑资源。在 Apache RocketMQ 中，通过消费者分组内初始化多个消费者实现消费性能的水平扩展以及高可用容灾。

在消费者分组中，统一定义以下消费行为，同一分组下的多个消费者将按照分组内统一的消费行为和负载均衡策略消费消息。

- 订阅关系：Apache RocketMQ 以消费者分组的粒度管理订阅关系，实现订阅关系的管理和追溯。具体信息，请参见[订阅关系（Subscription）](https://rocketmq.apache.org/zh/docs/domainModel/09subscription)。
- 投递顺序性：Apache RocketMQ 的服务端将消息投递给消费者消费时，支持顺序投递和并发投递，投递方式在消费者分组中统一配置。具体信息，请参见[顺序消息](https://rocketmq.apache.org/zh/docs/featureBehavior/03fifomessage)。
- 消费重试策略： 消费者消费消息失败时的重试策略，包括重试次数、死信队列设置等。具体信息，请参见[消费重试](https://rocketmq.apache.org/zh/docs/featureBehavior/10consumerretrypolicy)。

## 6.2 模型关系

在 Apache RocketMQ 的领域模型中，消费者分组的位置和流程如下：![消费组](https://rocketmq.apache.org/zh/assets/images/archiforconsumergroup-9d98f4f7fc0302aa2363454a552477d9.png)

1. 消息由生产者初始化并发送到Apache RocketMQ 服务端。
2. 消息按照到达Apache RocketMQ 服务端的顺序存储到主题的指定队列中。
3. 消费者按照指定的订阅关系从Apache RocketMQ 服务端中获取消息并消费。

## 6.3 内部属性

**消费者分组名称**

- 定义：消费者分组的名称，用于区分不同的消费者分组。集群内全局唯一。
- 取值：消费者分组由用户设置并创建。具体命名规范，请参见[参数限制](https://rocketmq.apache.org/zh/docs/introduction/03limits)。

**投递顺序性**

- 定义：消费者消费消息时，Apache RocketMQ 向消费者客户端投递消息的顺序。

  根据不同的消费场景，Apache RocketMQ 提供顺序投递和并发投递两种方式。具体信息，请参见[顺序消息](https://rocketmq.apache.org/zh/docs/featureBehavior/03fifomessage)。

- 取值：默认投递方式为并发投递。

**消费重试策略**

- 定义：消费者消费消息失败时，系统的重试策略。消费者消费消息失败时，系统会按照重试策略，将指定消息投递给消费者重新消费。具体信息，请参见[消费重试](https://rocketmq.apache.org/zh/docs/featureBehavior/10consumerretrypolicy)。
- 取值：重试策略包括：
  - 最大重试次数：表示消息可以重新被投递的最大次数，超过最大重试次数还没被成功消费，消息将被投递至死信队列或丢弃。
  - 重试间隔：Apache RocketMQ 服务端重新投递消息的间隔时间。 最大重试次数和重试间隔的取值范围及默认值，请参见[参数限制](https://rocketmq.apache.org/zh/docs/introduction/03limits)。
- 约束：重试间隔仅在PushConsumer消费类型下有效。

**订阅关系**

- 定义：当前消费者分组关联的订阅关系集合。·者订阅的主题，以及消息的过滤规则等。订阅关系由消费者动态注册到消费者分组中，Apache RocketMQ 服务端会持久化订阅关系并匹配消息的消费进度。更多信息，请参见[订阅关系（Subscription）](https://rocketmq.apache.org/zh/docs/domainModel/09subscription)。

## 6.4 行为约束

在 Apache RocketMQ 领域模型中，消费者的管理通过消费者分组实现，同一分组内的消费者共同分摊消息进行消费。因此，为了保证分组内消息的正常负载和消费，

Apache RocketMQ 要求同一分组下的所有消费者以下消费行为**保持一致**：

- **投递顺序**
- **消费重试策略**

## 6.5 版本兼容性

如行为约束中所述，同一分组内所有消费者的投递顺序和消费重试策略需要保持一致。

- Apache RocketMQ 服务端5.x版本：上述消费者的消费行为从关联的消费者分组中统一获取，因此，同一分组内所有消费者的消费行为必然是一致的，客户端无需关注。
- Apache RocketMQ 服务端3.x/4.x历史版本：上述消费逻辑由消费者客户端接口定义，因此，您需要自己在消费者客户端设置时保证同一分组下的消费者的消费行为一致。

若您使用 Apache RocketMQ 服务端5.x版本，客户端使用历史版本SDK，则消费者的消费逻辑以消费者客户端接口的设置为准。

## 6.6 使用建议

**按照业务合理拆分分组**

Apache RocketMQ 的消费者和主题是多对多的关系，对于消费者分组的拆分设计，建议遵循以下原则：

- 消费者的投递顺序一致：同一消费者分组下所有消费者的消费投递顺序是相同的，统一都是顺序投递或并发投递，不同业务场景不能混用消费者分组。
- 消费者业务类型一致：一般消费者分组和主题对应，不同业务域对消息消费的要求不同，例如消息过滤属性、消费重试策略不同。因此，不同业务域主题的消费建议使用不同的消费者分组，避免一个消费者分组消费超过10个主题。

**消费者分组管理尽量避免自动化机制**

在 Apache RocketMQ 架构中，消费分组属于状态管理类的逻辑资源，每个消费分组都会涉及关联的消费状态、堆积信息、可观测指标和监控采集数据。因此，生产环境需要严格管理消费者分组资源，请勿随意进行增、删、改、查操作。

Apache RocketMQ 虽然提供了自动创建消费者分组的功能，但是建议仅在测试环境使用，生产环境请勿打开，避免产生大量消费者分组，无法管理和回收，且浪费系统资源。

# 7. 消费者（Consumer）

本文介绍 Apache RocketMQ 中消费者（Consumer）的定义、模型关系、内部属性、行为约束、版本兼容性及使用建议。

## 7.1 定义

消费者是 Apache RocketMQ 中用来接收并处理消息的运行实体。 消费者通常被集成在业务系统中，从 Apache RocketMQ 服务端获取消息，并将消息转化成业务可理解的信息，供业务逻辑处理。

在消息消费端，可以定义如下传输行为：

- 消费者身份：消费者必须关联一个指定的消费者分组，以获取分组内统一定义的行为配置和消费状态。
- 消费者类型：Apache RocketMQ 面向不同的开发场景提供了多样的消费者类型，包括PushConsumer类型、SimpleConsumer类型、PullConsumer类型（仅推荐流处理场景使用）等。具体信息，请参见[消费者分类](https://rocketmq.apache.org/zh/docs/featureBehavior/06consumertype)。
- 消费者本地运行配置：消费者根据不同的消费者类型，控制消费者客户端本地的运行配置。例如消费者客户端的线程数，消费并发度等，实现不同的传输效果。

## 7.2 模型关系

在 Apache RocketMQ 的领域模型中，消费者的位置和流程如下：![消费者](https://rocketmq.apache.org/zh/assets/images/archiforconsumer-24914573add839fdf2ba2cbc0fcab7c4.png)

1. 消息由生产者初始化并发送到Apache RocketMQ 服务端。
2. 消息按照到达Apache RocketMQ 服务端的顺序存储到主题的指定队列中。
3. 消费者按照指定的订阅关系从Apache RocketMQ 服务端中获取消息并消费。

## 7.3 内部属性

**消费者分组名称**

- 定义：当前消费者关联的消费者分组名称，消费者必须关联到指定的消费者分组，通过消费者分组获取消费行为。更多信息，请参见[消费者分组（ConsumerGroup）](https://rocketmq.apache.org/zh/docs/domainModel/07consumergroup)。
- 取值：消费者分组为Apache RocketMQ 的逻辑资源，需要您提前通过控制台或OpenAPI创建。具体命名格式，请参见[使用限制](https://rocketmq.apache.org/zh/docs/introduction/03limits)。

**客户端ID**

- 定义：消费者客户端的标识，用于区分不同的消费者。集群内全局唯一。
- 取值：客户端ID由Apache RocketMQ 的SDK**自动生成**，主要用于日志查看、问题定位等运维场景，不支持修改。

**通信参数**

- 接入点信息 **（必选）** ：连接服务端的接入地址，用于识别服务端集群。 接入点必须按格式配置，建议使用域名，避免使用IP地址，防止节点变更无法进行热点迁移。
- 身份认证信息 **（可选）** ：客户端用于身份验证的凭证信息。 仅在服务端开启身份识别和认证时需要传输。
- 请求超时时间 **（可选）** ：客户端网络请求调用的超时时间。取值范围和默认值，请参见[参数限制](https://rocketmq.apache.org/zh/docs/introduction/03limits)。

**预绑定订阅关系列表**

- 定义：指定消费者的订阅关系列表。 Apache RocketMQ 服务端可在消费者初始化阶段，根据预绑定的订阅关系列表对目标主题进行权限及合法性校验，无需等到应用启动后才能校验。

- 取值：建议在消费者初始化阶段明确订阅关系即要订阅的主题列表，若未设置，或订阅的主题动态变更，Apache RocketMQ 会对目标主题进行动态补充校验。

**消费监听器**

- 定义：Apache RocketMQ 服务端将消息推送给消费者后，消费者调用消息消费逻辑的监听器。
- 取值：由消费者客户端本地配置。
- 约束：使用PushConsumer类型的消费者消费消息时，消费者客户端必须设置消费监听器。消费者类型的具体信息，请参见[消费者分类](https://rocketmq.apache.org/zh/docs/featureBehavior/06consumertype)。

## 7.4 行为约束

在 Apache RocketMQ 领域模型中，消费者的管理通过消费者分组实现，同一分组内的消费者共同分摊消息进行消费。因此，为了保证分组内消息的正常负载和消费，

Apache RocketMQ 要求同一分组下的所有消费者以下消费行为保持一致：

- **投递顺序**
- **消费重试策略**

## 7.5 版本兼容性

如行为约束中所述，同一分组内所有消费者的投递顺序和消费重试策略需要保持一致。

- Apache RocketMQ 服务端5.x版本：上述消费者的消费行为从关联的消费者分组中统一获取，因此，同一分组内所有消费者的消费行为必然是一致的，客户端无需关注。
- Apache RocketMQ 服务端3.x/4.x历史版本：上述消费逻辑由消费者客户端接口定义，因此，您需要自己在消费者客户端设置时保证同一分组下的消费者的消费行为一致。

若您使用 Apache RocketMQ 服务端5.x版本，客户端使用历史版本SDK，则消费者的消费逻辑以消费者客户端接口的设置为准。

## 7.6 使用建议

**不建议在单一进程内创建大量消费者**

Apache RocketMQ 的消费者在通信协议层面支持非阻塞传输模式，网络通信效率较高，并且支持多线程并发访问。因此，大部分场景下，单一进程内同一个消费分组只需要初始化唯一的一个消费者即可，开发过程中应避免以相同的配置初始化多个消费者。

**不建议频繁创建和销毁消费者**

Apache RocketMQ 的消费者是可以重复利用的底层资源，类似数据库的连接池。因此不需要在每次接收消息时动态创建消费者，且在消费完成后销毁消费者。这样频繁地创建销毁会在服务端产生大量短连接请求，严重影响系统性能。

- 正确示例

  ```java
  Consumer c = ConsumerBuilder.build();
  for (int i =0;i<n;i++){
        Message m= c.receive();
        //process message
      }
  c.shutdown();
  ```

  

- 典型错误示例

  ```java
  for (int i =0;i<n;i++){
      Consumer c = ConsumerBuilder.build();
      Message m= c.receive();
      //process message
      c.shutdown();
    }
  ```

# 8. 订阅关系（Subscription）

本文介绍 Apache RocketMQ 中订阅关系（Subscription）的定义、模型关系、内部属性及使用建议。

## 8.1 定义

订阅关系是 Apache RocketMQ 系统中**消费者获取消息、处理消息的规则和状态配置**。

订阅关系由消费者分组动态注册到服务端系统，并在后续的消息传输中按照订阅关系定义的过滤规则进行消息匹配和消费进度维护。

通过配置订阅关系，可控制如下传输行为：

- 消息过滤规则：用于控制消费者在消费消息时，选择主题内的哪些消息进行消费，设置消费过滤规则可以高效地过滤消费者需要的消息集合，灵活根据不同的业务场景设置不同的消息接收范围。具体信息，请参见[消息过滤](https://rocketmq.apache.org/zh/docs/featureBehavior/07messagefilter)。
- 消费状态：Apache RocketMQ 服务端默认提供订阅关系持久化的能力，即消费者分组在服务端注册订阅关系后，当消费者离线并再次上线后，可以获取离线前的消费进度并继续消费。

## 8.2 订阅关系判断原则

Apache RocketMQ 的订阅关系按照消费者分组和主题粒度设计，因此，一个订阅关系指的是指定某个消费者分组对于某个主题的订阅，判断原则如下：

- 不同消费者分组对于同一个主题的订阅相互独立如下图所示，消费者分组Group A和消费者分组Group B分别以不同的订阅关系订阅了同一个主题Topic A，这两个订阅关系互相独立，可以各自定义，不受影响。

  ![订阅关系不同分组](https://rocketmq.apache.org/zh/assets/images/subscription_diff_group-0b215b9bb822b4bf43c388e9155ecca1.png)

- 同一个消费者分组对于不同主题的订阅也相互独立如下图所示，消费者分组Group A订阅了两个主题Topic A和Topic B，对于Group A中的消费者来说，订阅的Topic A为一个订阅关系，订阅的Topic B为另外一个订阅关系，且这两个订阅关系互相独立，可以各自定义，不受影响。

  ![订阅关系相同分组](https://rocketmq.apache.org/zh/assets/images/subscription_one_group-77bd92b987e8264ad3c5f27b29463942.png)

## 8.3 模型关系

在 Apache RocketMQ 的领域模型中，订阅关系的位置和流程如下：![订阅关系](https://rocketmq.apache.org/zh/assets/images/archiforsubsciption-a495c04e71ed64b9403b689f9413ed08.png)

1. 消息由生产者初始化并发送到Apache RocketMQ 服务端。
2. 消息按照到达Apache RocketMQ 服务端的顺序存储到主题的指定队列中。
3. 消费者按照指定的订阅关系从Apache RocketMQ 服务端中获取消息并消费。

## 8.4 内部属性

**过滤类型**

- 定义：消息过滤规则的类型。订阅关系中设置消息过滤规则后，系统将按照过滤规则匹配主题中的消息，只将符合条件的消息投递给消费者消费，实现消息的再次分类。
- 取值：
  - TAG过滤：按照Tag字符串进行全文过滤匹配。
  - SQL92过滤：按照SQL语法对消息属性进行过滤匹配。

**过滤表达式**

- 定义：自定义的过滤规则表达式。
- 取值：具体取值规范，请参见[过滤表达式语法规范](https://rocketmq.apache.org/zh/docs/featureBehavior/07messagefilter)。

## 8.5 行为约束

**订阅关系一致**

Apache RocketMQ 是按照消费者分组粒度管理订阅关系，因此，同一消费者分组内的消费者在消费逻辑上必须保持一致，否则会出现消费冲突，导致部分消息消费异常。

- 正确示例

  ```java
  //Consumer c1
  Consumer c1 = ConsumerBuilder.build(groupA);
  c1.subscribe(topicA,"TagA");
  //Consumer c2
  Consumer c2 = ConsumerBuilder.build(groupA);
  c2.subscribe(topicA,"TagA");
  ```

  

- 错误示例

  ```java
  //Consumer c1
  Consumer c1 = ConsumerBuilder.build(groupA);
  c1.subscribe(topicA,"TagA");
  //Consumer c2
  Consumer c2 = ConsumerBuilder.build(groupA);
  c2.subscribe(topicA,"TagB");
  ```

  

## 8.6 使用建议

**建议不要频繁修改订阅关系**

在 Apache RocketMQ 领域模型中，订阅关系关联了过滤规则、消费进度等元数据和相关配置，同时系统需要保证消费者分组下的所有消费者的消费行为、消费逻辑、负载策略等一致，整体运算逻辑比较复杂。因此，不建议在生产环境中通过频繁修改订阅关系来实现业务逻辑的变更，这样可能会导致客户端一直处于负载均衡调整和变更的过程，从而影响消息接收。

---

# 9. 环境搭建

## 9.1 NameServer与BrokerServer

在RocketMQ安装中没有具体去介绍NameServer与BrokerServer,先保证RocketMQ能够正常运行.这里我们来看看在RocketMQ中NameServer与BrokerServer扮演着什么样的角色起到了什么样的作用

**现有角色分析与流程**

**相关角色**

- NameServer: 用来管理所有 BrokerServer
- BrokerServer: 存储和传输消息
- Producer: 生产者
- Consumer: 消费者

**工作流程图**

![image](https://ucc.alicdn.com/vc6xjkdrx4fec/developer-article1522321/20241029/065acf83389f4666913121dae28175f3.png)

工作流程详解

这里分俩部分进行讲解分别product发送消息和consumer消费消息

**producer发送消息**

​	在RocketMQ的架构中,生产者向BrokerServer发送消息,生产者并不知道BrokerServer是谁,甚至不知道有多少个BrokerServer.生产者会先和NameServer建立连接,因为NameServer管理着所有的BrokerServer.然后生产者就知道向哪个BrokerServer发送消息了.

**consumer消费消息**

​	消费者也会和生产者发送消息一样,先去NameServer上找到需要去哪个BrokerServer中拉去消息进行消费.BrokerServer会实时上报NameServer自己的状态,有点类似于Spring Cloud的注册中心组件.这里也对应了我们一开始需要先启动NameServer然后启动BrokerServer去指定NameServer的ip地址

**总结**

有了NameServer,生产者和消费者只需要通过NameServer建立连接无需关心BrokerServer.类似Spring Cloud中注册中心和服务之间的关系一样,也方便后期做拓展集群.

