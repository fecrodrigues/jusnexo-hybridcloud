import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';

import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Entities" id="entity-menu" data-cy="entity" style={{ maxHeight: '80vh', overflow: 'auto' }}>
    <MenuItem icon="asterisk" to="/client">
      Client
    </MenuItem>
    <MenuItem icon="asterisk" to="/chat">
      Chat
    </MenuItem>
    <MenuItem icon="asterisk" to="/message">
      Message
    </MenuItem>
    <MenuItem icon="asterisk" to="/client-rating">
      Client Rating
    </MenuItem>
    <MenuItem icon="asterisk" to="/credential">
      Credential
    </MenuItem>
    <MenuItem icon="asterisk" to="/area-of-expertise">
      Area Of Expertise
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
