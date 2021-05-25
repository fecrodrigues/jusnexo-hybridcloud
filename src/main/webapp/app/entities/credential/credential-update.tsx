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
import { getEntity, updateEntity, createEntity, reset } from './credential.reducer';
import { ICredential } from 'app/shared/model/credential.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICredentialUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CredentialUpdate = (props: ICredentialUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { credentialEntity, clients, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/credential' + props.location.search);
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
        ...credentialEntity,
        ...values,
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
          <h2 id="jusnexoApp.credential.home.createOrEditLabel" data-cy="CredentialCreateUpdateHeading">
            Create or edit a Credential
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : credentialEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="credential-id">ID</Label>
                  <AvInput id="credential-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="passwordLabel" for="credential-password">
                  Password
                </Label>
                <AvField
                  id="credential-password"
                  data-cy="password"
                  type="text"
                  name="password"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="usernameLabel" for="credential-username">
                  Username
                </Label>
                <AvField
                  id="credential-username"
                  data-cy="username"
                  type="text"
                  name="username"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/credential" replace color="info">
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
  credentialEntity: storeState.credential.entity,
  loading: storeState.credential.loading,
  updating: storeState.credential.updating,
  updateSuccess: storeState.credential.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(CredentialUpdate);
