import server from './server'

server()
  .listen()
  .then(({ url }: { url: any }) => {
    console.log(`🚀  Server ready at ${url}`)
  })
