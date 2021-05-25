import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './area-of-expertise.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAreaOfExpertiseDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AreaOfExpertiseDetail = (props: IAreaOfExpertiseDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { areaOfExpertiseEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="areaOfExpertiseDetailsHeading">AreaOfExpertise</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{areaOfExpertiseEntity.id}</dd>
          <dt>
            <span id="areaName">Area Name</span>
          </dt>
          <dd>{areaOfExpertiseEntity.areaName}</dd>
          <dt>
            <span id="isSelected">Is Selected</span>
          </dt>
          <dd>{areaOfExpertiseEntity.isSelected ? 'true' : 'false'}</dd>
          <dt>Client</dt>
          <dd>
            {areaOfExpertiseEntity.clients
              ? areaOfExpertiseEntity.clients.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {areaOfExpertiseEntity.clients && i === areaOfExpertiseEntity.clients.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/area-of-expertise" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/area-of-expertise/${areaOfExpertiseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ areaOfExpertise }: IRootState) => ({
  areaOfExpertiseEntity: areaOfExpertise.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AreaOfExpertiseDetail);
