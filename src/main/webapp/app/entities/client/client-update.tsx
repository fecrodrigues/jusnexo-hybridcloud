import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { setFileData, openFile, byteSize, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICredential } from 'app/shared/model/credential.model';
import { getEntities as getCredentials } from 'app/entities/credential/credential.reducer';
import { IAreaOfExpertise } from 'app/shared/model/area-of-expertise.model';
import { getEntities as getAreaOfExpertises } from 'app/entities/area-of-expertise/area-of-expertise.reducer';
import { IChat } from 'app/shared/model/chat.model';
import { getEntities as getChats } from 'app/entities/chat/chat.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './client.reducer';
import { IClient } from 'app/shared/model/client.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IClientUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ClientUpdate = (props: IClientUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { clientEntity, credentials, areaOfExpertises, chats, loading, updating } = props;

  const { biography, picture, pictureContentType } = clientEntity;

  const handleClose = () => {
    props.history.push('/client' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getCredentials();
    props.getAreaOfExpertises();
    props.getChats();
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...clientEntity,
        ...values,
        credential: credentials.find(it => it.id.toString() === values.credentialId.toString()),
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
          <h2 id="jusnexoApp.client.home.createOrEditLabel" data-cy="ClientCreateUpdateHeading">
            Create or edit a Client
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : clientEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="client-id">ID</Label>
                  <AvInput id="client-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup check>
                <Label id="advocateLabel">
                  <AvInput id="client-advocate" data-cy="advocate" type="checkbox" className="form-check-input" name="advocate" />
                  Advocate
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="biographyLabel" for="client-biography">
                  Biography
                </Label>
                <AvInput id="client-biography" data-cy="biography" type="textarea" name="biography" />
              </AvGroup>
              <AvGroup>
                <Label id="birthdateLabel" for="client-birthdate">
                  Birthdate
                </Label>
                <AvField
                  id="client-birthdate"
                  data-cy="birthdate"
                  type="date"
                  className="form-control"
                  name="birthdate"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="firstnameLabel" for="client-firstname">
                  Firstname
                </Label>
                <AvField
                  id="client-firstname"
                  data-cy="firstname"
                  type="text"
                  name="firstname"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="lastnameLabel" for="client-lastname">
                  Lastname
                </Label>
                <AvField
                  id="client-lastname"
                  data-cy="lastname"
                  type="text"
                  name="lastname"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="oabnumberLabel" for="client-oabnumber">
                  Oabnumber
                </Label>
                <AvField id="client-oabnumber" data-cy="oabnumber" type="text" name="oabnumber" />
              </AvGroup>
              <AvGroup>
                <Label id="phoneLabel" for="client-phone">
                  Phone
                </Label>
                <AvField
                  id="client-phone"
                  data-cy="phone"
                  type="text"
                  name="phone"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="createdAtLabel" for="client-createdAt">
                  Created At
                </Label>
                <AvField id="client-createdAt" data-cy="createdAt" type="date" className="form-control" name="createdAt" />
              </AvGroup>
              <AvGroup>
                <AvGroup>
                  <Label id="pictureLabel" for="picture">
                    Picture
                  </Label>
                  <br />
                  {picture ? (
                    <div>
                      {pictureContentType ? (
                        <a onClick={openFile(pictureContentType, picture)}>
                          <img src={`data:${pictureContentType};base64,${picture}`} style={{ maxHeight: '100px' }} />
                        </a>
                      ) : null}
                      <br />
                      <Row>
                        <Col md="11">
                          <span>
                            {pictureContentType}, {byteSize(picture)}
                          </span>
                        </Col>
                        <Col md="1">
                          <Button color="danger" onClick={clearBlob('picture')}>
                            <FontAwesomeIcon icon="times-circle" />
                          </Button>
                        </Col>
                      </Row>
                    </div>
                  ) : null}
                  <input id="file_picture" data-cy="picture" type="file" onChange={onBlobChange(true, 'picture')} accept="image/*" />
                  <AvInput type="hidden" name="picture" value={picture} />
                </AvGroup>
              </AvGroup>
              <AvGroup>
                <Label for="client-credential">Credential</Label>
                <AvInput id="client-credential" data-cy="credential" type="select" className="form-control" name="credentialId">
                  <option value="" key="0" />
                  {credentials
                    ? credentials.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/client" replace color="info">
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
  credentials: storeState.credential.entities,
  areaOfExpertises: storeState.areaOfExpertise.entities,
  chats: storeState.chat.entities,
  clientEntity: storeState.client.entity,
  loading: storeState.client.loading,
  updating: storeState.client.updating,
  updateSuccess: storeState.client.updateSuccess,
});

const mapDispatchToProps = {
  getCredentials,
  getAreaOfExpertises,
  getChats,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ClientUpdate);
