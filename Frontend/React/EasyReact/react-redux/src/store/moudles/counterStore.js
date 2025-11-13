import {createSlice} from "@reduxjs/toolkit"

const counterStore = createSlice({
    name: 'counter',
    // 初始化state
    initialState: {
        count: 0
    },
    // 编写修改数据的方法
    reducers: {
        increment: (state) => {
            state.count++
        },
        decrement: (state) => {
            state.count--
        }
    }
})

// 结构出来actionCreator函数
const {increment, decrement} = counterStore.actions
// 获取reducer
const reducer = counterStore.reducer

// 以按需导出方式导出actionCreator
export {increment, decrement}
// 以默认导出的方式导出reducer
export default reducer
