// 导入样式
import './index.css'

const style = {
    color: 'red',
    fontSize: '50px',
}

function App() {
    return (
        <div className="App">
           {/*行内样式控制两种方式*/}
            <span style={{color:'red', fontSize:'50px'}}>This is span</span>
            <br/>
            <span style={style}>This is span</span>
            {/*通过class类名控制*/}
            <span className="foo">This is class foo</span>
        </div>
    );
}

export default App;
