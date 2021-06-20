module.exports = {
  preset: 'ts-jest',
  testEnvironment: 'node',
  transform: {
    '\\.(gql|graphql)$': 'jest-transform-graphql',
    '.*': 'ts-jest'
  },
  coveragePathIgnorePatterns: [
    'node_modules',
    'src/__generated__',
    'src/schema',
    'test'
  ]
}
