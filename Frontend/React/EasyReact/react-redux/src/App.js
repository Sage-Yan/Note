import {useSelector, useDispatch} from 'react-redux'

// 导入actionCreator
import {increment, decrement} from './store/moudles/counterStore'

function App() {
    const {count} = useSelector(state => state.counter)
    const dispatch = useDispatch()
    return (
        <div className="App">
            <button onClick={() => dispatch(decrement())}>-</button>
            {count}
            <button onClick={() => dispatch(increment())}>+</button>
        </div>
    );
}

export default App;
