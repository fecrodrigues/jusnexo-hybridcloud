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

describe('Message e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/messages*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('message');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Messages', () => {
    cy.intercept('GET', '/api/messages*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('message');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Message').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Message page', () => {
    cy.intercept('GET', '/api/messages*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('message');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('message');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Message page', () => {
    cy.intercept('GET', '/api/messages*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('message');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Message');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Message page', () => {
    cy.intercept('GET', '/api/messages*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('message');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Message');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of Message', () => {
    cy.intercept('GET', '/api/messages*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('message');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Message');

    cy.get(`[data-cy="description"]`)
      .type('Granito Macio revolutionize', { force: true })
      .invoke('val')
      .should('match', new RegExp('Granito Macio revolutionize'));

    cy.get(`[data-cy="createdAt"]`).type('2021-05-25').should('have.value', '2021-05-25');

    cy.setFieldSelectToLastOfEntity('chat');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/messages*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('message');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of Message', () => {
    cy.intercept('GET', '/api/messages*').as('entitiesRequest');
    cy.intercept('GET', '/api/messages/*').as('dialogDeleteRequest');
    cy.intercept('DELETE', '/api/messages/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('message');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('message').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/messages*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('message');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
