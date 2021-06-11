import { Resolvers } from '../__generated__/graphql'
import { DataSources } from '../dataSources'
import { PlanetDTO } from '../__generated__/dataSources/Planet/data-contracts'

const resolvers: Resolvers = {
  Query: {
    planets: async (parent: any, args: any, { dataSources }: {dataSources: DataSources}) => {
      return dataSources.planetAPI.getAll()
    }
  },
  Planet: {
    moons: async (parent: PlanetDTO, args: any, { dataSources }: {dataSources: DataSources}) => {
      const planetDetails = await dataSources.planetAPI.getDetails(parent.id)
      return planetDetails.moons || []
    }
  }
}

export default resolvers
