import { setupServer } from 'msw/node'
import { ResponseComposition, rest, RestRequest } from 'msw'
import {
  CreatePlanetDTO,
  PagedModelPlanetDTO,
  PlanetDetailsDTO,
  PlanetDTO
} from '../src/__generated__/dataSources/Planet/data-contracts'

import config from '../src/config'

const { SERVICE_PLANET: baseUrl } = config

const INITIAL_PLANETS: PlanetDetailsDTO[] = [
  {
    id: 'mars-id',
    name: 'Mars',
    gravity: 1.2,
    size: 2345.11,
    moons: [
      {
        id: 'mars-moon-id',
        name: 'Mars-Moon'
      }
    ]
  },
  {
    id: 'venus-id',
    name: 'Venus',
    gravity: 2.3,
    moons: [
      {
        id: 'venus-moon-id',
        name: 'Venus-Moon'
      }
    ]
  }
]

let planets: PlanetDetailsDTO[] = [...INITIAL_PLANETS]

afterEach(() => {
  planets = [...INITIAL_PLANETS]
})

function paginate (page: number, size: number) {
  return planets.slice(page * size, (page + 1) * size)
}

const mswServer = setupServer(
  rest.get(`${baseUrl}/planet/paginated`,
    (req, res, ctx) => {
      const page = Number.parseInt(req.url.searchParams.get('page')) || 0
      const size = Number.parseInt(req.url.searchParams.get('size')) || 20
      const paginated = paginate(page, size)

      return res(
        ctx.status(200),
        ctx.json<PagedModelPlanetDTO>({
          _embedded: {
            planetDTOList: paginated.map(planet => ({
              id: planet.id,
              name: planet.name,
              gravity: planet.gravity,
              size: planet.size
            }))
          },
          _links: paginate(page + 1, size).length
            ? {
                next: {
                  href: `${baseUrl}/planet/paginated?page=${page + 1}&size=${size}`
                }
              }
            : undefined
        })
      )
    }
  ),
  rest.get(`${baseUrl}/planet/:id`, (req, res, ctx) => {
    const { id } = req.params
    return res(
      ctx.status(200),
      ctx.json(planets.find(planet => planet.id === id))
    )
  }),

  rest.post(`${baseUrl}/planet`, (req: RestRequest<CreatePlanetDTO>, res: ResponseComposition<PlanetDTO>, ctx) => {
    const dto = req.body

    const planet: PlanetDetailsDTO = {
      id: Math.random().toString(),
      name: dto.name,
      size: dto.size,
      gravity: dto.gravity,
      moons: dto.moons?.map(moon => ({
        id: Math.random().toString(),
        name: moon.name
      })) || []
    }

    planets.push(planet)

    return res(
      ctx.status(200),
      ctx.json(planet)
    )
  })
)

beforeAll(() => {
  mswServer.listen()
})

afterAll(() => {
  mswServer.close()
})

export default mswServer
