import resolvers from './resolvers'
import { readFileSync } from 'fs'
import dataSources from './dataSources'
const { ApolloServer, gql } = require('apollo-server')
const schema = readFileSync('./src/schema/index.graphql', { encoding: 'utf-8' })
const typeDefs = gql`${schema}`

const server = new ApolloServer({ typeDefs, resolvers, dataSources })

// The `listen` method launches a web server.
server.listen().then(({ url }: { url: any }) => {
  console.log(`🚀  Server ready at ${url}`)
})
