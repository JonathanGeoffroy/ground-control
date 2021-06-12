import PlanetAPI from '../__generated__/dataSources/Planet/Planet'

export interface DataSources {
  planetAPI: PlanetAPI
}
export default (): DataSources => {
  return {
    planetAPI: new PlanetAPI()
  }
}
