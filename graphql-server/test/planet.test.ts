import server from '../src/server'
// @ts-ignore
import mswServer from './planet.msw'

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
    query: '{planets {name}}'
  })
  expect(result.errors).toBeUndefined()
  expect(result.data?.planets).toEqual([
    {
      name: 'Mars'
    }, {
      name: 'Venus'
    }
  ])
})

it('handle planet details', async () => {
  const result = await app.executeOperation({
    query: '{planets {name, gravity, moons{name}}}'
  })
  expect(result.errors).toBeUndefined()
  expect(result.data?.planets).toEqual([
    {
      name: 'Mars',
      gravity: 1.2,
      moons: [{
        name: 'Mars-Moon'
      }]
    }, {
      name: 'Venus',
      gravity: 2.3,
      moons: [{
        name: 'Venus-Moon'
      }]
    }
  ])
})
