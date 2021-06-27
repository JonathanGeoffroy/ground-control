import { CreatePlanet, Page, QueryPlanetsArgs, Resolvers } from '../__generated__/graphql'
import { DataSources } from '../dataSources'
import { PlanetDTO } from '../__generated__/dataSources/Planet/data-contracts'
import { parse } from 'query-string'

const DEFAULT_PAGE = 0
const DEFAULT_SIZE = 20

const resolvers: Resolvers = {
  Query: {
    planets: async (parent: any, args: QueryPlanetsArgs, { dataSources }: { dataSources: DataSources }) => {
      const data = await dataSources.planetAPI.getPaginated({
        page: args.page || DEFAULT_PAGE,
        size: args.size || DEFAULT_SIZE
      })

      let nextCursor: Page | undefined

      if (data._links?.next?.href) {
        const pagination = parse(data._links.next.href.split('?')[1]) as any

        nextCursor = {
          page: Number.parseInt(pagination.page)!,
          size: Number.parseInt(pagination.size)!
        }
      }

      return {
        data: data._embedded?.planetDTOList,
        nextCursor
      }
    }
  },
  Planet: {
    moons: async (parent: PlanetDTO, args: any, { dataSources }: { dataSources: DataSources }) => {
      const planetDetails = await dataSources.planetAPI.getDetails(parent.id)
      return planetDetails.moons || []
    }
  },

  Mutation: {
    createPlanet: async (parent: any, { planet }: { planet: CreatePlanet }, { dataSources }: { dataSources: DataSources }): Promise<PlanetDTO> => {
      return dataSources.planetAPI.create({
        name: planet.name,
        gravity: planet.gravity || undefined,
        size: planet.size || undefined,
        moons: planet.moons || undefined
      })
    }
  }
}

export default resolvers
