# ReactNative 学习笔记

Author：Shijie Yan

---

## 1. 入门基础篇

### 1.1 使用`Expo`创建`ReactNative`项目

1. 创建项目

```cmd
npx create-expo-app@latest # yes -> 并输入项目名
```

2. 启动项目

```cmd
npx expo start
```

3. `AppleStore`下载`Expo Go`软件

​	保证iphone与电脑在同一个局域网中，扫描运行即可

### 1.2 初识React

1. `React`只能返回一个根标签，可以用`</>`或者最大的<View>标签。
2. `ReactNative`文字只能用<Text>标签

### 1.3 ReactNative中的样式

```js
import {StyleSheet, Text, View} from 'react-native';

export default function App() {
    return (
        <View style={styles.container}>
            <View>
    			// 谁在前面谁优先
				<Text style={[styles.title, {fontSize: 30}]}>React Native Hello World!</Text> 
            </View>
            <View>

            </View>
        </View>
    );
}

const styles = StyleSheet.create({ // 默认自带flex属性
    container: {
        flex: 1,
        backgroundColor: '#fff',
        alignItems: 'center',
        justifyContent: 'center',
    },
    title: {
        fontSize: 22,
        fontWeight: 'bold',
        color: 'skyblue',
    }
});

```

### 1.4 Hook之useState状态管理

```jsx
import {Button, StyleSheet, View, Text} from 'react-native';
import {useState} from "react";

export default function App() {

    const [count, setCount] = useState(0);

    return (
        <View style={styles.container}>
            <Text>一共 {count} 次</Text>
            <Button title="点击" onPress={() => setCount(count + 1)} />
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        alignItems: 'center',
        justifyContent: 'center',
    }
});

```

### 1.5 使用map遍历并渲染数据

```jsx
import {Button, StyleSheet, View, Text} from 'react-native';
import {useState} from "react";

export default function App() {


    const [courses, setCourses] = useState([
        {id: 1, name: 'HTML'},
        {id: 2, name: 'CSS'},
        {id: 3, name: 'JavaScript'},
        {id: 4, name: 'React'},
        {id: 5, name: 'Node.js'},
        {id: 6, name: 'Python'},
    ]);

    return (
        <View style={styles.container}>
            {courses.map(course => (
                <Text key={course.id} style={{fontSize: 20, margin: 5}}>
                    {course.name}
                </Text>
            ))}
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        alignItems: 'center',
        justifyContent: 'center',
    }
});

```

### 1.6 运行真实项目接口

### 1.7 ReactNative发起网络请求

> ReactNative自带FetchAPI

```jsx
import {Button, StyleSheet, View, Text} from 'react-native';
import {useState} from "react";

export default function App() {


    const [courses, setCourses] = useState([]);

    const fetchCourses = async () => {
        const res = await fetch('http://localhost:8000/courses')
        const {data} = await res.json()
        setCourses(data);
    }

    return (
        <View style={styles.container}>
            {courses.map(course => (
                <Text key={course.id} style={{fontSize: 20, margin: 5}}>
                    {course.name}
                </Text>
            ))}
            <Button title="获取课程" onPress={fetchCourses}/>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        alignItems: 'center',
        justifyContent: 'center',
    }
});

```

### 1.8 useEffect 自动运行

```jsx
import {StyleSheet, View, Text, TextInput} from 'react-native';
import {useEffect, useState} from "react";

export default function App() {


    const [courses, setCourses] = useState([]);
    const [keyword, setKeyword] = useState('');

    const fetchCourses = async () => {
        const res = await fetch('http://localhost:8000/courses')
        const {data} = await res.json()
        setCourses(data);
    }

    useEffect(() => {
        fetchCourses();
    }, [keyword]);

    return (
        <View style={styles.container}>
            <Text>您搜索的关键词是:{keyword}</Text>
            <TextInput
                style={styles.input}
                placeholder="请填写要搜索的课程！"
                onChangeText={(text) => setKeyword(text)}
                defaultValue={keyword}
            />

            {courses.map(course => (
                <Text key={course.id} style={{fontSize: 20, margin: 5}}>
                    {course.name}
                </Text>
            ))}
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        alignItems: 'center',
        justifyContent: 'center',
    },
    input: {
        height: 40,
        width: 300,
        margin: 12,
        padding: 10,
        borderColor: 'gray',
        borderWidth: 1,
        borderRadius: 5,
    }
});

```

### 1.9 自定义加载中组件

```jsx
import {ActivityIndicator, StyleSheet} from "react-native";

const Loading = () => {
    return <ActivityIndicator size={"small"} color={"skyblue"} style={styles.loading}/>
}

const styles = StyleSheet.create({
    loading: {
        backgroundColor: '#fff',
        position: 'absolute',
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
        zIndex: 1
    }
})

export default Loading;
```

### 1.10 自定义网络错误组件

```jsx
import {StyleSheet, Text, View} from "react-native";
import {SimpleLineIcons} from "@expo/vector-icons";

const NetWorkError = () => {
    return (
        <View style={styles.container}>
            <SimpleLineIcons name={'drawer'} size={160} color="#F2F2F2" />
            <Text>数据加载失败，请稍后重试！</Text>
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        alignItems: 'center',
        justifyContent: 'center',
    }
})
export default NetWorkError;

```

### 1.11 实现重新加载

```jsx
import {StyleSheet, View, Text, TextInput} from 'react-native';
import {useEffect, useState} from "react";
import Loading from "./components/Loading";
import NetWorkError from "./components/NetWorkError";

export default function App() {


    const [courses, setCourses] = useState([]);
    const [keyword, setKeyword] = useState('');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(false);

    const fetchCourses = async () => {

        try {
            const res = await fetch('http://localhost:8000/courses')
            const {data} = await res.json()
            setCourses(data);
        } catch (err) {
            setError(true)
        } finally {
            setLoading(false)
        }
    }

    const onReload = async () => {
        setLoading(true)
        setError(false);
        await fetchCourses()
    }

    useEffect(() => {
        fetchCourses();
    }, [keyword]);

    if (loading) {
        return <Loading/>
    }

    if (error) {
        return <NetWorkError title='网络错误，请点击重新加载。' onReload={onReload}/>
    }
    return (
        <View style={styles.container}>
            <Text>您搜索的关键词是:{keyword}</Text>
            <TextInput
                style={styles.input}
                placeholder="请填写要搜索的课程！"
                onChangeText={(text) => setKeyword(text)}
                defaultValue={keyword}
            />

            {courses.map(course => (
                <Text key={course.id} style={{fontSize: 20, margin: 5}}>
                    {course.name}
                </Text>
            ))}
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        alignItems: 'center',
        justifyContent: 'center',
    },
    input: {
        height: 40,
        width: 300,
        margin: 12,
        padding: 10,
        borderColor: 'gray',
        borderWidth: 1,
        borderRadius: 5,
    }
});
```

```jsx
import {StyleSheet, Text, TouchableOpacity, View} from "react-native";
import {SimpleLineIcons} from "@expo/vector-icons";

const NetWorkError = (props) => {
    const title = props.title || '网络异常，请稍后重试！'
    const {onReload} = props
    return (
        <View style={styles.container}>
            <SimpleLineIcons name={'drawer'} size={160} color="#F2F2F2" />
            <Text style={styles.title}>{title}</Text>
            <TouchableOpacity style={styles.reload} onPress={onReload}>
                <Text style={styles.label}>重新加载</Text>
            </TouchableOpacity>
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        alignItems: 'center',
        justifyContent: 'center',
    },
    title: {
        color: '#999',
    },
    reload: {
        marginTop: 10,
        backgroundColor: '#1f99b0',
        height: 40,
        borderRadius: 4,
        paddingLeft: 10,
        paddingRight: 10,
    },
    label: {
        color: 'skyblue',
        lineHeight: 40,
    }
})
export default NetWorkError;
```

### 1.12 环境变量与封装Fetch请求

1. 新建`.env`文件
   - 必须大写
   - 必须以`EXPO_URL`开头
2. 安装`urlcat`包

```cmd
npm i urlcat
```

3. 封装`request.js`

```jsx
import urlcat from "urlcat";

const request = async (url, {method = 'GET', params, body} = {}) => {

    // 完整请求地址
    const apiUrl = process.env.EXPO_PUBLIC_API_URL
    const requestUrl = urlcat(apiUrl, url, params)

    // 请求头
    const headers = {
        Accept: 'application/json',
        'Content-Type': 'application/json',
        // TODO Token
    }

    const config = {
        method,
        headers,
        ...(body && {body: JSON.stringify(body)}),
    }

    const response = await fetch(requestUrl, config);

    if (!response.ok) {
        // TODO 登录超时处理
        const {message, errors} = await response.json().catch(() => null);

        const error = new Error(message);
        error.status = response.status;
        error.errors = errors;
        throw error;
    }

    return await response.json()
}

export default request;

export const get = (url, params) => request(url, {method: 'GET', params});

export const post = (url, body) => request(url, {method: 'POST', body});

export const put = (url, body) => request(url, {method: 'PUT', body});

export const del = (url) => request(url, {method: 'DELETE'});

export const patch = (url, body) => request(url, {method: 'PATCH', body});
```

### 1.13 自定义Hook请求

```jsx
import {useEffect, useState} from "react";
import request, {get} from "../utils/request";

/**
 * 自定义 Hooks 获取数据
 * @param url API 请求路径
 * @param params 查询参数
 */
const useFetchData = (url, params = {}) => {
    const [data, setData] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(false);

    const fetchData = async () => {
        try {
            const {data} = await get(url, params);
            setData(data);
        } catch (err) {
            setError(true)
        } finally {
            setLoading(false)
        }
    }

    /**
     * 重新加载数据
     * @returns {Promise<void>}
     */
    const onReload = async () => {
        setLoading(true)
        setError(false);
        await fetchData()
    }

    /**
     * 当依赖参数是一个对象或者引用类型时，如params
     * 即使它的内容没有变化，每次渲染时它的引用地址也会变化
     * 会导致 useEffect 重复执行
     * 这里通过 JSON.stringify(params) 将对象转换为字符串进行比较
     */
    useEffect(() => {
        fetchData()
        console.log('运行了')
    }, [url, JSON.stringify(params)]);

    return {
        data,
        loading,
        error,
        setData,
        onReload
    }
}

export default useFetchData;
```

```jsx
import {StyleSheet, View, Text, TextInput} from 'react-native';
import {useState} from "react";
import Loading from "./components/Loading";
import NetWorkError from "./components/NetWorkError";
import useFetchData from "./hooks/useFetchData";

export default function App() {

    const [keyword, setKeyword] = useState('');

    const {data, loading, error, onReload} = useFetchData('/search', {q: keyword})

    const {courses} = data

    // 加载数据
    if (loading) {
        return <Loading/>
    }

    // 网络错误
    if (error) {
        return <NetWorkError title='网络错误，请点击重新加载。' onReload={onReload}/>
    }

    return (
        <View style={styles.container}>
            <Text>您搜索的关键词是:{keyword}</Text>
            <TextInput
                style={styles.input}
                placeholder="请填写要搜索的课程！"
                onChangeText={(text) => setKeyword(text)}
                defaultValue={keyword}
            />

            {courses.map(course => (
                <Text key={course.id} style={{fontSize: 20, margin: 5}}>
                    {course.name}
                </Text>
            ))}
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        alignItems: 'center',
        justifyContent: 'center',
    },
    input: {
        height: 40,
        width: 300,
        margin: 12,
        padding: 10,
        borderColor: 'gray',
        borderWidth: 1,
        borderRadius: 5,
    }
});
```

### 1.14 useReducer统一管理状态

```jsx
// 初始状态
import {get} from "../utils/request";
import {useEffect, useReducer} from "react";

const initialState = {
    data: {},
    loading: true,
    error: false
};

// 定义 action 类型
const FETCH_SUCCESS = 'FETCH_SUCCESS';
const FETCH_ERROR = 'FETCH_ERROR';
const SET_DATA = 'SET_DATA';
const RELOAD_START = 'RELOAD_START';

// 定义 reducer 函数
const reducer = (state, action) => {
    switch (action.type) {
        case FETCH_SUCCESS:
            return {
                ...state,
                data: action.payload,
                loading: false,
                error: false
            };
        case FETCH_ERROR:
            return {
                ...state,
                loading: false,
                error: true
            };
        case SET_DATA:
            return {
                ...state,
                data: action.payload
            };
        case RELOAD_START:
            return {
                ...state,
                loading: true,
                error: false
            };
        default:
            return state;
    }
};

const useFetchData = (url, params = {}) => {
    // 使用 useReducer 管理状态
    const [state, dispatch] = useReducer(reducer, initialState);

    const fetchData = async () => {
        try {
            const {data} = await get(url, params);
            dispatch({type: FETCH_SUCCESS, payload: data});
        } catch (err) {
            dispatch({type: FETCH_ERROR});
        }
    }

    const setData = (data) => {
        dispatch({type: SET_DATA, payload: data});
    }

    const onReload = async () => {
        dispatch({type: RELOAD_START});
        await fetchData()
    }

    useEffect(() => {
        fetchData()
    }, [url, JSON.stringify(params)]);

    return {
        ...state,
        setData,
        onReload
    };

}

export default useFetchData;

```

### 1.15 useContext全局共享数据

```jsx
import {View, Text, StyleSheet} from 'react-native';
import { createContext, useContext} from "react";

const themes = {
    light: {
        backgroundColor: '#fff',
        color: '#000',
    },
    dark: {
        backgroundColor: '#000',
        color: '#fff',
    }
}

// 创建一个主题上下文
const ThemeContext = createContext(themes.light);

export default function A() {
    return (
        <ThemeContext.Provider value={themes.dark}>
            <B/>
        </ThemeContext.Provider>
    )
}

const B = () => {
    return <C />
}

const C = () => {
    const theme = useContext(ThemeContext);

    return (
        <View style={[styles.container, {backgroundColor: theme.backgroundColor}]}>
            <Text style={{color: theme.color, fontSize: 30}}>Hello, World!</Text>
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        alignItems: 'center',
        justifyContent: 'center',
    }
})

```

### 1.16 使用ScrollView组件

```jsx
import {Text, StyleSheet, ScrollView, RefreshControl} from 'react-native';
import {SafeAreaView} from "react-native-safe-area-context";
import {useState} from "react";

export default function App() {
    const [refreshing, setRefreshing] = useState(false);

    const onRefresh = () => {
        setRefreshing(true);
        // 模拟网络请求
        setTimeout(() => {
            setRefreshing(false);
        }, 2000);
    }


    return (
        <SafeAreaView style={styles.container}>
            <ScrollView
                // 设置下拉刷新
                refreshControl={
                    <RefreshControl refreshing={refreshing} onRefresh={onRefresh} tintColor={'skyblue'}/>
                }>
                <Text style={styles.content}>
                    将进酒
                    君不见，黄河之水天上来，奔流到海不复回。
                    君不见，高堂明镜悲白发，朝如青丝暮成雪。
                    人生得意须尽欢，莫使金樽空对月。
                    天生我材必有用，千金散尽还复来。
                    烹羊宰牛且为乐，会须一饮三百杯。
                    岑夫子，丹丘生，将进酒，杯莫停。
                    与君歌一曲，请君为我倾耳听。
                    钟鼓馔玉不足贵，但愿长醉不复醒。
                    古来圣贤皆寂寞，惟有饮者留其名。
                    陈王昔时宴平乐，斗酒十千恣欢谑。
                    主人何为言少钱，径须沽取对君酌。
                    五花马，千金裘，呼儿将出换美酒，与尔同销万古愁。
                </Text>
            </ScrollView>
        </SafeAreaView>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff'
    },
    content: {
        fontSize: 60,
    }
})

```

### 1.17 FlatList组件

```jsx
import {Text, StyleSheet, ScrollView, RefreshControl, FlatList} from 'react-native';
import {SafeAreaView} from "react-native-safe-area-context";
import {useState} from "react";

export default function App() {

    const [refreshing, setRefreshing] = useState(false);

    const onRefresh = () => {
        setRefreshing(true);
        // 模拟网络请求
        setTimeout(() => {
            setRefreshing(false);
        }, 2000);
    }

    const data = [
        // 30首诗
        {"id": 1, "name": "静夜思"},
        {"id": 2, "name": "将进酒"},
        {"id": 3, "name": "登鹳雀楼"},
        {"id": 4, "name": "春晓"},
        {"id": 5, "name": "望庐山瀑布"},
        {"id": 6, "name": "黄鹤楼送孟浩然之广陵"},
        {"id": 7, "name": "早发白帝城"},
        {"id": 8, "name": "送元二使安西"},
        {"id": 9, "name": "夜泊牛渚怀古"},
        {"id": 10, "name": "题西林壁"},
        {"id": 11, "name": "九月九日忆山东兄弟"},
        {"id": 12, "name": "泊秦淮"},
        {"id": 13, "name": "江南逢李龟年"},
        {"id": 14, "name": "芙蓉楼送辛渐"},
        {"id": 15, "name": "凉州词"},
        {"id": 16, "name": "登高"},
        {"id": 17, "name": "望天门山"},
        {"id": 18, "name": "早春呈水部张十八员外"},
        {"id": 19, "name": "江雪"},
        {"id": 20, "name": "渔歌子"},
        {"id": 21, "name": "题都城南庄"},
        {"id": 22, "name": "秋夕"},
        {"id": 23, "name": "赋得古原草送别"},
        {"id": 24, "name": "泊船瓜洲"},
        {"id": 25, "name": "长歌行"},
        {"id": 26, "name": "春望"},
        {"id": 27, "name": "江畔独步寻花"},
        {"id": 28, "name": "黄州快哉亭记"},
        {"id": 29, "name": "赤壁赋"},
        {"id": 30, "name": "念奴娇·赤壁怀古"}
    ]

    // map 无论数据是否在屏幕可见范围内都要渲染，造成性能浪费， 使用FlatList优化
    const renderItem = ({item}) => (
        <Text style={styles.content}>{item.name}</Text>
    )
    const onEndReached = () => {
        console.log('到达底部，加载更多数据')
    }
    return (
        <SafeAreaView style={styles.container}>
            <FlatList
                data={data}
                renderItem={renderItem}
                keyExtractor={item => item.id}
                // horizontal={true}
                ListHeaderComponent={() => <Text style={styles.content}>古诗三十首</Text>}
                ListFooterComponent={() => <Text style={styles.content}>—— 结束 ——</Text>}
                // 下拉刷新
                refreshControl={
                    <RefreshControl refreshing={refreshing} onRefresh={onRefresh}/>
                }
                // 到底部加载更多
                onEndReached={onEndReached}
                onEndReachedThreshold={0.1}
            />
        </SafeAreaView>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff'
    },
    content: {
        textAlign: 'center',
        fontSize: 40,
        margin: 10
    }
})

```

### 1.18 常用其他组件

#### 1.18.1 Switch

```jsx
import React, { useState } from "react";
import {Switch, StyleSheet, View} from "react-native";
import {SafeAreaView} from "react-native-safe-area-context";

const App = () => {
    const [isEnabled, setIsEnabled] = useState(false);
    const toggleSwitch = () => setIsEnabled(previousState => !previousState);

    return (
        <View style={{ flex: 1 }}>
            <SafeAreaView style={styles.container}>
                <Switch onValueChange={toggleSwitch} value={isEnabled}/>
            </SafeAreaView>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: "center",
        justifyContent: "center",
        backgroundColor: "#fff",
    }
});

export default App;

```

#### 1.18.2 Alert

```jsx
import React, { useState } from "react";
import { View, StyleSheet, Button, Alert } from "react-native";

const App = () => {
    const createTwoButtonAlert = () =>
        Alert.alert(
            "你点我撒？",
            "你又想干嘛？",
            [
                {
                    text: "取消",
                    onPress: () => console.log("Cancel Pressed"),
                    style: "cancel"
                },
                { text: "确定", onPress: () => console.log("OK Pressed") }
            ]
        );

    const createThreeButtonAlert = () =>
        Alert.alert(
            "你再点我试试？",
            "你还想干嘛？",
            [
                {
                    text: "稍后再说",
                    onPress: () => console.log("Ask me later pressed")
                },
                {
                    text: "取消",
                    onPress: () => console.log("Cancel Pressed"),
                    style: "cancel"
                },
                { text: "确定", onPress: () => console.log("OK Pressed") }
            ]
        );

    return (
        <View style={styles.container}>
            <Button title={"2-Button Alert"} onPress={createTwoButtonAlert} />
            <Button title={"3-Button Alert"} onPress={createThreeButtonAlert} />
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: "space-around",
        alignItems: "center"
    }
});

export default App;

```

#### 1.18.3 Dimensions

> 获取当前设备的宽和高

```jsx
import {StyleSheet, Text, Dimensions, View} from 'react-native';

const {width, height} = Dimensions.get('window');

const App = () => {

    return (
        <View style={styles.container}>
            <Text>
                屏幕宽度 : {width} {'\n'}
                屏幕高度 : {height}
            </Text>
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    }
});

export default App;
```

#### 1.18.4 StatusBar

> 顶部电量...

```jsx
import {StyleSheet, StatusBar} from 'react-native';

const App = () => {

    return (
       <StatusBar/>
    );
};

export default App;
```

#### 1.18.5 Image

...

## 2. 路由基础篇

### 2.1 安装配置Expo Router

#### 2.1.1 安装依赖

```cmd
npx expo install expo-router react-native-safe-area-context react-native-screens expo-linking expo-constants expo-status-bar
```

#### 2.1.2 设置入口点`package.js`

```js
"main": "expo-router/entry",
```

#### 2.1.3 删除无用文件

1. 删除根目录`index.js`
2. 删除根目录`App.js`

#### 2.1.4 新建app目录

1. 根目录新建`app`目录
2. 新建`index.js`文件

#### 2.1.5 配置深度链接

> 如下配置后，将来可以通过`myapp://course/react-native`从别的应用或者浏览器直接跳转到你的App的对应页面。

1. 再`app.json`中配置

```json
"scheme": "myapp",
```

### 2.2 使用`Link`组件跳转

```jsx
import {StyleSheet, Text, TouchableOpacity, View} from "react-native";
import {Link} from "expo-router";

const Index = () => {
    return (
        <View style={styles.container}>
            <Text style={styles.title}>这里是首页</Text>

            <Link href="/details" style={styles.link} asChild={true}>
                <TouchableOpacity>
                    <Text style={styles.buttonText}>
                        跳转到详情页
                    </Text>
                </TouchableOpacity>
            </Link>
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#fff',
    },
    title: {
        fontSize: 40,
        fontWeight: 'bold',
        color: '#e29447',
    },
    link: {
        marginTop: 20,
        fontSize: 20,
        color: '#1f99b0',
    },
    buttonText: {
        color: 'blue',
        fontWeight: 'bold',
    }

})

export default Index;
```

```jsx
import {StyleSheet, Text, View} from "react-native";
import {Link} from "expo-router";

const Details = () => {
    return (
        <View style={styles.container}>
            <Text style={styles.title}>这里是详情页</Text>

            <Link href="../.." style={styles.link}>
                <Text>返回首页</Text>
            </Link>
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#fff',
    },
    title: {
        fontSize: 40,
        fontWeight: 'bold',
        color: '#e29447',
    },
    link: {
        marginTop: 20,
        fontSize: 20,
        color: '#1f99b0',
    }
})

export default Details;

```

### 2.3 布局文件、Stack、Slot

#### 2.3.1 布局文件

> 固定文件名称`_layout.js`

#### 2.3.2 Stack

```jsx
import {Stack} from "expo-router";

const Layout = () => {
    return <Stack
        screenOptions={{
            headerTitleAlign: 'center',
            animation: 'slide_from_right'
        }}
    />
}

export default Layout
```

#### 2.3.3 Slot插槽

```jsx
import {SafeAreaView} from "react-native-safe-area-context";
import {StyleSheet, Text} from "react-native";
import {Slot} from "expo-router";

const Layout = () => {
    return (
        <SafeAreaView style={styles.container}>
            <Text style={styles.header}>我的APP</Text>
            <Slot/>
            <Text style={styles.footer}>Changle APP Inc.</Text>
        </SafeAreaView>
    )
}
const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
    header: {
        fontSize: 24,
        textAlign: "center",
    },
    footer: {
        fontSize: 18,
        textAlign: "center",
    }
})

export default Layout

```

### 2.4 使用`router`方法跳转

```jsx
import {StyleSheet, Text, TouchableOpacity, View} from "react-native";
import {useRouter} from "expo-router";

const Index = () => {
    const router = useRouter()
    return (
        <View style={styles.container}>
            <Text style={styles.title}>这里是首页</Text>
            <TouchableOpacity onPress={() => router.navigate('/details')}>
                <Text style={styles.buttonText}>
                    跳转（navigate）
                </Text>
            </TouchableOpacity>
            <TouchableOpacity onPress={() => router.replace('/details')}>
                <Text style={styles.buttonText}>
                    替换（replace）
                </Text>
            </TouchableOpacity>
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#fff',
    },
    title: {
        fontSize: 40,
        fontWeight: 'bold',
        color: '#e29447',
    },
    link: {
        marginTop: 20,
        fontSize: 20,
        color: '#1f99b0',
    },
    buttonText: {
        marginTop: 20,
        fontSize: 18,
        color: 'blue',
        fontWeight: 'bold',
    }

})

export default Index;

```

```jsx
import {StyleSheet, Text, TouchableOpacity, View} from "react-native";
import {useRouter} from "expo-router";

const Details = () => {
    const router = useRouter()
    return (
        <View style={styles.container}>
            <Text style={styles.title}>这里是详情页</Text>
            <TouchableOpacity onPress={() => router.navigate('/details')}>
                <Text style={styles.buttonText}>
                    再次跳转（navigate）
                </Text>
            </TouchableOpacity>
            <TouchableOpacity onPress={() => router.push('/details')}>
                <Text style={styles.buttonText}>
                    强行推入（push）
                </Text>
            </TouchableOpacity>
            <TouchableOpacity onPress={() => router.back()}>
                <Text style={styles.buttonText}>
                    返回（back）
                </Text>
            </TouchableOpacity>
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#fff',
    },
    title: {
        fontSize: 40,
        fontWeight: 'bold',
        color: '#e29447',
    },
    link: {
        marginTop: 20,
        fontSize: 20,
        color: '#1f99b0',
    },
    buttonText: {
        marginTop: 20,
        fontSize: 18,
        color: 'blue',
        fontWeight: 'bold',
    }

})

export default Details;

```

### 2.5 动态路由与参数传递

> 使用动态路由文件，创建`/course/[id].js`，文件命名为`[id].js`

```jsx
import {StyleSheet, Text, View} from "react-native";
import {Link, useRouter} from "expo-router";

const Index = () => {
    const router = useRouter()
    return (
        <View style={styles.container}>
            <Text style={styles.title}>这里是首页</Text>
            <Link href={'/course/1'} style={styles.link}>
                路由传参
            </Link>
            <Link
                style={styles.link}
                href={{
                    pathname: '/course/[id]',
                    params: {id: 2}
                }}>
                路由传参
            </Link>
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#fff',
    },
    title: {
        fontSize: 40,
        fontWeight: 'bold',
        color: '#e29447',
    },
    link: {
        marginTop: 20,
        fontSize: 20,
        color: '#1f99b0',
    }
})

export default Index;

```

```jsx
import {useLocalSearchParams} from "expo-router";
import {StyleSheet, Text, View} from "react-native";

const Course = () => {
    const {id} = useLocalSearchParams()

    return (
        <View style={styles.container}>
            <Text style={styles.title}>
                这里是课程页
            </Text>

            <Text style={styles.info}>
                课程ID：{id}
            </Text>
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#fff',
    },
    title: {
        fontSize: 40,
        fontWeight: 'bold',
        color: '#e29447',
    },
    info: {
        marginTop: 20,
        fontSize: 20,
        color: '#1f99b0',
    }
})

export default Course;

```

### 2.6 设置导航栏标题

```jsx
import {StyleSheet} from "react-native";
import {Stack} from "expo-router";

const Layout = () => {
    return <Stack
        screenOptions={{
            headerTitleAlign: 'center',
            animation: 'slide_from_right'
        }}
    />
}
const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
    header: {
        fontSize: 24,
        textAlign: "center",
    },
    footer: {
        fontSize: 18,
        textAlign: "center",
    }
})

export default Layout
```

```jsx
import {StyleSheet, Text, View} from "react-native";
import {Link, Stack, useRouter} from "expo-router";

const Index = () => {

    const router = useRouter()

    return (
        <View style={styles.container}>
            <Stack.Screen options={{title: '首页'}}/>

            <Text style={styles.title}>这里是首页</Text>
            <Link href={'/course/1?title=Node.js'} style={styles.link}>
                路由传参
            </Link>
            <Link
                style={styles.link}
                href={{
                    pathname: '/course/[id]',
                    params: {id: 2, title: 'React Native'}
                }}>
                路由传参
            </Link>
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#fff',
    },
    title: {
        fontSize: 40,
        fontWeight: 'bold',
        color: '#e29447',
    },
    link: {
        marginTop: 20,
        fontSize: 20,
        color: '#1f99b0',
    }
})

export default Index;
```

```jsx
import {Stack, useLocalSearchParams, useNavigation} from "expo-router";
import {StyleSheet, Text, TouchableOpacity, View} from "react-native";

const Course = () => {
    const {id,title} = useLocalSearchParams()

    const navigation = useNavigation();

    return (
        <View style={styles.container}>
            <Stack.Screen options={{title: title}}/>
            <Text style={styles.title}>这里是课程页</Text>
            <Text style={styles.info}>课程ID：{id}</Text>

            <TouchableOpacity onPress={() => navigation.setOptions({title: '修改后的标题'})}>
                <Text style={styles.info}>修改标题</Text>
            </TouchableOpacity>
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#fff',
    },
    title: {
        fontSize: 40,
        fontWeight: 'bold',
        color: '#e29447',
    },
    info: {
        marginTop: 20,
        fontSize: 20,
        color: '#1f99b0',
    }
})

export default Course;

```

### 2.7 布局文件配置导航栏

```jsx
import {StyleSheet} from "react-native";
import {Stack} from "expo-router";

const Layout = () => {
    return <Stack
        screenOptions={{
            headerTitleAlign: 'center',
            animation: 'slide_from_right',
            headerStyle: { // 导航栏整体样式
                backgroundColor: '#e29447',
            },
            headerTintColor: '#fff', // 导航栏中文字 按钮 图标的颜色
            headerTitleStyle: { // 导航栏标题样式
                fontWeight: 'bold',
            }
        }}
    >
        <Stack.Screen name='index' options={{title: '首页'}}/>
        <Stack.Screen
            name='course/[id]'
            options={({route}) => ({
                title: route.params?.title || '课程详情'
            })}
        />
    </Stack>
}
const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
    header: {
        fontSize: 24,
        textAlign: "center",
    },
    footer: {
        fontSize: 18,
        textAlign: "center",
    }
})

export default Layout

```

### 2.8 使用自定义组件配置导航栏

```jsx
import {Image, StyleSheet, TouchableOpacity, View} from "react-native";
import { SimpleLineIcons } from '@expo/vector-icons';
import {Link, Stack} from "expo-router";

const LogoTitle = () => {
    return <Image style={styles.logo} contentFit="contain" source={require('../assets/icon.png')}/>
}

const HeaderButton = (props) => {
    const {name, ...rest} = props
    return(
        <Link asChild {...rest}>
            <TouchableOpacity>
                <SimpleLineIcons size={20} color='#1f99b0' name={name} />
            </TouchableOpacity>
        </Link>
    )
}

const Layout = () => {
    return <Stack
        screenOptions={{
            title: '',
            headerTitleAlign: 'center',
            animation: 'slide_from_right',
            headerTintColor: '#1f99b0', // 导航栏中文字 按钮 图标的颜色
            headerTitleStyle: { // 导航栏标题样式
                fontWeight: 'bold',
                color: '#2A2929',
                fontSize: 16
            },
            headerBackButtonDisplayMode: 'minimal' // 设置返回按钮只显示箭头不显示文字
        }}
    >
        <Stack.Screen
            name='index'
            options={{
                headerTitle: props => <LogoTitle {...props}/>,
                headerLeft: () => <HeaderButton name="bell" href="/articles" style={styles.headerLeft}/>,
                headerRight: () => (
                    <>
                        <HeaderButton name="magnifier" href="/search" style={styles.headerRight}/>
                        <HeaderButton name="options" href="/setting" style={styles.headerRight}/>
                    </>
                )
            }}
        />
        <Stack.Screen
            name='course/[id]'
            options={({route}) => ({
                title: route.params?.title || '课程详情'
            })}
        />
    </Stack>
}
const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
    header: {
        fontSize: 24,
        textAlign: "center",
    },
    footer: {
        fontSize: 18,
        textAlign: "center",
    },
    logo: {
        width: 30,
        height: 30,
    },
    headerRight: {
        marginRight: 15
    }
})

export default Layout
```

### 2.9 TabBar标准配置方式

```jsx
import {Stack} from "expo-router";


const Layout = () => {
    return (
        <Stack
            screenOptions={{
                title: '',
                headerTitleAlign: 'center',
                animation: 'slide_from_right',
                headerTintColor: '#1f99b0', // 导航栏中文字 按钮 图标的颜色
                headerTitleStyle: { // 导航栏标题样式
                    fontWeight: 'bold',
                    color: '#2A2929',
                    fontSize: 16
                },
                headerBackButtonDisplayMode: 'minimal' // 设置返回按钮只显示箭头不显示文字
            }}
        >
            {/*Tabs*/}
            <Stack.Screen name="(tabs)" options={{headerShown: false}}/>

            {/*Cards*/}
            <Stack.Screen name="articles/index" options={{title: '文章'}}/>
            <Stack.Screen name="course/[id]" options={{title: '课程'}}/>
            <Stack.Screen name="setting/index" options={{title: '设置'}}/>
            <Stack.Screen name="search/index" options={{title: '搜索'}}/>

        </Stack>
    )
}

export default Layout
```

```jsx
import {Image, StyleSheet, TouchableOpacity} from "react-native";
import {SimpleLineIcons} from '@expo/vector-icons';
import {Link, Tabs} from "expo-router";

/**
 * 导航栏Logo组件
 * @returns {React.JSX.Element}
 * @constructor
 */
const LogoTitle = () => {
    return <Image style={styles.logo} contentFit="contain" source={require('../../assets/icon.png')}/>
}


const HeaderButton = (props) => {
    // ...rest ES6对象剩余运算符
    const {name, ...rest} = props;

    return (
        <Link asChild {...rest}>
            <TouchableOpacity>
                <SimpleLineIcons name={name} size={20} color="#1f99b0"/>
            </TouchableOpacity>
        </Link>
    )
}

const TabLayout = () => {
    return (
        <Tabs
            screenOptions={{
                headerTitleAlign: "center",
                headerTitle: props => <LogoTitle {...props} />,
                headerLeft: () => <HeaderButton name="bell" href="/articles" style={styles.headerLeft}/>,
                headerRight: () => (
                    <>
                        <HeaderButton name="magnifier" href="/search" style={styles.headerRight}/>
                        <HeaderButton name="options" href="/setting" style={styles.headerRight}/>
                    </>
                )
            }}
        >
            <Tabs.Screen
                name="index"
                options={{title: '发现'}}
            />
            <Tabs.Screen
                name="videos"
                options={{title: '视频课程'}}
            />
            <Tabs.Screen
                name="users"
                options={{title: '我的'}}
            />
        </Tabs>
    )
}

const styles = StyleSheet.create({
    logo: {
        width: 30,
        height: 30,
    },
    headerLeft: {
        marginLeft: 15,
    },
    headerRight: {
        marginRight: 15,
    }
})

export default TabLayout;
```

## 3. 项目实战篇

### 3.1 项目目录与Prettier格式化代码

1. 安装`Prettier`

```jsx
pnpm add -D prettier
```

2. 新建`.prettierrc.json`

```json
{
  "$schema": "https://json.schemastore.org/prettierrc",
  "semi": false,
  "singleQuote": true,
  "printWidth": 100
}
```

3. 配置`package.json`

```json
"format": "prettier --write \"**/*.{js,ts,tsx,json,md,jsx}\""
```

### 3.2 定义`src`路径别名

1. 终端执行命令

```cmd
npx expo customize babel.config.js
```

2. 安装插件

```cmd
pnpm add -D babel-plugin-module-resolver
```

3. 配置`babel.config.js`

```js
module.exports = function (api) {
  api.cache(true)
  return {
    presets: ['babel-preset-expo'],
    plugins: [
      [
        'module-resolver',
        {
          root: ['./src'],
          alias: {
            '@': './src',
          },
        },
      ],
    ],
  }
}
```

4. 新建并配置`jsconfig.json`

> 让编辑器知道路径别名

```json
{
  "compilerOptions": {
    "baseUrl": ".",
    "paths": {
      "@/*": ["./src/*"]
    }
  }
}
```

3.3 更新Expo SDK及相关依赖包

```cmd
pnpm add expo@latest
```

```cmd
npx expo install --fix
```

```cmd
npx expo-doctor
```

### 3.4 IOS26 液态玻璃导航条

```jsx
import { Image, StyleSheet, TouchableOpacity } from 'react-native'
import { SimpleLineIcons } from '@expo/vector-icons'
import { Link } from 'expo-router'
import { NativeTabs, Icon, Label } from 'expo-router/unstable-native-tabs'

const TabLayout = () => {
  return (
    <NativeTabs tintColor="#1f99b0" disableTransparentOnScrollEdge>
      <NativeTabs.Trigger name="index">
        <Label>发现</Label>
        <Icon sf={{ default: 'play.house', selected: 'play.house.fill' }} />
      </NativeTabs.Trigger>
      <NativeTabs.Trigger name="videos">
        <Label>视频课程</Label>
        <Icon sf={{ default: 'video', selected: 'video.fill' }} />
      </NativeTabs.Trigger>
      <NativeTabs.Trigger name="users">
        <Label>我的</Label>
        <Icon sf={{ default: 'person', selected: 'person.fill' }} />
      </NativeTabs.Trigger>
    </NativeTabs>
  )

export default TabLayout
```



## 4. 上架发布篇