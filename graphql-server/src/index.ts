import server from './server'
server({ playground: true })
  .listen()
  .then(({
    url
  }: { url: string }) => {
    console.log(`🚀  Server ready at ${url}`)
  })
