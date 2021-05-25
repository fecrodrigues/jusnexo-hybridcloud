import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './client.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IClientDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ClientDetail = (props: IClientDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { clientEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="clientDetailsHeading">Client</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{clientEntity.id}</dd>
          <dt>
            <span id="advocate">Advocate</span>
          </dt>
          <dd>{clientEntity.advocate ? 'true' : 'false'}</dd>
          <dt>
            <span id="biography">Biography</span>
          </dt>
          <dd>{clientEntity.biography}</dd>
          <dt>
            <span id="birthdate">Birthdate</span>
          </dt>
          <dd>
            {clientEntity.birthdate ? <TextFormat value={clientEntity.birthdate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="firstname">Firstname</span>
          </dt>
          <dd>{clientEntity.firstname}</dd>
          <dt>
            <span id="lastname">Lastname</span>
          </dt>
          <dd>{clientEntity.lastname}</dd>
          <dt>
            <span id="oabnumber">Oabnumber</span>
          </dt>
          <dd>{clientEntity.oabnumber}</dd>
          <dt>
            <span id="phone">Phone</span>
          </dt>
          <dd>{clientEntity.phone}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>
            {clientEntity.createdAt ? <TextFormat value={clientEntity.createdAt} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="picture">Picture</span>
          </dt>
          <dd>
            {clientEntity.picture ? (
              <div>
                {clientEntity.pictureContentType ? (
                  <a onClick={openFile(clientEntity.pictureContentType, clientEntity.picture)}>
                    <img src={`data:${clientEntity.pictureContentType};base64,${clientEntity.picture}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {clientEntity.pictureContentType}, {byteSize(clientEntity.picture)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>Credential</dt>
          <dd>{clientEntity.credential ? clientEntity.credential.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/client" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/client/${clientEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ client }: IRootState) => ({
  clientEntity: client.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ClientDetail);
