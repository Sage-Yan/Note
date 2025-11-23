import { useEffect, useState } from 'react'
import request, { get } from '@/utils/request'

/**
 * 自定义 Hooks 获取数据
 * @param url API 请求路径
 * @param params 查询参数
 */
const useFetchData = (url, params = {}) => {
  const [data, setData] = useState({})
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(false)

  const fetchData = async () => {
    try {
      const { data } = await get(url, params)
      setData(data)
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
    setError(false)
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
  }, [url, JSON.stringify(params)])

  return {
    data,
    loading,
    error,
    setData,
    onReload,
  }
}

export default useRe
