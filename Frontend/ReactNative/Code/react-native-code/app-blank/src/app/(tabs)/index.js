import { StyleSheet, Text, View } from 'react-native'
import { Link, useRouter } from 'expo-router'

const Index = () => {
  const router = useRouter()

  return (
    <View style={styles.container}>
      <Text style={styles.title}>这里是首页</Text>
      <Link href={'/course/1?title=Node.js'} style={styles.link}>
        查看Node.js课程
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

export default Index
