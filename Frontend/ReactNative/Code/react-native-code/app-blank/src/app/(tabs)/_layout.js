import { Image, StyleSheet, TouchableOpacity } from 'react-native'
import { SimpleLineIcons } from '@expo/vector-icons'
import { Link } from 'expo-router'
import { NativeTabs, Icon, Label } from 'expo-router/unstable-native-tabs'

/**
 * 导航栏Logo组件
 * @returns {React.JSX.Element}
 * @constructor
 */
const LogoTitle = () => {
  return <Image style={styles.logo} contentFit="contain" source={require('@/assets/icon.png')} />
}

const HeaderButton = (props) => {
  // ...rest ES6对象剩余运算符
  const { name, ...rest } = props

  return (
    <Link asChild {...rest}>
      <TouchableOpacity>
        <SimpleLineIcons name={name} size={20} color="#1f99b0" />
      </TouchableOpacity>
    </Link>
  )
}

const TabBarIcon = (props) => {
  return <SimpleLineIcons size={25} {...props} />
}

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
  // return (
  //   <Tabs
  //     screenOptions={{
  //       headerTitleAlign: 'center',
  //       headerTitle: (props) => <LogoTitle {...props} />,
  //       headerLeft: () => <HeaderButton name="bell" href="/articles" style={styles.headerLeft} />,
  //       headerRight: () => (
  //         <>
  //           <HeaderButton name="magnifier" href="/search" style={styles.headerRight} />
  //           <HeaderButton name="options" href="/setting" style={styles.headerRight} />
  //         </>
  //       ),
  //       tabBarActiveTintColor: '#1f99b0', // 设置 TabBar 选中项的颜色
  //     }}
  //   >
  //     <Tabs.Screen
  //       name="index"
  //       options={{
  //         title: '发现',
  //         tabBarIcon: ({ color }) => <Ionicons name="compass" size={24} color={color} />,
  //       }}
  //     />
  //     <Tabs.Screen
  //       name="videos"
  //       options={{
  //         title: '视频课程',
  //         tabBarIcon: ({ color }) => <SimpleLineIcons name="camrecorder" size={24} color={color} />,
  //       }}
  //     />
  //     <Tabs.Screen
  //       name="users"
  //       options={{
  //         title: '我的',
  //         tabBarIcon: ({ color }) => <AntDesign name="user" size={24} color={color} />,
  //       }}
  //     />
  //   </Tabs>
  // )
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
  },
})

export default TabLayout
