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

describe('Client e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/clients*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('client');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Clients', () => {
    cy.intercept('GET', '/api/clients*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('client');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Client').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Client page', () => {
    cy.intercept('GET', '/api/clients*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('client');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('client');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Client page', () => {
    cy.intercept('GET', '/api/clients*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('client');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Client');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Client page', () => {
    cy.intercept('GET', '/api/clients*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('client');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Client');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of Client', () => {
    cy.intercept('GET', '/api/clients*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('client');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Client');

    cy.get(`[data-cy="advocate"]`).should('not.be.checked');
    cy.get(`[data-cy="advocate"]`).click().should('be.checked');

    cy.get(`[data-cy="biography"]`)
      .type('../fake-data/blob/hipster.txt', { force: true })
      .invoke('val')
      .should('match', new RegExp('../fake-data/blob/hipster.txt'));

    cy.get(`[data-cy="birthdate"]`).type('2021-05-25').should('have.value', '2021-05-25');

    cy.get(`[data-cy="firstname"]`).type('Genérico', { force: true }).invoke('val').should('match', new RegExp('Genérico'));

    cy.get(`[data-cy="lastname"]`)
      .type('enable Loan backing', { force: true })
      .invoke('val')
      .should('match', new RegExp('enable Loan backing'));

    cy.get(`[data-cy="oabnumber"]`)
      .type('Sharable Caribbean Borracha', { force: true })
      .invoke('val')
      .should('match', new RegExp('Sharable Caribbean Borracha'));

    cy.get(`[data-cy="phone"]`).type('(98) 8145-1687', { force: true }).invoke('val').should('match', new RegExp('(98) 8145-1687'));

    cy.get(`[data-cy="createdAt"]`).type('2021-05-25').should('have.value', '2021-05-25');

    cy.setFieldImageAsBytesOfEntity('picture', 'integration-test.png', 'image/png');

    cy.setFieldSelectToLastOfEntity('credential');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/clients*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('client');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of Client', () => {
    cy.intercept('GET', '/api/clients*').as('entitiesRequest');
    cy.intercept('GET', '/api/clients/*').as('dialogDeleteRequest');
    cy.intercept('DELETE', '/api/clients/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('client');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('client').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/clients*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('client');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
