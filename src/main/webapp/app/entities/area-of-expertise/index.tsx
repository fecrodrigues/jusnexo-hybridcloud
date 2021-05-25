import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AreaOfExpertise from './area-of-expertise';
import AreaOfExpertiseDetail from './area-of-expertise-detail';
import AreaOfExpertiseUpdate from './area-of-expertise-update';
import AreaOfExpertiseDeleteDialog from './area-of-expertise-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AreaOfExpertiseUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AreaOfExpertiseUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AreaOfExpertiseDetail} />
      <ErrorBoundaryRoute path={match.url} component={AreaOfExpertise} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AreaOfExpertiseDeleteDialog} />
  </>
);

export default Routes;
