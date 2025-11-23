import { StyleSheet, Text, View } from 'react-native'

const Index = () => {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>这里是通知列表页</Text>
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
    color: '#4f9df7',
  },
})

export default Index
