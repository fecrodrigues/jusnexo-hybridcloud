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
import { getEntity, updateEntity, createEntity, reset } from './chat.reducer';
import { IChat } from 'app/shared/model/chat.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IChatUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ChatUpdate = (props: IChatUpdateProps) => {
  const [idsclientReceiver, setIdsclientReceiver] = useState([]);
  const [idsclientSender, setIdsclientSender] = useState([]);
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { chatEntity, clients, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/chat' + props.location.search);
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
        ...chatEntity,
        ...values,
        clientReceivers: mapIdList(values.clientReceivers),
        clientSenders: mapIdList(values.clientSenders),
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
          <h2 id="jusnexoApp.chat.home.createOrEditLabel" data-cy="ChatCreateUpdateHeading">
            Create or edit a Chat
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : chatEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="chat-id">ID</Label>
                  <AvInput id="chat-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdAtLabel" for="chat-createdAt">
                  Created At
                </Label>
                <AvField id="chat-createdAt" data-cy="createdAt" type="date" className="form-control" name="createdAt" />
              </AvGroup>
              <AvGroup>
                <Label for="chat-clientReceiver">Client Receiver</Label>
                <AvInput
                  id="chat-clientReceiver"
                  data-cy="clientReceiver"
                  type="select"
                  multiple
                  className="form-control"
                  name="clientReceivers"
                  value={!isNew && chatEntity.clientReceivers && chatEntity.clientReceivers.map(e => e.id)}
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
              <AvGroup>
                <Label for="chat-clientSender">Client Sender</Label>
                <AvInput
                  id="chat-clientSender"
                  data-cy="clientSender"
                  type="select"
                  multiple
                  className="form-control"
                  name="clientSenders"
                  value={!isNew && chatEntity.clientSenders && chatEntity.clientSenders.map(e => e.id)}
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
              <Button tag={Link} id="cancel-save" to="/chat" replace color="info">
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
  chatEntity: storeState.chat.entity,
  loading: storeState.chat.loading,
  updating: storeState.chat.updating,
  updateSuccess: storeState.chat.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(ChatUpdate);
