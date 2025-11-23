import { StyleSheet, Text, View } from 'react-native'
import { Link, useRouter } from 'expo-router'

const Videos = () => {
  const router = useRouter()

  return (
    <View style={styles.container}>
      <Text style={styles.title}>这里是视频课程</Text>
      <Link href={'/course/2?title=Reactnative'} style={styles.link}>
        查看React Native课程
      </Link>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#fff',
  },
  title: {
    fontSize: 40,
    fontWeight: 'bold',
    color: '#e29447',
  },
  link: {
    marginTop: 20,
    fontSize: 20,
    color: '#1f99b0',
  },
})

export default Videos
