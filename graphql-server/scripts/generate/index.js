const { generateApi } = require('swagger-typescript-api')
const { readFileSync, promises: fs } = require('fs')
const { resolve } = require('path')

const { apis } = JSON.parse(
  readFileSync(resolve(__dirname, 'config.json'), { encoding: 'utf-8' })
)

Promise.all(
  apis.map(api =>
    fs.mkdir(resolve(__dirname, `../../src/__generated__/dataSources/${api.name}`), { recursive: true })
      .then(() => generateApi({
        ...api,
        input: resolve(__dirname, api.input),
        templates: resolve(__dirname, './templates'),
        modular: true
      }))
      .then(({ files }) =>
        Promise.all(files.map(({ content, name }) => {
          const path = resolve(__dirname, `../../src/__generated__/dataSources/${api.name}/${name}`)
          return fs.writeFile(path, content, { encoding: 'utf-8' })
        }))
      )
  ))
  .then(() => console.log('All apis generated'))
  .catch(error => {
    console.log(error)
    process.exit(-1)
  })
