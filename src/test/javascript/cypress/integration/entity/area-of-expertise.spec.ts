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

describe('AreaOfExpertise e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/area-of-expertises*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('area-of-expertise');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load AreaOfExpertises', () => {
    cy.intercept('GET', '/api/area-of-expertises*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('area-of-expertise');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('AreaOfExpertise').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details AreaOfExpertise page', () => {
    cy.intercept('GET', '/api/area-of-expertises*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('area-of-expertise');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('areaOfExpertise');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create AreaOfExpertise page', () => {
    cy.intercept('GET', '/api/area-of-expertises*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('area-of-expertise');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('AreaOfExpertise');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit AreaOfExpertise page', () => {
    cy.intercept('GET', '/api/area-of-expertises*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('area-of-expertise');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('AreaOfExpertise');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of AreaOfExpertise', () => {
    cy.intercept('GET', '/api/area-of-expertises*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('area-of-expertise');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('AreaOfExpertise');

    cy.get(`[data-cy="areaName"]`)
      .type('Rua Incrível Corporate', { force: true })
      .invoke('val')
      .should('match', new RegExp('Rua Incrível Corporate'));

    cy.get(`[data-cy="isSelected"]`).should('not.be.checked');
    cy.get(`[data-cy="isSelected"]`).click().should('be.checked');
    cy.setFieldSelectToLastOfEntity('client');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/area-of-expertises*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('area-of-expertise');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of AreaOfExpertise', () => {
    cy.intercept('GET', '/api/area-of-expertises*').as('entitiesRequest');
    cy.intercept('GET', '/api/area-of-expertises/*').as('dialogDeleteRequest');
    cy.intercept('DELETE', '/api/area-of-expertises/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('area-of-expertise');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('areaOfExpertise').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/area-of-expertises*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('area-of-expertise');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
