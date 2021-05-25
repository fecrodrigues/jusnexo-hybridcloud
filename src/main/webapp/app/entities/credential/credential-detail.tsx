import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './credential.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICredentialDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CredentialDetail = (props: ICredentialDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { credentialEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="credentialDetailsHeading">Credential</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{credentialEntity.id}</dd>
          <dt>
            <span id="password">Password</span>
          </dt>
          <dd>{credentialEntity.password}</dd>
          <dt>
            <span id="username">Username</span>
          </dt>
          <dd>{credentialEntity.username}</dd>
        </dl>
        <Button tag={Link} to="/credential" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/credential/${credentialEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ credential }: IRootState) => ({
  credentialEntity: credential.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CredentialDetail);
