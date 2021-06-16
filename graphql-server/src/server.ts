import resolvers from './resolvers'
import dataSources from './dataSources'
import { ApolloServerExpressConfig } from 'apollo-server-express'
import { ApolloServer } from 'apollo-server'

/// <reference path="./graphql.d.ts" />
import typeDefs from './schema/index.graphql'

export default function (config: ApolloServerExpressConfig = {}) {
  return new ApolloServer({
    typeDefs,
    resolvers,
    dataSources: (): any => dataSources(),
    ...config
  })
}
