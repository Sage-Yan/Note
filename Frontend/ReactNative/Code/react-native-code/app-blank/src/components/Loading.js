import { ActivityIndicator, StyleSheet } from 'react-native'

const Loading = () => {
  return <ActivityIndicator size={'small'} color={'skyblue'} style={styles.loading} />
}

const styles = StyleSheet.create({
  loading: {
    backgroundColor: '#fff',
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    zIndex: 1,
  },
})

export default Loading
