import { StyleSheet, Text, TouchableOpacity, View } from 'react-native'
import { SimpleLineIcons } from '@expo/vector-icons'

const NetWorkError = (props) => {
  const title = props.title || '网络异常，请稍后重试！'
  const { onReload } = props
  return (
    <View style={styles.container}>
      <SimpleLineIcons name={'drawer'} size={160} color="#F2F2F2" />
      <Text style={styles.title}>{title}</Text>
      <TouchableOpacity style={styles.reload} onPress={onReload}>
        <Text style={styles.label}>重新加载</Text>
      </TouchableOpacity>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
  title: {
    color: '#999',
  },
  reload: {
    marginTop: 10,
    backgroundColor: '#1f99b0',
    height: 40,
    borderRadius: 4,
    paddingLeft: 10,
    paddingRight: 10,
  },
  label: {
    color: 'white',
    lineHeight: 40,
  },
})
export default NetWorkError
