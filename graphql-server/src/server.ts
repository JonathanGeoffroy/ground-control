import resolvers from './resolvers'
import { readFileSync } from 'fs'
import dataSources from './dataSources'
import { ApolloServerExpressConfig } from 'apollo-server-express'
import { ApolloServer, gql } from 'apollo-server'

const schema = readFileSync('./src/schema/index.graphql', { encoding: 'utf-8' })
const typeDefs = gql`${schema}`

export default function (config :ApolloServerExpressConfig = {}) {
  return new ApolloServer({ typeDefs, resolvers, dataSources: (): any => dataSources(), ...config })
}
