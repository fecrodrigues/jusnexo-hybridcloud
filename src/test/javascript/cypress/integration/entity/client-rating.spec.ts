import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('ClientRating e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/client-ratings*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('client-rating');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load ClientRatings', () => {
    cy.intercept('GET', '/api/client-ratings*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('client-rating');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('ClientRating').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details ClientRating page', () => {
    cy.intercept('GET', '/api/client-ratings*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('client-rating');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('clientRating');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create ClientRating page', () => {
    cy.intercept('GET', '/api/client-ratings*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('client-rating');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ClientRating');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit ClientRating page', () => {
    cy.intercept('GET', '/api/client-ratings*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('client-rating');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('ClientRating');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of ClientRating', () => {
    cy.intercept('GET', '/api/client-ratings*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('client-rating');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ClientRating');

    cy.get(`[data-cy="score"]`).type('81342').should('have.value', '81342');

    cy.get(`[data-cy="description"]`)
      .type('evolve Switchable integrate', { force: true })
      .invoke('val')
      .should('match', new RegExp('evolve Switchable integrate'));

    cy.setFieldSelectToLastOfEntity('client');

    cy.setFieldSelectToLastOfEntity('clientEvaluator');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/client-ratings*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('client-rating');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of ClientRating', () => {
    cy.intercept('GET', '/api/client-ratings*').as('entitiesRequest');
    cy.intercept('GET', '/api/client-ratings/*').as('dialogDeleteRequest');
    cy.intercept('DELETE', '/api/client-ratings/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('client-rating');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('clientRating').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/client-ratings*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('client-rating');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
