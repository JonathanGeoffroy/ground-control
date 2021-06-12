import { setupServer } from 'msw/node'
import { rest } from 'msw'
import { PlanetDetailsDTO, PlanetDTO } from '../src/__generated__/dataSources/Planet/data-contracts'

const planets: PlanetDetailsDTO[] = [
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
export default setupServer(
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
  })
)
