import server from '../src/server'
// @ts-ignore
import './planet.msw'

const app = server()

it('handles minimum planet fields creation', async () => {
  const createPlanet = `
    mutation {
      createPlanet(
        planet: {
          name: "A new planet"
        }
      ) {
        name
        gravity
        size
        moons {
          name
        }
      }
    }
  `

  const result = await app.executeOperation({
    query: createPlanet
  })
  expect(result.errors).toBeUndefined()
  expect(result.data).toEqual({
    createPlanet: {
      name: 'A new planet',
      gravity: null,
      size: null,
      moons: []
    }
  })
})

it('handles all planet fields creation', async () => {
  const createPlanet = `
    mutation {
      createPlanet(
        planet: {
          name: "A new planet"
          gravity: 23.45
          size: 1111.2222
          moons: [{ name: "a new moon" }]
        }
      ) {
        name
        gravity
        size
        moons {
          name
        }
      }
    }
  `

  const result = await app.executeOperation({
    query: createPlanet
  })
  expect(result.errors).toBeUndefined()
  expect(result.data).toEqual({
    createPlanet: {
      name: 'A new planet',
      gravity: 23.45,
      size: 1111.2222,
      moons: [
        {
          name: 'a new moon'
        }
      ]
    }
  })
})
