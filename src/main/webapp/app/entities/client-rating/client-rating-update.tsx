import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IClient } from 'app/shared/model/client.model';
import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { getEntity, updateEntity, createEntity, reset } from './client-rating.reducer';
import { IClientRating } from 'app/shared/model/client-rating.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IClientRatingUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ClientRatingUpdate = (props: IClientRatingUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { clientRatingEntity, clients, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/client-rating' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getClients();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...clientRatingEntity,
        ...values,
        client: clients.find(it => it.id.toString() === values.clientId.toString()),
        clientEvaluator: clients.find(it => it.id.toString() === values.clientEvaluatorId.toString()),
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jusnexoApp.clientRating.home.createOrEditLabel" data-cy="ClientRatingCreateUpdateHeading">
            Create or edit a ClientRating
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : clientRatingEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="client-rating-id">ID</Label>
                  <AvInput id="client-rating-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="scoreLabel" for="client-rating-score">
                  Score
                </Label>
                <AvField
                  id="client-rating-score"
                  data-cy="score"
                  type="string"
                  className="form-control"
                  name="score"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="descriptionLabel" for="client-rating-description">
                  Description
                </Label>
                <AvField
                  id="client-rating-description"
                  data-cy="description"
                  type="text"
                  name="description"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="client-rating-client">Client</Label>
                <AvInput id="client-rating-client" data-cy="client" type="select" className="form-control" name="clientId">
                  <option value="" key="0" />
                  {clients
                    ? clients.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="client-rating-clientEvaluator">Client Evaluator</Label>
                <AvInput
                  id="client-rating-clientEvaluator"
                  data-cy="clientEvaluator"
                  type="select"
                  className="form-control"
                  name="clientEvaluatorId"
                >
                  <option value="" key="0" />
                  {clients
                    ? clients.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/client-rating" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  clients: storeState.client.entities,
  clientRatingEntity: storeState.clientRating.entity,
  loading: storeState.clientRating.loading,
  updating: storeState.clientRating.updating,
  updateSuccess: storeState.clientRating.updateSuccess,
});

const mapDispatchToProps = {
  getClients,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ClientRatingUpdate);
