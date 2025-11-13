import {configureStore} from "@reduxjs/toolkit"
// 导入子模块reducer
import counterReducer from './moudles/counterStore'

// 创建store组合子模块
const store = configureStore({
    reducer: {
        counter: counterReducer
    }
})

export default store
