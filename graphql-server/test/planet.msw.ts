import { setupServer } from 'msw/node'
import { ResponseComposition, rest, RestRequest } from 'msw'
import { CreatePlanetDTO, PlanetDetailsDTO, PlanetDTO } from '../src/__generated__/dataSources/Planet/data-contracts'

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

const mswServer = setupServer(
  rest.get('http://localhost:8080/planet',
    (req, res, ctx) =>
      res(
        ctx.status(200),
        ctx.json<PlanetDTO[]>(planets.map(planet => ({
          id: planet.id,
          name: planet.name,
          gravity: planet.gravity,
          size: planet.size
        })))
      )
  ),
  rest.get('http://localhost:8080/planet/:id', (req, res, ctx) => {
    const { id } = req.params
    return res(
      ctx.status(200),
      ctx.json(planets.find(planet => planet.id === id))
    )
  }),

  rest.post('http://localhost:8080/planet', (req: RestRequest<CreatePlanetDTO>, res: ResponseComposition<PlanetDTO>, ctx) => {
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
