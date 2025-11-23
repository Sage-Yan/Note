import { StyleSheet, Text, View } from 'react-native'
import { useRouter } from 'expo-router'

const Users = () => {
  const router = useRouter()

  return (
    <View style={styles.container}>
      <Text style={styles.title}>这里是用户页</Text>
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
})

export default Users
