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
import { getEntity, updateEntity, createEntity, reset } from './area-of-expertise.reducer';
import { IAreaOfExpertise } from 'app/shared/model/area-of-expertise.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAreaOfExpertiseUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AreaOfExpertiseUpdate = (props: IAreaOfExpertiseUpdateProps) => {
  const [idsclient, setIdsclient] = useState([]);
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { areaOfExpertiseEntity, clients, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/area-of-expertise' + props.location.search);
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
        ...areaOfExpertiseEntity,
        ...values,
        clients: mapIdList(values.clients),
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
          <h2 id="jusnexoApp.areaOfExpertise.home.createOrEditLabel" data-cy="AreaOfExpertiseCreateUpdateHeading">
            Create or edit a AreaOfExpertise
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : areaOfExpertiseEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="area-of-expertise-id">ID</Label>
                  <AvInput id="area-of-expertise-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="areaNameLabel" for="area-of-expertise-areaName">
                  Area Name
                </Label>
                <AvField id="area-of-expertise-areaName" data-cy="areaName" type="text" name="areaName" />
              </AvGroup>
              <AvGroup check>
                <Label id="isSelectedLabel">
                  <AvInput
                    id="area-of-expertise-isSelected"
                    data-cy="isSelected"
                    type="checkbox"
                    className="form-check-input"
                    name="isSelected"
                  />
                  Is Selected
                </Label>
              </AvGroup>
              <AvGroup>
                <Label for="area-of-expertise-client">Client</Label>
                <AvInput
                  id="area-of-expertise-client"
                  data-cy="client"
                  type="select"
                  multiple
                  className="form-control"
                  name="clients"
                  value={!isNew && areaOfExpertiseEntity.clients && areaOfExpertiseEntity.clients.map(e => e.id)}
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
              <Button tag={Link} id="cancel-save" to="/area-of-expertise" replace color="info">
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
  areaOfExpertiseEntity: storeState.areaOfExpertise.entity,
  loading: storeState.areaOfExpertise.loading,
  updating: storeState.areaOfExpertise.updating,
  updateSuccess: storeState.areaOfExpertise.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(AreaOfExpertiseUpdate);
