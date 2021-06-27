describe('App starts', () => {
  it('Starts the app', () => {
    cy.visit('/')
    cy.contains('Oddity')
  })
})
