import server from '../src/server'
// @ts-ignore
import mswServer from './planet.msw'
import { Planet } from '../src/__generated__/graphql'

const app = server()

beforeAll(async () => {
  mswServer.listen()
  await app.listen()
})

afterAll(async () => {
  mswServer.close()
  return await app.stop()
})

it('handles planets', async () => {
  const result = await app.executeOperation({
    query: '{planets {id, name}}'
  })
  expect(result.errors).toBeUndefined()
  expect(result.data?.planets).toEqual<Planet[]>([
    {
      id: 'mars-id',
      name: 'Mars'
    }, {
      id: 'venus-id',
      name: 'Venus'
    }
  ])
})

it('handle planet details', async () => {
  const result = await app.executeOperation({
    query: '{planets {id, name, gravity, size, moons{id, name}}}'
  })
  expect(result.errors).toBeUndefined()
  expect(result.data?.planets).toEqual<Planet[]>([
    {
      id: 'mars-id',
      name: 'Mars',
      gravity: 1.2,
      size: 2345.11,
      moons: [{
        id: 'mars-moon-id',
        name: 'Mars-Moon'
      }]
    }, {
      id: 'venus-id',
      name: 'Venus',
      gravity: 2.3,
      size: null,
      moons: [{
        id: 'venus-moon-id',
        name: 'Venus-Moon'
      }]
    }
  ])
})
