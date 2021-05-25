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

describe('Chat e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/chats*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('chat');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Chats', () => {
    cy.intercept('GET', '/api/chats*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('chat');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Chat').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Chat page', () => {
    cy.intercept('GET', '/api/chats*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('chat');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('chat');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Chat page', () => {
    cy.intercept('GET', '/api/chats*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('chat');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Chat');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Chat page', () => {
    cy.intercept('GET', '/api/chats*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('chat');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Chat');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of Chat', () => {
    cy.intercept('GET', '/api/chats*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('chat');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Chat');

    cy.get(`[data-cy="createdAt"]`).type('2021-05-25').should('have.value', '2021-05-25');

    cy.setFieldSelectToLastOfEntity('clientReceiver');

    cy.setFieldSelectToLastOfEntity('clientSender');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/chats*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('chat');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of Chat', () => {
    cy.intercept('GET', '/api/chats*').as('entitiesRequest');
    cy.intercept('GET', '/api/chats/*').as('dialogDeleteRequest');
    cy.intercept('DELETE', '/api/chats/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('chat');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('chat').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/chats*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('chat');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
