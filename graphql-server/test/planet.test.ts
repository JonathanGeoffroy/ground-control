import server from '../src/server'
// @ts-ignore
import './planet.msw'
import { Planet } from '../src/__generated__/graphql'

const app = server()
beforeAll(() => app.listen())
afterAll(() => app.stop())

it('handles planets', async () => {
  const result = await app.executeOperation({
    query: `
    {
      planets {
        data {
          id
          name
        }
      }
    }`
  })
  expect(result.errors).toBeUndefined()
  expect(result.data?.planets.data).toEqual<Planet[]>([
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
    query: `
    {
      planets {
        data {
          id
          name
          gravity
          size
          moons {
            id
            name
          }
        }
      }
    }`
  })
  expect(result.errors).toBeUndefined()
  expect(result.data?.planets.data).toEqual<Planet[]>([
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

it('handles pagination', async () => {
  const result = await app.executeOperation({
    query: `
    {
      planets(page:0, size: 1) {
        data {
          id
          name
        },
        nextCursor {
          page
          size
        }
      }
    }`
  })

  expect(result.errors).toBeUndefined()
  expect(result.data?.planets.data).toEqual<Planet[]>([
    {
      id: 'mars-id',
      name: 'Mars'
    }
  ])
  expect(result.data?.planets.nextCursor).toEqual({
    page: 1,
    size: 1
  })
})

it('handles last page', async () => {
  const result = await app.executeOperation({
    query: `
    {
      planets(page:1, size: 1) {
        data {
          id
          name
        },
        nextCursor {
          page
          size
        }
      }
    }`
  })

  expect(result.errors).toBeUndefined()
  expect(result.data?.planets.data).toEqual<Planet[]>([
    {
      id: 'venus-id',
      name: 'Venus'
    }
  ])
  expect(result.data?.planets.nextCursor).toBeNull()
})
