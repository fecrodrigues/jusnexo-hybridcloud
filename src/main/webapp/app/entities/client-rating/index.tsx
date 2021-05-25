import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ClientRating from './client-rating';
import ClientRatingDetail from './client-rating-detail';
import ClientRatingUpdate from './client-rating-update';
import ClientRatingDeleteDialog from './client-rating-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ClientRatingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ClientRatingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ClientRatingDetail} />
      <ErrorBoundaryRoute path={match.url} component={ClientRating} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ClientRatingDeleteDialog} />
  </>
);

export default Routes;
