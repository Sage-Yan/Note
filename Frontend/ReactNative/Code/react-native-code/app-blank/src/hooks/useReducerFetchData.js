// 初始状态
import { get } from '@/utils/request'
import { useEffect, useReducer } from 'react'

const initialState = {
  data: {},
  loading: true,
  error: false,
}

// 定义 action 类型
const FETCH_SUCCESS = 'FETCH_SUCCESS'
const FETCH_ERROR = 'FETCH_ERROR'
const SET_DATA = 'SET_DATA'
const RELOAD_START = 'RELOAD_START'

// 定义 reducer 函数
const reducer = (state, action) => {
  switch (action.type) {
    case FETCH_SUCCESS:
      return {
        ...state,
        data: action.payload,
        loading: false,
        error: false,
      }
    case FETCH_ERROR:
      return {
        ...state,
        loading: false,
        error: true,
      }
    case SET_DATA:
      return {
        ...state,
        data: action.payload,
      }
    case RELOAD_START:
      return {
        ...state,
        loading: true,
        error: false,
      }
    default:
      return state
  }
}

const useFetchData = (url, params = {}) => {
  // 使用 useReducer 管理状态
  const [state, dispatch] = useReducer(reducer, initialState)

  const fetchData = async () => {
    try {
      const { data } = await get(url, params)
      dispatch({ type: FETCH_SUCCESS, payload: data })
    } catch (err) {
      dispatch({ type: FETCH_ERROR })
    }
  }

  const setData = (data) => {
    dispatch({ type: SET_DATA, payload: data })
  }

  const onReload = async () => {
    dispatch({ type: RELOAD_START })
    await fetchData()
  }

  useEffect(() => {
    fetchData()
  }, [url, JSON.stringify(params)])

  return {
    ...state,
    setData,
    onReload,
  }
}

export default useFetchData
