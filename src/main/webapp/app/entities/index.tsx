import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Client from './client';
import Chat from './chat';
import Message from './message';
import ClientRating from './client-rating';
import Credential from './credential';
import AreaOfExpertise from './area-of-expertise';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}client`} component={Client} />
      <ErrorBoundaryRoute path={`${match.url}chat`} component={Chat} />
      <ErrorBoundaryRoute path={`${match.url}message`} component={Message} />
      <ErrorBoundaryRoute path={`${match.url}client-rating`} component={ClientRating} />
      <ErrorBoundaryRoute path={`${match.url}credential`} component={Credential} />
      <ErrorBoundaryRoute path={`${match.url}area-of-expertise`} component={AreaOfExpertise} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
