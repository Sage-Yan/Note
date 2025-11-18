import {createSlice} from "@reduxjs/toolkit"
import axios from 'axios'

const channelStore = createSlice({
    name: 'channel',
    // 初始化state
    initialState: {
        channelList: []
    },
    // 编写修改数据的方法
    reducers: {
        setChannels: (state, action) => {
            state.channelList = action.payload
        }
    }
})

// 异步请求部分
const {setChannels} = channelStore.actions

const fetchChannelList = () => {
    return async (dispatch) => {
        const res = await axios.get("http://geek.itheima.net/v1_0/channels")
        dispatch(setChannels(res.data.data.channels))
    }
}

export {fetchChannelList}

const reducer = channelStore.reducer

export default reducer

