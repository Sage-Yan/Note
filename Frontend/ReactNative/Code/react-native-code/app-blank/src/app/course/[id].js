import { Stack, useLocalSearchParams, useNavigation } from 'expo-router'
import { StyleSheet, Text, TouchableOpacity, View } from 'react-native'

const Course = () => {
  const { id } = useLocalSearchParams()

  const navigation = useNavigation()

  return (
    <View style={styles.container}>
      <Text style={styles.title}>这里是课程页</Text>
      <Text style={styles.info}>课程ID：{id}</Text>

      <TouchableOpacity onPress={() => navigation.setOptions({ title: '修改后的标题' })}>
        <Text style={styles.info}>修改标题</Text>
      </TouchableOpacity>
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
  info: {
    marginTop: 20,
    fontSize: 20,
    color: '#1f99b0',
  },
})

export default Course
