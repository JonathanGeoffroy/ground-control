const colors = require('tailwindcss/colors')

module.exports = {
  purge: ['./src/**/*.{js,jsx,ts,tsx}', './public/index.html'],
  darkMode: 'media', // or 'media' or 'class'
  theme: {
    colors: {
      white: '#eeeeee',
      black: '#0c141f',
      blue: {
        light: '#E6FFFF',
        DEFAULT: '#6FC3DF',
        dark: '#2894B8'
      },
      primary: '#6FC3DF',
      yellow: '#FFD400',
      orange: 'DF740C',
      red: '#DC3B40',
      gray: colors.coolGray
    }
  },
  variants: {
    extend: {}
  },
  plugins: []
}
