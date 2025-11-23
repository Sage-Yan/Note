import { Stack } from 'expo-router'

const Layout = () => {
  return (
    <Stack
      screenOptions={{
        title: '',
        headerTitleAlign: 'center',
        animation: 'slide_from_right',
        headerTintColor: '#1f99b0', // 导航栏中文字 按钮 图标的颜色
        headerTitleStyle: {
          // 导航栏标题样式
          fontWeight: 'bold',
          color: '#2A2929',
          fontSize: 16,
        },
        headerBackButtonDisplayMode: 'minimal', // 设置返回按钮只显示箭头不显示文字
      }}
    >
      {/*Tabs*/}
      <Stack.Screen name="(tabs)" options={{ headerShown: false }} />

      {/*Cards*/}
      <Stack.Screen name="articles/index" options={{ title: '文章' }} />
      <Stack.Screen name="course/[id]" options={{ title: '课程' }} />
      <Stack.Screen name="setting/index" options={{ title: '设置' }} />
      <Stack.Screen name="search/index" options={{ title: '搜索' }} />
    </Stack>
  )
}

export default Layout
