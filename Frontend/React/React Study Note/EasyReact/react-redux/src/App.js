import {useSelector, useDispatch} from 'react-redux'

// 导入actionCreator
import {increment, decrement, addToNum} from './store/moudles/counterStore'
import {fetchChannelList} from './store/moudles/channelStore'
import {useEffect} from "react";

function App() {
    const {count} = useSelector(state => state.counter)
    const {channelList} = useSelector(state => state.channel)
    const dispatch = useDispatch()
    // 使用useEffect触发异步请求执行
    useEffect(() => {
        dispatch(fetchChannelList())
    }, [])
    return (
        <div className="App">
            <button onClick={() => dispatch(decrement())}>-</button>
            {count}
            <button onClick={() => dispatch(increment())}>+</button>
            <button onClick={() => dispatch(addToNum(20))}>to 20</button>
            <button onClick={() => dispatch(addToNum(100))}>to 100</button>
            <ul>
                {channelList.map(item => <li key={item.id}>{item.name}</li>)}
            </ul>
        </div>
    );
}

export default App;
