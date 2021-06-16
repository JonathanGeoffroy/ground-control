const { CleanWebpackPlugin } = require('clean-webpack-plugin')
const { merge } = require('webpack-merge')
const path = require('path')

const common = require('./webpack.common.js')

module.exports = merge(common, {
  devtool: 'source-map',
  entry: [path.join(__dirname, 'src/index.ts')],
  externalsPresets: { node: true },
  mode: 'production',
  plugins: [new CleanWebpackPlugin()]
})
